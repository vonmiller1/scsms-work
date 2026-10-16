package com.springboot.service;

import com.springboot.model.Complaint;
import com.springboot.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private static final long MAX_PHOTO_BYTES = 5 * 1024 * 1024;

    @Autowired
    private ComplaintRepository complaintRepository;

    private final Path uploadDir;

    public ComplaintServiceImpl() throws IOException {
        uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);
    }

    @Override
    public Complaint registerComplaint(Complaint complaint, MultipartFile photo, String photoUrl) throws IOException {
        if (photo != null && !photo.isEmpty()) {
            if (photo.getSize() > MAX_PHOTO_BYTES) {
                throw new IllegalArgumentException("Photo exceeds maximum upload size (5 MB)");
            }

            String extension = safeImageExtension(photo.getOriginalFilename());
            String fileName = "photo_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;
            Path target = uploadDir.resolve(fileName).normalize();
            if (!target.startsWith(uploadDir)) {
                throw new IllegalArgumentException("Invalid upload path");
            }

            Files.write(target, photo.getBytes());

            complaint.setPhotoPath("uploads/" + fileName);
            complaint.setPhotoUrl(null);
        } else if (photoUrl != null && !photoUrl.isBlank()) {
            complaint.setPhotoUrl(photoUrl.trim());
            complaint.setPhotoPath(null);
        }

        return complaintRepository.save(complaint);
    }

    private static String safeImageExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return ".jpg";
        }
        String ext = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        return switch (ext) {
            case ".jpg", ".jpeg", ".png", ".gif", ".webp" -> ext;
            default -> ".jpg";
        };
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    @Override
    public Complaint updateComplaintStatus(Long id, Complaint complaint) {
        Optional<Complaint> existingComplaint = complaintRepository.findById(id);
        if (existingComplaint.isPresent()) {
            Complaint updatedComplaint = existingComplaint.get();
            updatedComplaint.setStatus(complaint.getStatus());
            return complaintRepository.save(updatedComplaint);
        } else {
            throw new RuntimeException("Complaint not found");
        }
    }

    @Override
    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElseThrow(() -> new RuntimeException("Complaint not found"));
    }
}
