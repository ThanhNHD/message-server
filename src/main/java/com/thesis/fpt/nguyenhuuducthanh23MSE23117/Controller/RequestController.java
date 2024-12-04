package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Service.RequestService;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.IncomingRequest;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.SentRequest;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;
    Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    public RequestController(
            RequestService requestService) {
        this.requestService = requestService;
    }

    // Endpoint to save new incoming requests
    @PostMapping("/save")
    public ResponseEntity<IncomingRequest> saveNewRequest(@RequestBody IncomingRequest request) {
        IncomingRequest savedRequest = requestService.saveNewRequest(request);
        return new ResponseEntity<>(savedRequest, HttpStatus.OK);
    }

    // Endpoint to get all sent requests
    @GetMapping("/sent")
    public ResponseEntity<List<SentRequest>> getAllSentRequests() {
        List<SentRequest> sentRequests = requestService.getAllSentRequests();
        return new ResponseEntity<>(sentRequests, HttpStatus.OK);
    }
}