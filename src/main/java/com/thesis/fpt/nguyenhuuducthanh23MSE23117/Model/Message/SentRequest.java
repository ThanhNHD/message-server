package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sent_request")
public class SentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;
    
    @Column(name = "original_request_id")
    private Long originalRequestId; // ID of the original incoming request

    @Column(name = "sent_time_stamp")
    private LocalDateTime sentTimestamp = LocalDateTime.now();
}