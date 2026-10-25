package com.hotel.controller;

import com.hotel.entity.Room;
import com.hotel.entity.RoomStatus;
import com.hotel.entity.RoomType;
import com.hotel.service.RoomService;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public String list(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "rooms";
    }

    @PostMapping("/rooms/save")
    public String save(@RequestParam(required = false) Long id,
                       @RequestParam String roomNo,
                       @RequestParam("type") RoomType type,
                       @RequestParam BigDecimal price,
                       @RequestParam Integer capacity,
                       @RequestParam(required = false) RoomStatus status,
                       RedirectAttributes flash) {
        Room saved = roomService.save(id, roomNo, type, price, capacity, status);
        flash.addFlashAttribute("success", "Room " + saved.getRoomNo() + " saved.");
        return "redirect:/rooms";
    }

    @PostMapping("/rooms/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        roomService.delete(id);
        flash.addFlashAttribute("success", "Room deleted.");
        return "redirect:/rooms";
    }
}
