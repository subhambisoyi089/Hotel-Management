package com.hotel.controller;

import com.hotel.entity.Guest;
import com.hotel.service.GuestService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/guests")
    public String list(@RequestParam(required = false, defaultValue = "") String q, Model model) {
        List<Guest> guests = guestService.search(q);
        model.addAttribute("guests", guests);
        model.addAttribute("stays", guestService.stayCounts(guests));
        model.addAttribute("q", q.trim());
        return "guests";
    }

    @PostMapping("/guests/save")
    public String save(@RequestParam(required = false) Long id,
                       @RequestParam String name,
                       @RequestParam String phone,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String idProof,
                       @RequestParam(required = false) String address,
                       RedirectAttributes flash) {
        Guest saved = guestService.save(id, name, phone, email, idProof, address);
        flash.addFlashAttribute("success", saved.getName() + " saved.");
        return "redirect:/guests";
    }

    @PostMapping("/guests/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        guestService.delete(id);
        flash.addFlashAttribute("success", "Guest deleted.");
        return "redirect:/guests";
    }
}
