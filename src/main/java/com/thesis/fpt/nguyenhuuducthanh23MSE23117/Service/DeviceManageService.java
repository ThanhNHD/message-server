package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control.Devices;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Repository.DevicesRepository;

@Service
public class DeviceManageService {
    private final DevicesRepository devicesRepository;

    @Autowired
    public DeviceManageService(
            DevicesRepository devicesRepository) {
        this.devicesRepository = devicesRepository;
    }

    public Devices getDeviceByConnection(String connectId) {
        return devicesRepository.findDeviceByConnectionIp(connectId);
    }

    public boolean saveDevice(Devices coDevices) {
        Devices temp = devicesRepository.save(coDevices);
        return temp != null && temp.getId() != null;
    }

    public void updateDevice(Devices coDevices) {
        devicesRepository.updateDeviceById(
                coDevices.getDeviceConnection(),
                coDevices.getUsername(),
                coDevices.getPassword(),
                coDevices.getId());
    }

    public boolean deleteDevice(String deviceIp) {
        return devicesRepository.deleteDeviceByIp(deviceIp) > 0;
    }

    public List<Devices> getAll() {
        return devicesRepository.findAll();
    }
}
