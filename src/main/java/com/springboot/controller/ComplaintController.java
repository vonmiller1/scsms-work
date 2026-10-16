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

        Complaint complaint = new Complaint();
        complaint.setDescription(description);
        complaint.setArea(area);
        complaint.setLatitude(latitude);
        complaint.setLongitude(longitude);
        complaint.setStatus(status);

        return complaintService.registerComplaint(complaint, photo, photoUrl);
    }

    // Get all complaints
    @GetMapping
    public List<Complaint> getAllComplaints(HttpSession session) {
        Object user = session.getAttribute("user");
        System.out.println("Session user: " + user);

        if (user == null) {
            throw new RuntimeException("User not logged in or session expired.");
        }

        return complaintService.getAllComplaints();
    }


    // Get a specific complaint by ID
    @GetMapping("/{id}")
    public Complaint getComplaint(@PathVariable Long id) {
        return complaintService.getComplaintById(id);
    }

    // Update complaint status by ID
    @PutMapping("/{id}")
    public Complaint updateComplaintStatus(@PathVariable Long id, @RequestBody Complaint complaint) {
        return complaintService.updateComplaintStatus(id, complaint);
    }

}
