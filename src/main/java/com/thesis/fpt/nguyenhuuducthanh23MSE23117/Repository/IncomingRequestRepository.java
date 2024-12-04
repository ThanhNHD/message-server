package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.IncomingRequest;

import jakarta.transaction.Transactional;

@Repository
public interface IncomingRequestRepository extends JpaRepository<IncomingRequest, Long> {
    @Query(value = "SELECT \n" +
            "        t.id, t.content, t.sent, t.created_at, t.device_id\n" +
            "    FROM\n" +
            "        incoming_request t\n" +
            "    JOIN (SELECT \n" +
            "        MAX(id) AS id, device_id\n" +
            "    FROM\n" +
            "        incoming_request\n" +
            "    WHERE\n" +
            "        sent = 0\n" +
            "    GROUP BY device_id) s ON s.id = t.id", nativeQuery = true)
    List<IncomingRequest> findBySentFalse();

    @Query(value = "SELECT \n" + 
            "         t.created_at" + 
            "    FROM\n" + 
            "        incoming_request t\n" + 
            "    JOIN (SELECT \n" + 
            "        MAX(id) AS id, device_id\n" + 
            "    FROM\n" + 
            "        incoming_request\n" + 
            "    WHERE\n" + 
            "        sent = 1\n" + 
            "        and device_id = :deviceId\n" + 
            "    GROUP BY device_id) s ON s.id = t.id" +
            "", nativeQuery = true)
    LocalDateTime findSendedByDeviceId(@Param("deviceId") String deviceId);

    @Query(value = "delete from incoming_request where device_id = :deviceId and sent = 0", nativeQuery = true)
    @Transactional
    @Modifying
    void deleteAllNotSendThings(@Param("deviceId") String deviceId);
}