package com.d106.arti.global.interceptor;

import com.d106.arti.elasticsearch.repository.ViewCountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class StaticRequestInterceptor implements HandlerInterceptor {

    private final ViewCountRepository viewCountRepository;

    @Value("${cloud.aws.s3.base-url}")
    private String s3BaseUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // viewCount 테이블에서 해당 URI가 존재하는지 확인
        if (viewCountRepository.existsByRequestURI(requestURI)) {
            // 해당 값이 존재하면 S3로 리다이렉트
            int index = requestURI.lastIndexOf('/');
            String filename = requestURI.substring(index + 1);
            response.sendRedirect(s3BaseUrl + filename);
            return false; // 컨트롤러로 요청을 넘기지 않음
        }

        return true; // 계속 진행
    }
}
