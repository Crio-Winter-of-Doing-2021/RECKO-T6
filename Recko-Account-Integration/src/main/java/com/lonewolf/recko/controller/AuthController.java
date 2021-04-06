package com.lonewolf.recko.controller;

import com.lonewolf.recko.entity.Admin;
import com.lonewolf.recko.entity.Moderator;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.request.ChangePassword;
import com.lonewolf.recko.service.AuthService;
import com.lonewolf.recko.util.ResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/adminLogin")
    public ResponseEntity<Map<String, String>> controlAdminLogin(@RequestBody Admin admin) {
        boolean isLoggedIn = service.adminLogin(admin);
        if (!isLoggedIn) {
            throw new ReckoException("admin couldn't log in", HttpStatus.UNAUTHORIZED);
        }
        return ResponseGenerator.generateResponse("logged in successfully", HttpStatus.OK, false);
    }

    @PatchMapping("/adminChangePassword")
    public ResponseEntity<Map<String, String>> controlAdminChangePassword(@RequestBody ChangePassword adminPass) {
        boolean isChanged = service.adminChangePassword(adminPass);
        if (!isChanged) {
            throw new ReckoException("password couldn't be changed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseGenerator.generateResponse("password changed successfully", HttpStatus.OK, false);
    }

    @GetMapping("/adminForgotPassword/{adminName}")
    public ResponseEntity<Map<String, String>> controlAdminChangePassword(@PathVariable("adminName") String adminName) {
        String password = service.adminForgotPassword(adminName);
        return ResponseGenerator.generateResponse(password, HttpStatus.OK, true);
    }

    @PostMapping("/moderatorLogin")
    public ResponseEntity<Map<String, String>> controlModeratorLogin(@RequestBody Moderator moderator) {
        boolean isLoggedIn = service.moderatorLogin(moderator);
        if (!isLoggedIn) {
            throw new ReckoException("moderator couldn't log in", HttpStatus.UNAUTHORIZED);
        }
        return ResponseGenerator.generateResponse("logged in successfully", HttpStatus.OK, false);
    }

    @PatchMapping("/moderatorChangePassword")
    public ResponseEntity<Map<String, String>> controlModeratorChangePassword(@RequestBody ChangePassword modPass) {
        boolean isChanged = service.moderatorChangePassword(modPass);
        if (!isChanged) {
            throw new ReckoException("password couldn't be changed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseGenerator.generateResponse("password changed successfully", HttpStatus.OK, false);
    }

    @GetMapping("/moderatorForgotPassword/{modName}")
    public ResponseEntity<Map<String, String>> controlModeratorChangePassword(@PathVariable("modName") String modName) {
        String password = service.moderatorForgotPassword(modName);
        return ResponseGenerator.generateResponse(password, HttpStatus.OK, true);
    }
}
