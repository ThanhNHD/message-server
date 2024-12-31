package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "device")
@Data
public class DetectionSource {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "device_connection_ip")
    private String sourcePlace;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
}
