package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Control.DetectionSource;
import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Repository.DetectionSourceRepository;

@Service
public class DeviceManageService {
    private final DetectionSourceRepository devicesRepository;

    @Autowired
    public DeviceManageService(
            DetectionSourceRepository devicesRepository) {
        this.devicesRepository = devicesRepository;
    }

    public DetectionSource getDeviceByConnection(String connectId) {
        return devicesRepository.findSourceById(connectId);
    }

    public boolean saveSource(DetectionSource sourceInfo) {
        DetectionSource temp = devicesRepository.save(sourceInfo);
        return temp != null && temp.getId() != null;
    }

    public void updateDevice(DetectionSource coDevices) {
        devicesRepository.updateSourceById(
                coDevices.getSourcePlace(),
                coDevices.getUsername(),
                coDevices.getPassword(),
                coDevices.getId());
    }

    public boolean deleteSource(String deviceIp) {
        return devicesRepository.deleteSource(deviceIp) > 0;
    }

    public List<DetectionSource> getAll() {
        return devicesRepository.findAll();
    }
}
