package ru.mentee.power.crm.spring.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
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

  @PostMapping("/leads/{id}")
  public String updateLead(@PathVariable UUID id, @ModelAttribute CreateLeadRequest request) {
    Lead lead = leadMapper.toDomain(request);
    leadService.update(id, lead);
    return "redirect:/leads";
  }

  @GetMapping("/")
  @ResponseBody
  public String home() {
    return "Spring Boot CRM is running! Beans created: " + leadService.findAll().size() + " leads.";
  }

  @GetMapping("/leads/{id}/edit")
  public String showEditForm(@PathVariable UUID id, Model model) {
    Optional<Lead> lead = leadService.findById(id);
    if (lead.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found");
    }
    model.addAttribute("lead", lead.get());
    return "spring/edit";
  }
}
