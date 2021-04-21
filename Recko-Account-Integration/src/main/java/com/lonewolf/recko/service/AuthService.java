package com.lonewolf.recko.service;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.Admin;
import com.lonewolf.recko.entity.Moderator;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.request.ChangePassword;
import com.lonewolf.recko.model.request.CompanyHandler;
import com.lonewolf.recko.repository.AdminRepository;
import com.lonewolf.recko.repository.ModeratorRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final ModeratorRepository modRepository;
    private final JavaMailSender mailSender;
    private final String senderEmail;

    @Autowired
    public AuthService(AdminRepository adminRepository,
                       ModeratorRepository modRepository,
                       JavaMailSender mailSender,
                       @Qualifier(BeanNameRepository.Mail_Sender_Email_Address) String senderEmail) {
        this.adminRepository = adminRepository;
        this.modRepository = modRepository;
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
    }

    public Admin adminLogin(@NonNull Admin admin) {
        Admin existingAdmin = adminRepository.findByAdminNameAndPassword(
                admin.getAdminName().toLowerCase(),
                admin.getPassword())
                .orElse(null);

        if (existingAdmin == null) {
            throw new ReckoException("incorrect username or password specified", HttpStatus.UNAUTHORIZED);
        }
        return existingAdmin;
    }

    public Admin adminChangePassword(@NonNull ChangePassword adminPass) {
        Admin admin = adminRepository.findByAdminNameIgnoreCase(adminPass.getUsername()).orElse(null);
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

    public void adminForgotPassword(@NonNull CompanyHandler companyHandler) {
        Admin existingAdmin = adminRepository.findByAdminNameAndEmailIgnoreCase(
                companyHandler.getUsername(),
                companyHandler.getEmail())
                .orElse(null);

        if (existingAdmin == null) {
            throw new ReckoException("incorrect admin credentials specified", HttpStatus.UNAUTHORIZED);
        }

        String subject = "Your Recko Dashboard Password";
        String message = "Your Password is " + existingAdmin.getPassword();

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(senderEmail);
        mailMessage.setTo(existingAdmin.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public Moderator moderatorLogin(@NonNull Moderator mod) {
        Moderator existingModerator = modRepository.findByModeratorNameAndPassword(
                mod.getModeratorName().toLowerCase(),
                mod.getPassword())
                .orElse(null);

        if (existingModerator == null) {
            throw new ReckoException("incorrect username or password specified", HttpStatus.UNAUTHORIZED);
        }
        return existingModerator;
    }

    public Moderator moderatorChangePassword(@NonNull ChangePassword modPassword) {
        Moderator moderator = modRepository.findByModeratorNameIgnoreCase(modPassword.getUsername()).orElse(null);

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

    public void moderatorForgotPassword(@NonNull CompanyHandler companyHandler) {
        Moderator existingModerator = modRepository.findByModeratorNameAndEmailIgnoreCase(
                companyHandler.getUsername(),
                companyHandler.getEmail())
                .orElse(null);

        if (existingModerator == null) {
            throw new ReckoException("incorrect moderator credentials specified", HttpStatus.UNAUTHORIZED);
        }

        String subject = "Your Recko Dashboard Password";
        String message = "Your Password is " + existingModerator.getPassword();

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(senderEmail);
        mailMessage.setTo(existingModerator.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}