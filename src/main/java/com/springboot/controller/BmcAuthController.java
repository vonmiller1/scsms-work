package com.springboot.controller;

import com.springboot.model.BmcUser;
import com.springboot.model.User;
import com.springboot.service.BmcUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bmc")
@CrossOrigin(origins = {"http://localhost:3000", "https://scsms.vercel.app"}, allowCredentials = "true")
public class BmcAuthController {

    @Autowired
    private BmcUserService bmcUserService;

    @PostMapping("/signup")
    public BmcUser signup(@RequestBody BmcUser user) {
        return bmcUserService.register(user);
    }

    @PostMapping("/login")
    public BmcUser login(@RequestBody BmcUser user, HttpSession session) {
        BmcUser loggedIn = bmcUserService.login(user.getEmail(), user.getPassword());
        if (loggedIn != null) {
            session.setAttribute("bmc_user", loggedIn); // Save in session
        }
        return loggedIn;
    }

    @GetMapping("/session-bmc")
//    public ResponseEntity<?> getBmcSessionUser(HttpSession session) {
//        Object bmcUser = session.getAttribute("bmc_user");
//        if (bmcUser != null) {
//            return ResponseEntity.ok(bmcUser);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No BMC user session found");
//        }
//    }
    public BmcUser getBmcSessionUser(HttpSession session) {
        return (BmcUser) session.getAttribute("bmc_user"); // Retrieve session bmc_user
    }
}
