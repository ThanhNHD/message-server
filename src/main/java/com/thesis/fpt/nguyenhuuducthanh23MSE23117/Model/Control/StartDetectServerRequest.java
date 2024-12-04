package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class StartDetectServerRequest {

    private List<String> rtsp_urls=new ArrayList<>();
    private String server_url;
}
