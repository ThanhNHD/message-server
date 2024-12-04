package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Repository.IncomingRequestRepository;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Repository.SentRequestRepository;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.IncomingRequest;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.SentRequest;

@Service
public class RequestService {

    private final IncomingRequestRepository incomingRequestRepository;

    private final SentRequestRepository sentRequestRepository;

    private final int timeBetweenSend;

    @Autowired
    public RequestService(
            IncomingRequestRepository incomingRequestRepository,
            SentRequestRepository sentRequestRepository,
            @Value("${server.time.between.send}") int timeBetweenSend) {
        this.incomingRequestRepository = incomingRequestRepository;
        this.sentRequestRepository = sentRequestRepository;
        this.timeBetweenSend = timeBetweenSend;
    }

    public IncomingRequest saveNewRequest(IncomingRequest request) {
        return incomingRequestRepository.save(request);
    }

    public List<IncomingRequest> getUnsentRequest() {
        return incomingRequestRepository.findBySentFalse();
    }

    public boolean getRequestPassLimit(String deviceId) {
        LocalDateTime pastSentTime = incomingRequestRepository.findSendedByDeviceId(deviceId);
        return pastSentTime == null ? true
                : Duration.between(incomingRequestRepository.findSendedByDeviceId(deviceId), LocalDateTime.now())
                        .getSeconds() > timeBetweenSend;
    }

    @Transactional
    public void markRequestAsSent(IncomingRequest request) {
        request.setSent(true);
        incomingRequestRepository.save(request);
        // Save to SentRequest table
        SentRequest sentRequest = new SentRequest();
        sentRequest.setContent(request.getContent());
        sentRequest.setOriginalRequestId(request.getId());
        sentRequestRepository.save(sentRequest);
    }

    @Transactional
    public void deleteAllNotSended(String deviceId) {
        incomingRequestRepository.deleteAllNotSendThings(deviceId);
    }

    public List<SentRequest> getAllSentRequests() {
        return sentRequestRepository.findAll();
    }
}