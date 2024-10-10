package com.d106.arti.gallery.service;

import com.d106.arti.artwork.domain.AiArtwork;
import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.artwork.domain.ArtworkTheme;
import com.d106.arti.artwork.domain.NormalArtWork;
import com.d106.arti.artwork.dto.response.ArtworkResponse;
import com.d106.arti.artwork.repository.AiArtworkRepository;
import com.d106.arti.artwork.repository.ArtworkRepository;
import com.d106.arti.artwork.repository.ArtworkThemeRepository;
import com.d106.arti.gallery.domain.Gallery;
import com.d106.arti.gallery.domain.Genre;
import com.d106.arti.gallery.domain.Theme;

import com.d106.arti.gallery.dto.request.GalleryRequest;
import com.d106.arti.gallery.dto.request.ThemeRequest;
import com.d106.arti.gallery.dto.response.GalleryResponse;
import com.d106.arti.gallery.dto.response.SubscribedGalleryResponse;
import com.d106.arti.gallery.dto.response.ThemeResponse;
import com.d106.arti.gallery.dto.response.ThemeWithArtworksResponse;
import com.d106.arti.gallery.repository.GalleryRepository;
import com.d106.arti.gallery.repository.ThemeRepository;
import com.d106.arti.global.exception.BadRequestException;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;
import com.d106.arti.storage.StorageService;

import java.util.Collections;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

import static com.d106.arti.global.exception.ExceptionCode.*;

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
    private final StorageService storageService;
    private final AiArtworkRepository aiArtworkRepository;

    @Transactional
    public GalleryResponse createGallery(GalleryRequest requestDto, MultipartFile image) {
        // 소유자(Member) 조회
        Member owner = memberRepository.findById(requestDto.getOwnerId())
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_OWNER_ID));

        // 이미지 저장
        String imageUrl = storageService.storeFile(image).getStoreFilename();

        // Gallery 엔티티 생성 및 저장
        Gallery gallery = Gallery.builder()
            .id(owner.getId())
            .name(requestDto.getName())
            .description(requestDto.getDescription())
            .image(imageUrl)  // 저장한 이미지 URL 설정
            .owner(owner)
            .view(0)  // 초기 조회 수는 0
            .build();

        galleryRepository.save(gallery);
        return GalleryResponse.fromEntity(gallery);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "randomGalleries", allEntries = true, cacheManager = "rcm"),
        @CacheEvict(value = "searchGalleries", allEntries = true, cacheManager = "rcm")
    })
    public GalleryResponse updateGallery(Integer galleryId, GalleryRequest requestDto, MultipartFile image) {
        Gallery gallery = galleryRepository.findById(galleryId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_GALLERY_ID));

        // 기존 이미지를 유지하거나 새로운 이미지를 저장
        String imageUrl = gallery.getImage();
        if (image != null && !image.isEmpty()) {
            imageUrl = storageService.storeFile(image).getStoreFilename();
        }

        // Gallery 엔티티 수정 및 저장
        gallery = Gallery.builder()
            .id(gallery.getId())
            .name(requestDto.getName())
            .description(requestDto.getDescription())
            .image(imageUrl)
            .view(gallery.getView())  // 기존 조회 수 유지
            .owner(gallery.getOwner())
            .themes(gallery.getThemes())  // 기존 테마 유지
            .build();

        galleryRepository.save(gallery);
        return GalleryResponse.fromEntity(gallery);
    }

    // 2. 미술관 상세 조회
    @Transactional(readOnly = true)
    public GalleryResponse getGalleryById(Integer galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_GALLERY_ID));
        return GalleryResponse.fromEntity(gallery);
    }




    // 1. 특정 미술관 id를 받아서 테마 전체 조회되게 변경
    @Transactional(readOnly = true)
//    @Cacheable(cacheNames = "themesByGalleryId", key = "#galleryId", sync = true, cacheManager = "rcm")
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
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_GALLERY_ID));

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
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new BadRequestException(NOT_FOUND_THEME_ID));
        Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new BadRequestException(NOT_FOUND_ARTWORK_ID));

        theme.addArtwork(artwork, description);
        themeRepository.save(theme);
    }

    @Transactional
    public void addAiArtworkToTheme(Integer themeId, Integer artworkId, String description) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new BadRequestException(NOT_FOUND_THEME_ID));
        Artwork artwork = aiArtworkRepository.findById(artworkId).orElseThrow(() -> new BadRequestException(NOT_FOUND_ARTWORK_ID));
        theme.addArtwork(artwork, description);
        themeRepository.save(theme);
    }


    // 4. 테마에서 미술품 삭제
    @Transactional
    public void removeArtworkFromTheme(Integer themeId, Integer artworkId) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new BadRequestException(NOT_FOUND_THEME_ID));
        Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(() -> new BadRequestException(NOT_FOUND_ARTWORK_ID));

        theme.removeArtwork(artwork);
        themeRepository.save(theme);
    }

    @Transactional
    public void removeAiArtworkFromTheme(Integer themeId, Integer artworkId) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new BadRequestException(NOT_FOUND_THEME_ID));
        Artwork artwork = aiArtworkRepository.findById(artworkId).orElseThrow(() -> new BadRequestException(NOT_FOUND_ARTWORK_ID));
        theme.removeArtwork(artwork);
        themeRepository.save(theme);
    }

    // 5. 테마 수정
    @Transactional
    public ThemeResponse updateTheme(Integer themeId, ThemeRequest themeRequest) {
        // 기존 테마 조회
        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_THEME_ID));

        // 이름 업데이트
        theme.updateName(themeRequest.getName());

        // 갤러리 업데이트: 갤러리 조회 후 설정
        Gallery gallery = galleryRepository.findById(themeRequest.getGalleryId())
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_GALLERY_ID));
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
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_GALLERY_ID));


        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_THEME_ID));

        // 갤러리 ID 확인
        if (!theme.getGallery().getId().equals(galleryId)) {
            throw new BadRequestException(NOT_FOUND_THEME_WITH_GALLERY);
        }

        themeRepository.deleteById(themeId);
    }

    // 테마에 담긴 모든 미술품 조회, 길이 너비 추가해야 할듯?
    @Transactional(readOnly = true)
//    @Cacheable(cacheNames = "artworksByThemeId", key = "#themeId", sync = true, cacheManager = "rcm")
    public List<ArtworkResponse> getArtworksByThemeId(Integer themeId) {
        // Theme 조회
        Theme theme = themeRepository.findById(themeId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_THEME_ID));

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
                        .year(aiArtwork.getYear())
                        .artist(aiArtwork.getMember().getNickname())
                        .build();
                } else if (artwork instanceof NormalArtWork) {
                    NormalArtWork normalArtwork = (NormalArtWork) artwork;
                    return ArtworkResponse.builder()
                        .id(normalArtwork.getId())
                        .title(normalArtwork.getTitle()) // 일반 작품의 제목 사용
                        .artist(normalArtwork.getArtist().getEngName())
                        .year(normalArtwork.getYear())
                        .description(normalArtwork.getDescription()) // 일반 작품의 설명
                        .imageUrl(imageBaseUrl+normalArtwork.getFilename())
                        .build();
                } else {
                    throw new BadRequestException(INVALID_ARTWORK_TYPE);
                }
            })
            .collect(Collectors.toList());
    }

    // 장르에 해당하는 미술품 50개 랜덤으로 가져오기
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "artworksByGenre", key = "#genreLabel", sync = true, cacheManager = "rcm")
    public List<ArtworkResponse> getRandomArtworksByGenre(String genreLabel) {
        // 1. 입력받은 genreLabel을 대문자로 변환하여 Enum에서 확인
        String formattedGenreLabel = genreLabel.trim().toUpperCase();  // 입력값을 대문자로 변환

        // 2. ArtGenre enum에서 변환된 label을 사용해 해당 장르 Enum 확인
        Genre genre;
        try {
            genre = Genre.valueOf(formattedGenreLabel);  // Enum 값이 존재하는지 확인
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(INVALID_GENRE); // 유효하지 않은 장르일 경우 예외 처리
        }

        // 3. Enum이 존재하면, 언더바를 공백으로 변환하여 검색에 사용할 label 생성
        String genreLabelForSearch = formattedGenreLabel.replace("_", " ");
        // 2. 작품 중에서 해당 장르가 포함된 미술품 조회
        List<NormalArtWork> artworks = artworkRepository.findAllByGenreContaining(genreLabelForSearch);

        // 3. 작품이 50개 이상일 경우 랜덤하게 50개 선택
        if (artworks.size() > 50) {
            Random random = new Random();
            artworks = random.ints(0, artworks.size())
                .distinct()
                .limit(50)
                .mapToObj(artworks::get)
                .collect(Collectors.toList());
        }

        // 4. Artwork 엔티티들을 ArtworkResponse DTO로 변환하여 반환
        return artworks.stream()
            .filter(artwork -> artwork instanceof NormalArtWork)  // NormalArtWork만 필터링
            .map(artwork -> {
                NormalArtWork normalArtwork = (NormalArtWork) artwork;
                return ArtworkResponse.builder()
                    .id(normalArtwork.getId())
                    .title(normalArtwork.getTitle())  // 일반 작품의 제목 사용
                    .artist(normalArtwork.getArtist().getEngName())
                    .year(normalArtwork.getYear())
                    .description(normalArtwork.getDescription())  // 일반 작품의 설명
                    .imageUrl(imageBaseUrl + normalArtwork.getFilename())  // 이미지 URL 생성
                    .build();
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
//    @Cacheable(cacheNames = "subGalleriesByMemberId", key = "#memberId", sync = true, cacheManager = "rcm")
    public List<SubscribedGalleryResponse> getSubscribedGalleriesByMemberId(Integer memberId) {
        // memberId로 Member 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        // Member의 구독된 갤러리 ID 리스트 조회
        List<Integer> subscribedGalleryIds = member.getSubscribedGalleryIds();

        // 갤러리 ID 목록으로 갤러리들 조회
        List<Gallery> galleries = galleryRepository.findAllByIdIn(subscribedGalleryIds);

        // 조회된 갤러리들을 SubscribedGalleryResponse로 변환하여 반환
        return galleries.stream()
            .map(SubscribedGalleryResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "randomGalleries", key = "#root.target + #root.methodName", sync = true, cacheManager = "rcm")
    public List<GalleryResponse> getRandomGalleries() {
        // 모든 미술관을 조회
        List<Gallery> galleries = galleryRepository.findAll();

//        // 미술관이 50개 미만일 경우 처리
//        if (galleries.size() < 50) {
//            throw new BadRequestException(NOT_FOUND_GALLERY_ID);
//        }

        // 리스트를 무작위로 섞는다
        Collections.shuffle(galleries);

        // 50개의 미술관을 선택하고 GalleryResponse로 변환
        return galleries.stream()
            .limit(50)
            .map(GalleryResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "searchGalleries", key = "#keyword", sync = true, cacheManager = "rcm")
    public List<GalleryResponse> searchGalleryByName(String keyword) {
        // GalleryRepository에서 부분 검색 수행
        List<Gallery> galleries = galleryRepository.findByNameContaining(keyword);

        if (galleries.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_GALLERY_ID);
        }

        // 검색된 갤러리를 GalleryResponse로 변환하여 반환
        return galleries.stream()
            .map(GalleryResponse::fromEntity)
            .collect(Collectors.toList());
    }

    // 특정 미술관 ID로 테마와 그 테마에 속한 모든 미술품 조회
    @Transactional(readOnly = true)
//    @Cacheable(value = "allGalleriesThings", key = "#galleryId", sync = true, cacheManager = "rcm")
    public List<ThemeWithArtworksResponse> getAllThemesWithArtworksByGalleryId(Integer galleryId) {
        // 특정 galleryId에 속한 테마 조회
        List<Theme> themes = themeRepository.findByGalleryId(galleryId);

        // 각 테마에 속한 미술품들을 조회하고, 함께 DTO로 변환
        return themes.stream()
            .map(theme -> {
                List<ArtworkResponse> artworks = theme.getArtworks().stream()
                    .map(ArtworkTheme::getArtwork)
                    .map(artwork -> {
                        if (artwork instanceof AiArtwork) {
                            AiArtwork aiArtwork = (AiArtwork) artwork;
                            return ArtworkResponse.builder()
                                .id(aiArtwork.getId())
                                .title(aiArtwork.getAiArtworkTitle())
                                .description(aiArtwork.getArtworkImage())
                                .imageUrl(aiArtwork.getArtworkImage())
                                .artist(aiArtwork.getMember().getNickname())
                                .year(aiArtwork.getYear())
                                .build();
                        } else if (artwork instanceof NormalArtWork) {
                            NormalArtWork normalArtwork = (NormalArtWork) artwork;
                            return ArtworkResponse.builder()
                                .id(normalArtwork.getId())
                                .title(normalArtwork.getTitle())
                                .artist(normalArtwork.getArtist().getEngName())
                                .year(normalArtwork.getYear())
                                .description(normalArtwork.getDescription())
                                .imageUrl(imageBaseUrl + normalArtwork.getFilename())
                                .build();
                        } else {
                            throw new BadRequestException(INVALID_ARTWORK_TYPE);
                        }
                    })
                    .collect(Collectors.toList());

                // 테마와 해당 미술품들을 ThemeWithArtworksResponse로 변환하여 반환
                return ThemeWithArtworksResponse.builder()
                    .themeId(theme.getId())
                    .themeName(theme.getName())
                    .artworks(artworks)
                    .build();
            })
            .collect(Collectors.toList());
    }


}
