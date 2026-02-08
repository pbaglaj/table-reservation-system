package com.example.reservation.controller;

import com.example.reservation.repository.TableRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TableController {

    private final TableRepository tableRepository;

    // constructor injection
    public TableController(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @GetMapping("/tables")
    public String showTables(Model model) {
        model.addAttribute("tablesList", tableRepository.findAll());
        return "tables-view";
    }
}
