package com.onthe7.petking.module.user.interfaces;

import com.onthe7.petking.common.util.RequestIdGenerator;
import com.onthe7.petking.common.vo.Response;
import com.onthe7.petking.module.user.application.KakaoLoginService;
import com.onthe7.petking.module.user.domain.dto.KakaoTokenDto;
import com.onthe7.petking.module.user.domain.dto.KakaoUserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoLoginService kakaoLoginService;
    private final RequestIdGenerator requestIdGenerator;

    /**
     * 인가 code 요청
     * TODO: 추후 해당 부분은 클라이언트에서 요청할것임, 클라이언트가 해당부분 구현전 임시 사용
     */
    @GetMapping("/oauth")
    public String getCode() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + "9d51e4a8826ac06ed399457a814220ac");
        url.append("&redirect_uri=http://localhost:8081/kakao/token");
        url.append("&response_type=code");
        url.append("&nonce=" + "96efbc43a462ab9d9c6a8173e5b322e17f218b56eb3a05a4bbc53221adebc7b3");

        return "redirect:" + url;
    }

    /**
     * 인가 code 로 토큰 요청
     */
    @GetMapping("/token")
    public ResponseEntity<Response> getKakaoAccessToken(@RequestParam String code) throws IOException {
        String requestId = requestIdGenerator.getRequestId();

        KakaoTokenDto result = kakaoLoginService.getAccessToken(code);
        return ResponseEntity.ok(Response.success(requestId, result));
    }

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<Response> refreshAccessToken(@RequestParam String refreshToken) throws IOException {
        String requestId = requestIdGenerator.getRequestId();

        KakaoTokenDto result = kakaoLoginService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(Response.success(requestId, result));
    }

}