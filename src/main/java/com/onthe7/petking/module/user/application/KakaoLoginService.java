package com.onthe7.petking.module.user.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.onthe7.petking.common.enums.GrantType;
import com.onthe7.petking.common.exception.JwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class ResponseDto {
        private String tokenType;

        private String accessToken;

        private Long expiresIn;

        private String idToken;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String scope;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String refreshToken;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Long refreshTokenExpiresIn;
    }

    @Value("${kakao.api-key}")
    private String REST_API_KEY;

    @Value("${kakao.endpoint}")
    private String ENDPOINT;

    @Value("${kakao.callback-uri}")
    private String CALLBACK_URI;

    // https://kauth.kakao.com/oauth/token
    public ResponseDto getAccessToken(String code) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", GrantType.AUTH_CODE.getValue())
                .add("client_id", REST_API_KEY)
                .add("redirect_uri", CALLBACK_URI)
                .add("code", code)
                .build();

        Request request = new Request.Builder()
                .url(ENDPOINT + "/oauth/token")
                .post(formBody)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        ResponseDto responseDto = null;

        okhttp3.Response httpResponse = client.newCall(request).execute();

        if (httpResponse.isSuccessful() == true) {
            String responseBody = httpResponse.body().string();
            log.debug("[Response Body] " + responseBody);
            responseDto = objectMapper.readValue(responseBody, ResponseDto.class);

        } else {
            throw new JwtException.JwtCreatedFailedException();
        }

        return responseDto;
    }

    // https://kauth.kakao.com/oauth/token
    public ResponseDto refreshAccessToken(String refreshToken) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", GrantType.REFRESH_TOKEN.getValue())
                .add("client_id", REST_API_KEY)
                .add("refresh_token", refreshToken)
                .build();

        Request request = new Request.Builder()
                .url(ENDPOINT + "/oauth/token")
                .post(formBody)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        ResponseDto responseDto = null;

        okhttp3.Response httpResponse = client.newCall(request).execute();

        if (httpResponse.isSuccessful() == true) {
            String responseBody = httpResponse.body().string();
            log.debug("[Response Body] " + responseBody);
            responseDto = objectMapper.readValue(responseBody, ResponseDto.class);

        } else {
            throw new JwtException.JwtCreatedFailedException();
        }

        return responseDto;
    }

}
