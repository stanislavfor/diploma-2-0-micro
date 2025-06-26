package com.example.auth.controller;

import com.example.auth.service.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final KeycloakAdminService adminService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model m) {
        if (error != null) m.addAttribute("param.error", true);
        return "login";
    }

    @GetMapping("/registration")
    public String regPage(@RequestParam(required = false) String error, Model m) {
        if (error != null) m.addAttribute("param.error", true);
        return "registration";
    }

    @PostMapping("/registration")
    public String register(@RequestParam String login, @RequestParam String password) {
        boolean ok = adminService.createUser(login, password);
        return ok ? "redirect:/login" : "redirect:/registration?error";
    }

    @GetMapping("/afterLogin")
    public String afterLogin() {
        return "redirect:" + "/"; // использовать значение app.redirect-url
    }

    @RequestMapping("/oops")
    public String oops() {
        return "oops";
    }

    @GetMapping("/email-page")
    public String showEmailPage() {
        return "email-page"; // шаблон в templates/email-page.html
    }

}
