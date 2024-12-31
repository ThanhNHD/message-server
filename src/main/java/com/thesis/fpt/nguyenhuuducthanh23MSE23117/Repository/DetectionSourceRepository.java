package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control.DetectionSource;

import jakarta.transaction.Transactional;

@Repository
public interface DetectionSourceRepository extends JpaRepository<DetectionSource, String> {
    @Query(value = "select * from device where device_connection_ip = :sourceId", nativeQuery = true)
    DetectionSource findSourceById(@Param("sourceId") String sourceId);

    @Transactional
    @Modifying
    @Query(value = "update device set device_connection_ip = :sourceId, username =:username, password = :password where id =:id",nativeQuery = true)
    void updateSourceById(@Param("sourceId") String sourceId,@Param("username") String username,@Param("password") String password,@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM device WHERE device_connection_ip = :sourceId",nativeQuery = true)
    int deleteSource(@Param("sourceId") String sourceId);
}
