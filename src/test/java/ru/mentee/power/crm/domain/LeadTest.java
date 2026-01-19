package ru.mentee.power.crm.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testing for Lead class")
class LeadTest {

  @Test
  void shouldReturnIdWhenGetIdCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");

    String id = lead.getId();

    String actualId = "L1";

    assertThat(id).isEqualTo(actualId);
  }

  @Test
  void shouldReturnEmailWhenGetEmailCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");

    String email = lead.getEmail();

    String actualEmail = "test@example.com";

    assertThat(email).isEqualTo(actualEmail);
  }

  @Test
  void shouldReturnPhoneWhenGetPhoneCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");

    String phone = lead.getPhone();

    String actualPhone = "+71234567890";

    assertThat(phone).isEqualTo(actualPhone);
  }

  @Test
  void shouldReturnCompanyWhenGetCompanyCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");

    String company = lead.getCompany();

    String actualCompany = "TestCorp";

    assertThat(company).isEqualTo(actualCompany);
  }

  @Test
  void shouldReturnStatusWhenGetStatusCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");

    String status = lead.getStatus();

    String actualStatus = "NEW";

    assertThat(status).isEqualTo(actualStatus);
  }

  @Test
  void shouldReturnFormattedStringWhenToStringCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");

    String expected = "Lead{id='L1', email='test@example.com', phone='+71234567890', company='TestCorp', status='NEW'}";

    String actualResult = lead.toString();

    assertThat(actualResult).isEqualTo(expected);
  }
}
