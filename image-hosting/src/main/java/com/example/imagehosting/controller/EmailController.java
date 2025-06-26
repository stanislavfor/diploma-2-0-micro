package com.example.imagehosting.controller;

import com.example.imagehosting.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email-page")
    public String showForm() {
        return "email-page";
    }

    @PostMapping("sendEmail")
    public String sendEmail(@RequestParam("username") String username,
                            @RequestParam("email") String email,
                            @RequestParam("message") String message,
                            Model model) {
        try {
            emailService.saveEmail(username, email, message);
            model.addAttribute("message", "Сообщение email отправлено!");
        } catch (IOException e) {
            model.addAttribute("message", "Ошибка отправки сообщения email.");
            e.printStackTrace();
        }
        return "email-page";
    }
}
