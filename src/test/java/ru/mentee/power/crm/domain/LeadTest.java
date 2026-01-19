package ru.mentee.power.crm.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.storage.LeadStorage;

@DisplayName("Testing for Lead class")
class LeadTest {

  @Test
  void shouldPreventStringConfusionWhenUsingUUID() {
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "test@example.com", "+71234567890", "TestCorp", "NEW");
    LeadStorage leadStorage = new LeadStorage();
    leadStorage.add(lead);

    Lead found = leadStorage.findById(id);

    assertThat(found).isEqualTo(lead);
  }

  @Test
  void shouldReturnIdWhenGetIdCalled() {
    UUID expectedId = UUID.randomUUID();
    Lead lead = new Lead(expectedId, "test@example.com", "+71234567890", "TestCorp", "NEW");

    UUID actualId = lead.getId();

    assertThat(expectedId).isEqualTo(actualId);
  }

  @Test
  void shouldReturnEmailWhenGetEmailCalled() {
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "test@example.com", "+71234567890", "TestCorp", "NEW");

    String email = lead.getEmail();

    String actualEmail = "test@example.com";

    assertThat(email).isEqualTo(actualEmail);
  }

  @Test
  void shouldReturnPhoneWhenGetPhoneCalled() {
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "test@example.com", "+71234567890", "TestCorp", "NEW");

    String phone = lead.getPhone();

    String actualPhone = "+71234567890";

    assertThat(phone).isEqualTo(actualPhone);
  }

  @Test
  void shouldReturnCompanyWhenGetCompanyCalled() {
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "test@example.com", "+71234567890", "TestCorp", "NEW");

    String company = lead.getCompany();

    String actualCompany = "TestCorp";

    assertThat(company).isEqualTo(actualCompany);
  }

  @Test
  void shouldReturnStatusWhenGetStatusCalled() {
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "test@example.com", "+71234567890", "TestCorp", "NEW");

    String status = lead.getStatus();

    String actualStatus = "NEW";

    assertThat(status).isEqualTo(actualStatus);
  }

  @Test
  void shouldReturnFormattedStringWhenToStringCalled() {
    UUID id = UUID.fromString("74d2ef6d-460d-432f-b024-7ee70528820a");
    Lead lead = new Lead(id, "test@example.com", "+71234567890", "TestCorp", "NEW");

    String expected = "Lead{id='74d2ef6d-460d-432f-b024-7ee70528820a', email='test@example.com', phone='+71234567890', company='TestCorp', status='NEW'}";

    String actual = lead.toString();

    assertThat(actual).isEqualTo(expected);
  }
}
