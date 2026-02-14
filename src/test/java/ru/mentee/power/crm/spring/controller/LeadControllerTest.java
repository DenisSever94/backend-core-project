package ru.mentee.power.crm.spring.controller;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;

@SpringBootTest
class LeadControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockitoBean
  LeadService service;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void shouldReturnOkAndContainEmail() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/leads"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content()
            .string(org.hamcrest.Matchers.containsString("Email")));
  }

  @Test
  void shouldReturnLeadsCreateView() throws Exception {
    mockMvc.perform(get("/leads/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("leads/create"))
        .andExpect(model().attributeExists("createLeadRequest"))
        .andExpect(model().attribute("createLeadRequest", instanceOf(CreateLeadRequest.class)));

  }

  @Test
  void shouldCreateLeadWithValidateAndRedirect() throws Exception {
    Address address = new Address("Moscow", "Tverskaya 1", "12345");

    mockMvc.perform(post("/leads")
            .param("company", "Test corp")
            .param("status", "NEW")
            .param("email", "test@mail.ru")
            .param("phone", "+79313313322")
            .param("city", "Moscow")
            .param("street", "Tverskaya 1")
            .param("zip", "12345"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/leads"));

    verify(service).addLead(
        eq("test@mail.ru"),
        eq("+79313313322"),
        eq(address),
        eq("Test corp"),
        eq(LeadStatus.NEW)

    );
  }
}