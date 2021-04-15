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

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final ModeratorRepository modRepository;

    @Autowired
    public AuthService(AdminRepository adminRepository, ModeratorRepository modRepository) {
        this.adminRepository = adminRepository;
        this.modRepository = modRepository;
    }

    public Admin adminLogin(@NonNull Admin admin) {
        Admin existingAdmin = adminRepository.findByAdminNameAndPassword(admin.getAdminName(), admin.getPassword()).orElse(null);
        if (existingAdmin == null) {
            throw new ReckoException("incorrect username or password specified", HttpStatus.UNAUTHORIZED);
        }
        return existingAdmin;
    }

    public Admin adminChangePassword(@NonNull ChangePassword adminPass) {
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
        return adminRepository.saveAndFlush(admin);
    }

    public Moderator moderatorLogin(@NonNull Moderator mod) {
        Moderator existingModerator = modRepository.findByModeratorNameAndPassword(mod.getModeratorName(), mod.getPassword()).orElse(null);
        if (existingModerator == null) {
            throw new ReckoException("incorrect username or password specified", HttpStatus.UNAUTHORIZED);
        }
        return existingModerator;
    }

    public Moderator moderatorChangePassword(@NonNull ChangePassword modPassword) {
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
        return modRepository.saveAndFlush(moderator);
    }
}