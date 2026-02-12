package ru.mentee.power.crm.dto;

import ru.mentee.power.crm.domain.LeadStatus;

public record CreateLeadRequest(String email,
                                String phone,
                                String company,
                                String city,
                                String street,
                                String zip,
                                LeadStatus status) {

  public CreateLeadRequest() {
    this("", "", "", "", "", "", LeadStatus.NEW);
  }
}
