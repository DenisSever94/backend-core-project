package ru.mentee.power.crm.spring.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;
import ru.mentee.power.crm.spring.mapper.LeadMapper;

@Controller
@RequiredArgsConstructor
public class LeadController {
  private final LeadService leadService;
  private final LeadMapper leadMapper;

  @GetMapping("/leads")
  public String showLeads(
      @RequestParam(required = false) LeadStatus status, Model model) {
    List<Lead> leads = (status == null)
        ? leadService.findAll()
        : leadService.findByStatus(status);
    model.addAttribute("leads", leads);
    model.addAttribute("currentFilter", status);
    return "leads/list";
  }

  @GetMapping("/leads/new")
  public String showCreatedForm(Model model) {
    model.addAttribute("createLeadRequest", new CreateLeadRequest());
    return "leads/create";
  }

  @PostMapping("/leads")
  public String createLead(@ModelAttribute CreateLeadRequest request) {
    Lead lead = leadMapper.toDomain(request);
    leadService.addLead(lead);
    return "redirect:/leads";
  }

  @GetMapping("/")
  @ResponseBody
  public String home() {
    return "Spring Boot CRM is running! Beans created: " + leadService.findAll().size() + " leads.";
  }
}
