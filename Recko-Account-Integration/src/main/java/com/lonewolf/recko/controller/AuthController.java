package com.lonewolf.recko.controller;

import com.lonewolf.recko.entity.Admin;
import com.lonewolf.recko.entity.Moderator;
import com.lonewolf.recko.model.request.ChangePassword;
import com.lonewolf.recko.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Admin controlAdminLogin(@RequestBody Admin admin) {
        return service.adminLogin(admin);
    }

    @PatchMapping("/adminChangePassword")
    public Admin controlAdminChangePassword(@RequestBody ChangePassword adminPass) {
        return service.adminChangePassword(adminPass);
    }

    @PostMapping("/moderatorLogin")
    public Moderator controlModeratorLogin(@RequestBody Moderator moderator) {
        return service.moderatorLogin(moderator);
    }

    @PatchMapping("/moderatorChangePassword")
    public Moderator controlModeratorChangePassword(@RequestBody ChangePassword modPass) {
        return service.moderatorChangePassword(modPass);
    }
}
