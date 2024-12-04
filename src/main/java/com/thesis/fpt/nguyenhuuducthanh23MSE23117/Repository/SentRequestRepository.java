package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thesis.fpt.nguyenhuuducthanh23MSE23117.Model.Message.SentRequest;

@Repository
public interface SentRequestRepository extends JpaRepository<SentRequest, Long> { }
