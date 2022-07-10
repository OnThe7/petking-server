package com.onthe7.petking.module.user.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onthe7.petking.common.enums.GrantType;
import com.onthe7.petking.common.exception.JwtException;
import com.onthe7.petking.common.exception.UserException;
import com.onthe7.petking.module.user.domain.dto.KakaoTokenDto;
import com.onthe7.petking.module.user.domain.dto.KakaoUserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoLoginService {

    @Value("${kakao.api-key}")
    private String REST_API_KEY;

    @Value("${kakao.endpoint.oauth}")
    private String OAUTH_ENDPOINT;

    @Value("${kakao.endpoint.user}")
    private String USER_ENDPOINT;

    @Value("${kakao.endpoint.oauth-callback}")
    private String CALLBACK_URI;

    // https://kauth.kakao.com/oauth/token
    public KakaoTokenDto getAccessToken(String code) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", GrantType.AUTH_CODE.getValue())
                .add("client_id", REST_API_KEY)
                .add("redirect_uri", CALLBACK_URI)
                .add("code", code)
                .build();

        Request request = new Request.Builder()
                .url(OAUTH_ENDPOINT + "/oauth/token")
                .post(formBody)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        KakaoTokenDto kakaoTokenDto = null;

        okhttp3.Response httpResponse = client.newCall(request).execute();

        if (httpResponse.isSuccessful() == true) {
            String responseBody = httpResponse.body().string();
            log.debug("[Response Body] " + responseBody);
            kakaoTokenDto = objectMapper.readValue(responseBody, KakaoTokenDto.class);

        } else {
            throw new JwtException.JwtCreatedFailedException();
        }

        return kakaoTokenDto;
    }

    // https://kauth.kakao.com/oauth/token
    public KakaoTokenDto refreshAccessToken(String refreshToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", GrantType.REFRESH_TOKEN.getValue())
                .add("client_id", REST_API_KEY)
                .add("refresh_token", refreshToken)
                .build();

        Request request = new Request.Builder()
                .url(OAUTH_ENDPOINT + "/oauth/token")
                .post(formBody)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        KakaoTokenDto kakaoTokenDto = null;

        okhttp3.Response httpResponse = client.newCall(request).execute();

        if (httpResponse.isSuccessful() == true) {
            String responseBody = httpResponse.body().string();
            log.debug("[Response Body] " + responseBody);
            kakaoTokenDto = objectMapper.readValue(responseBody, KakaoTokenDto.class);

        } else {
            throw new JwtException.JwtCreatedFailedException();
        }

        return kakaoTokenDto;
    }

    // https://kapi.kakao.com/v2/user/me
    public KakaoUserInfoDto getUserInfo(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(USER_ENDPOINT + "/v2/user/me")
                .addHeader("Authorization", String.format("Bearer %s", accessToken))
                .build();

        KakaoUserInfoDto kakaoUserInfoDto = null;

        okhttp3.Response httpResponse = client.newCall(request).execute();

        if (httpResponse.isSuccessful() == true) {
            String responseBody = httpResponse.body().string();
            log.debug("[Response Body] " + responseBody);
            kakaoUserInfoDto = objectMapper.readValue(responseBody, KakaoUserInfoDto.class);

        } else {
            throw new UserException.UserNotFoundException();
        }

        return kakaoUserInfoDto;
    }

}
