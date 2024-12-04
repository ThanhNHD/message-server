package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {
    private String code;
    private String message;
}
