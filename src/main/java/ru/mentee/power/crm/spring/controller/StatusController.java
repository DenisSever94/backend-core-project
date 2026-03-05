package ru.mentee.power.crm.spring.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mentee.power.crm.domain.StatusLead;
import ru.mentee.power.crm.service.LeadStatusService;

@Controller
@AllArgsConstructor
public class StatusController {
  private final LeadStatusService service;

  @GetMapping("/leads/status")
  public String showCreateStatus(
      @RequestParam(required = false) StatusLead statusLead, Model model) {
    List<StatusLead> statuses = service.getAllStatuses();
    model.addAttribute("statusLead", statuses);
    return "leads/listStatus";
  }

  @GetMapping("/leads/status/new")
  public String showCreateForm() {
    return "leads/status";
  }

  @PostMapping("/leads/status")
  public String createStatus(@RequestParam String status) {
    StatusLead statusLead = new StatusLead(status);
    service.addStatus(statusLead);
    return "redirect:/leads/status";
  }
}
