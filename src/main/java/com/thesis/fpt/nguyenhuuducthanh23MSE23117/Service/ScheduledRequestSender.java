package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.DiscordModel;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.FcmTokenToSendNoti;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.IncomingRequest;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.NotificationRequest;

@RestController
@RequestMapping("/api/notify")
@Component
public class ScheduledRequestSender {
    Logger logger = LoggerFactory.getLogger(ScheduledRequestSender.class);
    private final RequestService requestService;
    private final FCMService fcmService;
    private List<String> fcmTokens = new ArrayList<>();

    @PostMapping("/resign")
    public ResponseEntity<IncomingRequest> saveNewRequest(@RequestBody FcmTokenToSendNoti request) {
        if (!fcmTokens.contains(request.getToken()))
            fcmTokens.add(request.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public ScheduledRequestSender(
            RequestService requestService,
            @Value("${discord.server.url}") String discordUrl,
            @Value("${server.discord.message}") String discordMessage,
            FCMService fcmService) {
        this.requestService = requestService;
        this.discordUrl = discordUrl;
        this.discordMessage = discordMessage;
        this.fcmService = fcmService;
    }

    private final String discordMessage;
    private final String discordUrl;
    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .defaultHeader("Content-Type", "application/json").build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRateString = "${message.sender.check}") // Every 30 seconds
    public void sendUnsentRequest() throws RestClientException, JsonProcessingException {
        List<IncomingRequest> unsentRequest = requestService.getUnsentRequest();
        if (!unsentRequest.isEmpty()) {
            for (IncomingRequest incomingRequest : unsentRequest) {
                if (requestService.getRequestPassLimit(incomingRequest.getDeviceId())) {
                    DiscordModel discordModel = new DiscordModel();
                    discordModel.setContent(discordMessage + incomingRequest.getDeviceId());
                    requestService.markRequestAsSent(incomingRequest);
                    // Simulate sending the request
                    restTemplate.postForLocation(discordUrl, objectMapper.writeValueAsString(discordModel));
                    NotificationRequest notificationRequest = new NotificationRequest();
                    notificationRequest.setBody(discordModel.getContent());
                    notificationRequest.setTitle("Crime detected");
                    notificationRequest.setTopic("alert");
                    Iterator<String> iterator = fcmTokens.iterator();
                    while (iterator.hasNext()) {
                        String fcmtoken = iterator.next();
                        NotificationRequest tempNotiReq = notificationRequest;
                        tempNotiReq.setToken(fcmtoken);
                        try {
                            fcmService.sendMessageToToken(tempNotiReq);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            iterator.remove();
                        }
                    }
                    logger.info("Sented message");
                }
                requestService.deleteAllNotSended(incomingRequest.getDeviceId());
            }
        } else {
            logger.info("No unsent requests available.");
        }
    }
}