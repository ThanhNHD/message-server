package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message;

import lombok.Data;

@Data
public class NotificationRequest {
    private String title;
    private String body;
    private String topic;
    private String token;
}
