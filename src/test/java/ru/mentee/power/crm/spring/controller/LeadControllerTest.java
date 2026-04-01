package ru.mentee.power.crm.spring.controller;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;

@SpringBootTest
class LeadControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockitoBean
  LeadService leadService;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void shouldReturnEditFormWithLead() throws Exception {
    UUID expectedId = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", LeadStatus.NEW);

    when(leadService.findById(expectedId)).thenReturn(Optional.of(lead));
    mockMvc.perform(get("/leads/" + expectedId + "/edit"))
        .andExpect(status().isOk())
        .andExpect(view().name("spring/edit"))
        .andExpect(model().attributeExists("lead"));
  }

  @Test
  void shouldReturnShowViewForm() throws Exception {
    UUID expectedId = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", LeadStatus.NEW);

    when(leadService.findById(expectedId)).thenReturn(Optional.of(lead));
    mockMvc.perform(post("/leads/" + expectedId)
            .param("company", "TestCorp")
            .param("email", "test@example.com")
            .param("phone", "+71234567890")
            .param("city", "Москва")
            .param("street", "Молодежная 12")
            .param("zip", "34345")
            .param("status", "NEW"))
        .andExpect(redirectedUrl("/leads"));

  }

  @Test
  void shouldReturnNotFoundWhenLeadNotFound() throws Exception {
    UUID expectedId = UUID.randomUUID();

    when(leadService.findById(expectedId))
        .thenReturn(Optional.empty());
    mockMvc.perform(get("/leads/" + expectedId + "/edit"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnOkAndContainEmail() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/leads"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .string(org.hamcrest.Matchers.containsString("Почта")));
  }

  @Test
  void shouldReturnLeadsCreateView() throws Exception {
    mockMvc.perform(get("/leads/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("leads/create"))
        .andExpect(model().attributeExists("createLeadRequest"))
        .andExpect(model().attribute("createLeadRequest", instanceOf(CreateLeadRequest.class)));

  }
}