package com.d106.arti.artwork.service;

import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_ARTWORK;
import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;
import static com.d106.arti.global.exception.ExceptionCode.NOT_FOUND_THEME_ID;

import com.d106.arti.artwork.domain.AiArtwork;
import com.d106.arti.artwork.domain.Artwork;
import com.d106.arti.artwork.dto.request.AiArtworkRequest;
import com.d106.arti.artwork.dto.response.AiArtworkResponse;

import com.d106.arti.artwork.repository.AiArtworkRepository;
import com.d106.arti.artwork.repository.ArtworkRepository;

import com.d106.arti.gallery.repository.ThemeRepository;

import com.d106.arti.gallery.domain.Theme;
import com.d106.arti.global.exception.BadRequestException;
import com.d106.arti.member.domain.Member;
import com.d106.arti.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiArtworkService {

    private final AiArtworkRepository aiArtworkRepository;
    private final ArtworkRepository normalArtworkRepository; // Original Image 조회를 위한 repository
    private final MemberRepository memberRepository; // Member 조회를 위한 repository
    private final ThemeRepository themeRepository; // Theme 조회를 위한 repository

    // AI 미술품 생성
    @Transactional
    public AiArtworkResponse createAiArtwork(AiArtworkRequest request) {
        Artwork originalImage = normalArtworkRepository.findById(request.getOriginalImageId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_ARTWORK));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        Theme theme = themeRepository.findById(request.getThemeId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_THEME_ID));


        AiArtwork aiArtwork = request.toAiArtwork(originalImage, member, theme);
        AiArtwork savedArtwork = aiArtworkRepository.save(aiArtwork);

        return AiArtworkResponse.toAiArtworkResponse(savedArtwork);
    }

    // AI 미술품 삭제
    @Transactional
    public void deleteAiArtwork(Integer aiArtworkId) {
        AiArtwork aiArtwork = aiArtworkRepository.findById(aiArtworkId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_ARTWORK));
        aiArtworkRepository.delete(aiArtwork);
    }

    // 특정 멤버의 AI 미술품 조회
    @Transactional(readOnly = true)
    public List<AiArtworkResponse> getAiArtworksByMemberId(Integer memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        List<AiArtwork> aiArtworks = aiArtworkRepository.findByMember(member);

        return aiArtworks.stream()
            .map(AiArtworkResponse::toAiArtworkResponse)
            .collect(Collectors.toList());
    }


}
