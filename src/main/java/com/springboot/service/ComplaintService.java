package com.springboot.service;

import com.springboot.model.Complaint;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ComplaintService {
    Complaint registerComplaint(Complaint complaint, MultipartFile photo, String photoUrl) throws IOException;
    List<Complaint> getAllComplaints();
	Complaint updateComplaintStatus(Long id, Complaint complaint);
	Complaint getComplaintById(Long id);
}
