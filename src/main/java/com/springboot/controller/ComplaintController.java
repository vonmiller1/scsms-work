package com.springboot.controller;

import com.springboot.model.Complaint;
import com.springboot.service.ComplaintService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = {"http://localhost:3000", "https://scsms.vercel.app"}, allowCredentials = "true")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping
    public Complaint reportIssue(@RequestParam("description") String description,
                                 @RequestParam("area") String area,
                                 @RequestParam("latitude") double latitude,
                                 @RequestParam("longitude") double longitude,
                                 @RequestParam("status") String status,
                                 @RequestParam(value = "photo", required = false) MultipartFile photo,
                                 @RequestParam(value = "photoUrl", required = false) String photoUrl) throws IOException {

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (area == null || area.isBlank()) {
            throw new IllegalArgumentException("Area is required");
        }

        Complaint complaint = new Complaint();
        complaint.setDescription(description.trim());
        complaint.setArea(area.trim());
        complaint.setLatitude(latitude);
        complaint.setLongitude(longitude);
        complaint.setStatus(status);

        return complaintService.registerComplaint(complaint, photo, photoUrl);
    }

    @GetMapping
    public List<Complaint> getAllComplaints(HttpSession session) {
        if (session.getAttribute("user") == null && session.getAttribute("bmc_user") == null) {
            throw new RuntimeException("User not logged in or session expired.");
        }

        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public Complaint getComplaint(@PathVariable Long id) {
        return complaintService.getComplaintById(id);
    }

    @PutMapping("/{id}")
    public Complaint updateComplaintStatus(@PathVariable Long id, @RequestBody Complaint complaint) {
        return complaintService.updateComplaintStatus(id, complaint);
    }

}
