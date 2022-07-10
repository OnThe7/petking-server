package com.onthe7.petking.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Response {

    private int status;
    private String requestId;
    private Object data;

    /**
     * {
     *     "status": 200
     * }
     */
    public static Response success() {
        return Response.builder().status(200).build();
    }

    /**
     * {
     *     "status": 200,
     *     "requestId": "e5a4e8c5-f115-4a45-a3bb-17d09dbc877f"
     * }
     */
    public static Response success(String requestId) {
        return Response.builder().status(200).requestId(requestId).build();
    }

    /**
     * {
     *     "status": 200,
     *     "requestId": "e5a4e8c5-f115-4a45-a3bb-17d09dbc877f"
     *     "data": {
     *         "result": true
     *     }
     * }
     */
    public static Response success(String requestId, Object data) {
        return Response.builder().status(200).requestId(requestId).data(data).build();
    }

    /**
     * {
     *     "status": 404,
     *     "data": {
     *         "status": 404,
     *         "error": "NOT_FOUND",
     *         "code": "USER_NOT_FOUND",
     *         "message": "존재하지 않는 사용자입니다."
     *     }
     * }
     */
    public static Response error(ErrorResponse errorResponse) {
        return Response.builder()
                .status(errorResponse.getStatus())
                .data(errorResponse)
                .build();
    }

    /**
     * {
     *     "status": 404,
     *     "requestId": "e5a4e8c5-f115-4a45-a3bb-17d09dbc877f",
     *     "data": {
     *         "status": 404,
     *         "error": "NOT_FOUND",
     *         "code": "USER_NOT_FOUND",
     *         "message": "존재하지 않는 사용자입니다."
     *     }
     * }
     */
    public static Response error(String requestId, ErrorResponse errorResponse) {
        return Response.builder()
                .status(errorResponse.getStatus())
                .requestId(requestId)
                .data(errorResponse)
                .build();
    }

    /**
     * {
     *     "status": 404,
     *     "data": {
     *         ~~~~~~~
     *     }
     * }
     */
    public static Response error(int status, Object data) {
        return Response.builder().status(status).data(data).build();
    }

    /**
     * {
     *     "status": 404,
     *     "requestId": "e5a4e8c5-f115-4a45-a3bb-17d09dbc877f",
     *     "data": {
     *         ~~~~~~~
     *     }
     * }
     */
    public static Response error(int status, String requestId, Object data) {
        return Response.builder().status(status).requestId(requestId).data(data).build();
    }

}
