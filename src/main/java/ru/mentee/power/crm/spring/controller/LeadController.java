package ru.mentee.power.crm.spring.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;

@Controller
@AllArgsConstructor
public class LeadController {
  private final LeadService leadService;

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
    Address address = new Address(
        request.city(),
        request.street(),
        request.zip()
    );

    leadService.addLead(
        request.email(),
        request.phone(),
        address,
        request.company(),
        request.status());
    return "redirect:/leads";
  }
}
