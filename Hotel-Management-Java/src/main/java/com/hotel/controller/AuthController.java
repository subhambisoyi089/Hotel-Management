package com.hotel.controller;

import com.hotel.repository.UserRepository;
import com.hotel.web.AuthInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute(AuthInterceptor.USER_KEY) != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpServletRequest request, RedirectAttributes flash) {
        boolean ok = users.findByUsername(username.trim())
                .filter(u -> encoder.matches(password, u.getPasswordHash()))
                .isPresent();
        if (!ok) {
            flash.addFlashAttribute("error", "Wrong username or password.");
            return "redirect:/login";
        }
        request.changeSessionId(); // new session id after login
        request.getSession().setAttribute(AuthInterceptor.USER_KEY, username.trim());
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
