package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control.Devices;

import jakarta.transaction.Transactional;

@Repository
public interface DevicesRepository extends JpaRepository<Devices, String> {
    @Query(value = "select * from device where device_connection_ip = :deviceIp", nativeQuery = true)
    Devices findDeviceByConnectionIp(@Param("deviceIp") String deviceIp);

    @Transactional
    @Modifying
    @Query(value = "update device set device_connection_ip = :deviceIp, username =:username, password = :password where id =:id",nativeQuery = true)
    void updateDeviceById(@Param("deviceIp") String deviceIp,@Param("username") String username,@Param("password") String password,@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM device WHERE device_connection_ip = :deviceIp",nativeQuery = true)
    int deleteDeviceByIp(@Param("deviceIp") String deviceIp);
}
