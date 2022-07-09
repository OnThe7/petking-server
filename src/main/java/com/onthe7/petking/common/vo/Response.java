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
    private Object data;

    public static Response success(){
        return Response.builder().status(200).build();
    }

    public static Response success(Object data) {
        return Response.builder().status(200).data(data).build();
    }

    public static Response error(ErrorResponse errorResponse) {
        return Response.builder()
                .status(errorResponse.getStatus())
                .data(errorResponse)
                .build();
    }

    public static Response error(int status, Object data) {
        return Response.builder().status(status).data(data).build();
    }
}
