package ru.mentee.power.crm.spring.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.spring.MockLeadService;
import ru.mentee.power.crm.spring.mapper.LeadMapper;

class LeadControllerUnitTest {

  @Test
  void shouldCreateControllerWithoutSpring() {
    MockLeadService mockService = new MockLeadService();
    LeadMapper leadMapper = new LeadMapper();
    LeadController leadController = new LeadController(mockService, leadMapper);

    String response = leadController.home();

    assertThat(response).contains("2 leads");

  }

  @Test
  void shouldUseInjectedService() {
    MockLeadService mockService = new MockLeadService();
    LeadMapper leadMapper = new LeadMapper();
    LeadController leadController = new LeadController(mockService, leadMapper);

    String response = leadController.home();

    assertThat(response).isNotNull().contains("Spring Boot CRM is running");
  }
}
