package com.example.reservation.controller;

import com.example.reservation.model.RestaurantTable;
import com.example.reservation.repository.TableRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    private final TableRepository tableRepository;

    public AdminController(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("newTable", new RestaurantTable());
        return "admin/dashboard";
    }

    @PostMapping("/admin/add-table")
    public String addTable(@ModelAttribute RestaurantTable newTable) {
        newTable.setActive(true);
        tableRepository.save(newTable);
        return "redirect:/admin/dashboard?success";
    }
}
