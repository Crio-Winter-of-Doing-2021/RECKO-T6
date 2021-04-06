package com.lonewolf.recko.service;

import com.lonewolf.recko.entity.Admin;
import com.lonewolf.recko.entity.Moderator;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.request.ChangePassword;
import com.lonewolf.recko.repository.AdminRepository;
import com.lonewolf.recko.repository.ModeratorRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final ModeratorRepository modRepository;

    @Autowired
    public AuthService(AdminRepository adminRepository, ModeratorRepository modRepository) {
        this.adminRepository = adminRepository;
        this.modRepository = modRepository;
    }

    @SuppressWarnings("unused")
    private static String encodePassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public boolean adminLogin(@NonNull Admin admin) {
        if (adminRepository.findByAdminNameAndPassword(
                admin.getAdminName(),
                admin.getPassword())
                .orElse(null) == null) {
            throw new ReckoException("incorrect username or password specified", HttpStatus.UNAUTHORIZED);
        }

        return true;
    }

    public boolean adminChangePassword(@NonNull ChangePassword adminPass) {
        Admin admin = adminRepository.findById(adminPass.getUsername()).orElse(null);
        if (admin == null) {
            throw new ReckoException("incorrect username specified", HttpStatus.UNAUTHORIZED);
        }

        if (!admin.getPassword().equals(adminPass.getOldPassword())) {
            throw new ReckoException("incorrect password specified", HttpStatus.UNAUTHORIZED);
        }

        if (adminPass.getOldPassword().equals(adminPass.getNewPassword())) {
            throw new ReckoException("new password cannot be same as old password", HttpStatus.BAD_REQUEST);
        }

        admin.setPassword(adminPass.getNewPassword());
        adminRepository.saveAndFlush(admin);

        return true;
    }

    public String adminForgotPassword(@NonNull String adminName) {
        Admin admin = adminRepository.findById(adminName).orElse(null);
        if (admin == null) {
            throw new ReckoException("incorrect username specified", HttpStatus.UNAUTHORIZED);
        }
        return admin.getPassword();
    }

    public boolean moderatorLogin(@NonNull Moderator moderator) {
        if (modRepository.findByModeratorNameAndPassword(
                moderator.getModeratorName(),
                moderator.getPassword())
                .orElse(null) == null) {
            throw new ReckoException("incorrect username or password specified", HttpStatus.UNAUTHORIZED);
        }
        return true;
    }

    public boolean moderatorChangePassword(@NonNull ChangePassword modPassword) {
        Moderator moderator = modRepository.findById(modPassword.getUsername()).orElse(null);

        if (moderator == null) {
            throw new ReckoException("incorrect username specified", HttpStatus.UNAUTHORIZED);
        }

        if (!moderator.getPassword().equals(modPassword.getOldPassword())) {
            throw new ReckoException("incorrect password specified", HttpStatus.UNAUTHORIZED);
        }

        if (modPassword.getOldPassword().equals(modPassword.getNewPassword())) {
            throw new ReckoException("new password cannot be same as old password", HttpStatus.BAD_REQUEST);
        }

        moderator.setPassword(modPassword.getNewPassword());
        modRepository.saveAndFlush(moderator);

        return true;
    }

    public String moderatorForgotPassword(@NonNull String moderatorName) {
        Moderator moderator = modRepository.findById(moderatorName).orElse(null);
        if (moderator == null) {
            throw new ReckoException("incorrect username specified", HttpStatus.UNAUTHORIZED);
        }
        return moderator.getPassword();
    }
}