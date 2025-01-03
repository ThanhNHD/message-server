package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.NotificationRequest;

@Service
public class FCMService {
        Gson gson = new Gson();

        public void sendMessageToToken(NotificationRequest request)
                        throws InterruptedException, ExecutionException {
                Message message = getPreconfiguredMessageToToken(request);
                sendAndGetResponse(message);
        }

        private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
                return FirebaseMessaging.getInstance().sendAsync(message).get();
        }

        private AndroidConfig getAndroidConfig(String topic) {
                return AndroidConfig.builder()
                                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .setNotification(AndroidNotification.builder()
                                                .setTag(topic).build())
                                .build();
        }

        private ApnsConfig getApnsConfig(String topic) {
                return ApnsConfig.builder()
                                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
        }

        private Message getPreconfiguredMessageToToken(NotificationRequest request) {
                return getPreconfiguredMessageBuilder(request)
                                .setToken(request.getToken())
                                .build();
        }

        private Message.Builder getPreconfiguredMessageBuilder(NotificationRequest request) {
                AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
                ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
                Notification notification = Notification.builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getBody())
                                .build();
                return Message.builder()
                                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
                                .setNotification(notification);
        }
}