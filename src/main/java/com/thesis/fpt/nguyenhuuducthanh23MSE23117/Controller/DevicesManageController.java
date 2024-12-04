package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control.AddDeviceReq;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control.DeleteDeviceServices;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control.Devices;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control.GetAllDevicesIp;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control.StartDetectServerRequest;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.CommonResponse;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Service.DeviceManageService;

@RestController
@RequestMapping("/api/control")
public class DevicesManageController {
    private final DeviceManageService deviceManageService;
    private final String serverAddress;
    private final String detectServerStartAddress;
    private final String detectServerStopAddress;

    Logger logger = LoggerFactory.getLogger(DevicesManageController.class);
    @Value("${server.password}")
    private String serverPassword;

    @Autowired
    public DevicesManageController(
            DeviceManageService deviceManageService,
            @Value("${server.port}") String serverPort,
            @Value("${detectionserver.start.address}") String detectServerStartAddress,
            @Value("${detectionserver.stop.address}") String detectServerStopAddress,
            RestTemplateBuilder builder) throws UnknownHostException {
        this.deviceManageService = deviceManageService;
        String http = "http://";
        this.serverAddress = http + InetAddress.getLocalHost().getHostAddress() + ":" + serverPort;
        this.detectServerStartAddress = http + InetAddress.getLocalHost().getHostAddress() + ":"
                + detectServerStartAddress;
        this.detectServerStopAddress = http + InetAddress.getLocalHost().getHostAddress() + ":"
                + detectServerStopAddress;
        this.restTemplate = builder
                .defaultHeader("Content-Type", "application/json").build();
    }

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/all")
    public ResponseEntity<List<Devices>> getAllDevice(@RequestBody GetAllDevicesIp request) {
        List<Devices> devices = new ArrayList<>();
        try {
            if (request.getPassword().equals(serverPassword))
                devices = deviceManageService.getAll();
            else
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @PostMapping("/startServies")
    public ResponseEntity<String> startServices() throws JsonProcessingException {
        String returnString = "";
        try {
            List<Devices> devices = deviceManageService.getAll();
            StartDetectServerRequest startDetectServerRequest = new StartDetectServerRequest();
            for (Devices devices2 : devices) {
                startDetectServerRequest.getRtsp_urls().add(devices2.getDeviceConnection());
            }
            startDetectServerRequest.setServer_url(serverAddress + "/api/requests/save");
            returnString = restTemplate.postForEntity(detectServerStartAddress, startDetectServerRequest, String.class)
                    .getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setMessage(e.getMessage());
            returnString = objectMapper.writeValueAsString(commonResponse);
        }
        return new ResponseEntity<>(returnString, HttpStatus.OK);
    }

    @PostMapping("/stopServies")
    public ResponseEntity<String> stopServices() throws JsonProcessingException {
        String returnString = "";
        try {
            logger.info(detectServerStopAddress);
            returnString = restTemplate.postForEntity(detectServerStopAddress, "", String.class)
                    .getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setMessage(e.getMessage());
            returnString = objectMapper.writeValueAsString(commonResponse);
        }
        return new ResponseEntity<>(returnString, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse> saveDevice(@RequestBody AddDeviceReq request) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            if (request.getPassword().equals(serverPassword)) {
                if (deviceManageService.saveDevice(request.getData())) {
                    commonResponse.setCode("200");
                    commonResponse.setMessage("success");
                    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
                } else {
                    commonResponse.setCode("400");
                    commonResponse.setMessage("can't delete your id");
                    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
                }
            } else {
                commonResponse.setCode("401");
                commonResponse.setMessage("incorrect server password");
                return new ResponseEntity<>(commonResponse, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            commonResponse.setCode("500");
            commonResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(commonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<CommonResponse> updateDevice(@RequestBody Devices request) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            deviceManageService.updateDevice(request);
            commonResponse.setCode("200");
            commonResponse.setMessage("success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            commonResponse.setCode("500");
            commonResponse.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<CommonResponse> deleteDevice(@RequestBody DeleteDeviceServices request) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            if (request.getPassword().equals(serverPassword)) {
                if (deviceManageService.deleteDevice(request.getDeviceIp())) {
                    commonResponse.setCode("200");
                    commonResponse.setMessage("success");
                    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
                } else {
                    commonResponse.setCode("400");
                    commonResponse.setMessage("can't delete your id");
                    return new ResponseEntity<>(commonResponse, HttpStatus.BAD_REQUEST);
                }
            } else {
                commonResponse.setCode("401");
                commonResponse.setMessage("incorrect server password");
                return new ResponseEntity<>(commonResponse, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            commonResponse.setCode("500");
            commonResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(commonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
