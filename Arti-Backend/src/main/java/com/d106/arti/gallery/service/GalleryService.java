package com.d106.arti.gallery.service;

import com.d106.arti.artwork.domain.AiArtwork;
import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.artwork.domain.ArtworkTheme;
import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.dto.response.ArtworkResponse;
import com.d106.arti.artwork.repository.ArtworkRepository;
import com.d106.arti.artwork.repository.ArtworkThemeRepository;
import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.domain.Theme;

import com.d106.arti.gallery.dto.request.GalleryRequest;
import com.d106.arti.gallery.dto.request.ThemeRequest;
import com.d106.arti.gallery.dto.response.GalleryResponse;
import com.d106.arti.gallery.dto.response.ThemeResponse;
import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.gallery.repository.ThemeRepository;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryService {


    @Value("${server.image.base-url}")
    private String imageBaseUrl;

    private final ThemeRepository themeRepository;
    private final ArtworkRepository artworkRepository;
    private final ArtworkThemeRepository artworkThemeRepository;
    private final GalleryRepository galleryRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public GalleryResponse createGallery(GalleryRequest requestDto) {
        // 소유자(Member) 조회(로그인 한 멤버와 일치하는지 확인)
        Member owner = memberRepository.findById(requestDto.getOwnerId())
            .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        // Gallery 엔티티 생성 및 저장
        Gallery gallery = Gallery.builder()
            .name(requestDto.getName())
            .description(requestDto.getDescription())
            .image(requestDto.getImage())
            .owner(owner)
            .view(0)  // 초기 조회 수는 0
            .build();

        galleryRepository.save(gallery);
        return GalleryResponse.fromEntity(gallery);
    }

    // 2. 미술관 상세 조회
    @Transactional(readOnly = true)
    public GalleryResponse getGalleryById(Integer galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
            .orElseThrow(() -> new EntityNotFoundException("Gallery not found"));
        return GalleryResponse.fromEntity(gallery);
    }

    // 3. 미술관 정보 수정
    @Transactional
    public GalleryResponse updateGallery(Integer galleryId, GalleryRequest requestDto) {
        Gallery gallery = galleryRepository.findById(galleryId)
            .orElseThrow(() -> new EntityNotFoundException("Gallery not found"));



        gallery = Gallery.builder()
            .id(gallery.getId())
            .name(requestDto.getName())
            .description(requestDto.getDescription())
            .image(requestDto.getImage())
            .view(gallery.getView())  // 기존 조회수 유지
            .owner(gallery.getOwner())
            .themes(gallery.getThemes())  // 기존 테마 유지
            .build();

        galleryRepository.save(gallery);
        return GalleryResponse.fromEntity(gallery);
    }


    // 1. 특정 미술관 id를 받아서 테마 전체 조회되게 변경
    @Transactional(readOnly = true)
    public List<ThemeResponse> getAllThemesByGalleryId(Integer galleryId) {
        List<Theme> themes = themeRepository.findByGalleryId(galleryId);
        return themes.stream()
            .map(ThemeResponse::fromEntity)
            .collect(Collectors.toList());
    }

    // 2. 미술관에 테마 생성
    @Transactional
    public ThemeResponse createTheme(ThemeRequest themeRequest) {
        // Gallery 객체를 galleryId로 조회
        Gallery gallery = galleryRepository.findById(themeRequest.getGalleryId())
            .orElseThrow(() -> new IllegalArgumentException("Gallery not found"));

        // Theme 객체 생성
        Theme theme = Theme.builder()
            .name(themeRequest.getName())
            .gallery(gallery)  // Gallery 객체를 설정
            .build();

        // Theme 저장
        themeRepository.save(theme);

        // ThemeResponseDto 반환
        return ThemeResponse.fromEntity(theme);
    }



    // 3. 테마에 미술품 추가
    @Transactional
    public void addArtworkToTheme(Integer themeId, Integer artworkId, String description) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("Invalid theme ID"));
        Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new IllegalArgumentException("Invalid artwork ID"));

        theme.addArtwork(artwork, description);
        themeRepository.save(theme);
    }

    // 4. 테마에서 미술품 삭제
    @Transactional
    public void removeArtworkFromTheme(Integer themeId, Integer artworkId) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("Invalid theme ID"));
        Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new IllegalArgumentException("Invalid artwork ID"));

        theme.removeArtwork(artwork);
        themeRepository.save(theme);
    }

    // 5. 테마 수정
    @Transactional
    public ThemeResponse updateTheme(Integer themeId, ThemeRequest themeRequest) {
        // 기존 테마 조회
        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid theme ID"));

        // 이름 업데이트
        theme.updateName(themeRequest.getName());

        // 갤러리 업데이트: 갤러리 조회 후 설정
        Gallery gallery = galleryRepository.findById(themeRequest.getGalleryId())
            .orElseThrow(() -> new EntityNotFoundException("Gallery not found"));
        theme.updateGallery(gallery);  // 기존 갤러리로 업데이트

        // 테마 저장
        themeRepository.save(theme);

        // 응답 DTO 반환
        return ThemeResponse.fromEntity(theme);
    }


    // 6. 테마 삭제(특정 갤러리(나의 갤러리로 추후에 변경)의 특정 테마 삭제)
    @Transactional
    public void deleteTheme(Integer galleryId, Integer themeId) {
        Gallery gallery = galleryRepository.findById(galleryId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid gallery ID"));


        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid theme ID"));

        // 갤러리 ID 확인
        if (!theme.getGallery().getId().equals(galleryId)) {
            throw new IllegalArgumentException("Theme does not belong to the specified gallery");
        }

        themeRepository.deleteById(themeId);
    }

    // 테마에 담긴 모든 미술품 조회, 길이 너비 추가해야 할듯?
    @Transactional(readOnly = true)
    public List<ArtworkResponse> getArtworksByThemeId(Integer themeId) {
        // Theme 조회
        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid theme ID"));

        // 해당 테마에 속한 모든 Artwork 조회 (AiArtwork와 NormalArtWork 모두 포함)
        List<Artwork> artworks = theme.getArtworks().stream()
            .map(ArtworkTheme::getArtwork)
            .collect(Collectors.toList());

        // Artwork 엔티티들을 ArtworkResponse DTO로 변환
        return artworks.stream()
            .map(artwork -> {
                if (artwork instanceof AiArtwork) {
                    AiArtwork aiArtwork = (AiArtwork) artwork;
                    return ArtworkResponse.builder()
                        .id(aiArtwork.getId())
                        .title(aiArtwork.getAiArtworkTitle()) // AI 작품의 제목 사용
                        .description(aiArtwork.getArtworkImage()) // 이미지를 설명으로 예시
                        .imageUrl(aiArtwork.getArtworkImage())
                        .build();
                } else if (artwork instanceof NormalArtWork) {
                    NormalArtWork normalArtwork = (NormalArtWork) artwork;
                    return ArtworkResponse.builder()
                        .id(normalArtwork.getId())
                        .title(normalArtwork.getTitle()) // 일반 작품의 제목 사용
                        .description(normalArtwork.getDescription()) // 일반 작품의 설명
                        .imageUrl(imageBaseUrl+normalArtwork.getFilename())
                        .build();
                } else {
                    throw new IllegalArgumentException("Unknown artwork type");
                }
            })
            .collect(Collectors.toList());
    }
}
