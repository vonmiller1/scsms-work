package com.springboot.controller;

import com.springboot.model.BmcUser;
import com.springboot.service.BmcUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
            session.setAttribute("bmc_user", loggedIn);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loggedIn, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        return loggedIn;
    }

    @GetMapping("/session-bmc")
    public BmcUser getBmcSessionUser(HttpSession session) {
        return (BmcUser) session.getAttribute("bmc_user");
    }
}
