package ru.mentee.power.crm.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.storage.LeadStorage;

@DisplayName("Testing for Lead class")
class LeadTest {

  @Test
  void shouldDemonstrateThreeLevelCompositionWhenAccessingCity() {
    Address address = new Address("Москва", "Молодежная 12", "21345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Tech", "NEW");

    String city = lead.contact().address().city();

    assertThat(city).isEqualTo("Москва");
  }

  @Test
  void shouldThrowExceptionWhenContactIsNull() {
    assertThatThrownBy(() -> new Lead(UUID.randomUUID(), null, "Big", "NEW"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Contact must not be null");
  }

  @Test
  void shouldThrowExceptionWhenInvalidStatus() {
    Address address = new Address("Москва", "Молодежная 12", "21345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    assertThatThrownBy(() -> new Lead(UUID.randomUUID(), contact, "Big", "INVALID"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Status must be one of: NEW, QUALIFIED, CONVERTED. Got: ");
  }

  @Test
  void shouldCreateLeadWhenValidData() {
    Address address = new Address("Москва", "Молодежная 12", "21345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Tech", "NEW");

    assertThat(lead.contact()).isEqualTo(contact);
  }

  @Test
  void shouldAccessEmailThroughDelegationWhenLeadCreated() {
    Address address = new Address("Москва", "Молодежная", "45456");
    Contact contact = new Contact("test@mail.ru", "+79843435654", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "BigTech", "NEW");

    String actualEmail = lead.contact().email();
    String actualCity = lead.contact().address().city();

    assertThat(actualEmail).isEqualTo("test@mail.ru");
    assertThat(actualCity).isEqualTo("Москва");
  }

  @Test
  void shouldBeEqualWhenSameIdButDifferentContact() {
    UUID expectedId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address firstAddress = new Address("Москва", "Молодежная", "34545");
    Address secondAddress = new Address("СПБ", "пр Невский 112", "67804");
    Contact firstContact = new Contact("test@mail.ru", "+78234568890", firstAddress);
    Contact secondContact = new Contact("test@bk.ru", "+79653244593", secondAddress);
    Lead firstLead = new Lead(expectedId, firstContact, "Big", "NEW");
    Lead secondLead = new Lead(expectedId, secondContact, "Big", "NEW");

    assertThat(firstLead).isEqualTo(secondLead);
  }

  @Test
  void shouldPreventStringConfusionWhenUsingUUID() {
    UUID id = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(id, contact, "TestCorp", "NEW");
    LeadStorage leadStorage = new LeadStorage();

    boolean added = leadStorage.add(lead);
    Lead found = leadStorage.findById(id);

    assertThat(added).isTrue();
    assertThat(found).isNotNull();
    assertThat(found).isEqualTo(lead);
  }

  @Test
  void shouldReturnIdWhenGetIdCalled() {
    UUID expectedId = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", "NEW");

    UUID actualId = lead.id();

    assertThat(expectedId).isEqualTo(actualId);
  }

  @Test
  void shouldReturnEmailWhenGetEmailCalled() {
    UUID expectedId = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", "NEW");

    String email = lead.contact().email();

    String actualEmail = "test@example.com";

    assertThat(email).isEqualTo(actualEmail);
  }

  @Test
  void shouldReturnPhoneWhenGetPhoneCalled() {
    UUID expectedId = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", "NEW");

    String phone = lead.contact().phone();

    String actualPhone = "+71234567890";

    assertThat(phone).isEqualTo(actualPhone);
  }

  @Test
  void shouldReturnCompanyWhenGetCompanyCalled() {
    UUID expectedId = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", "NEW");

    String company = lead.company();

    String actualCompany = "TestCorp";

    assertThat(company).isEqualTo(actualCompany);
  }

  @Test
  void shouldReturnStatusWhenGetStatusCalled() {
    UUID expectedId = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", "NEW");

    String status = lead.status();

    String actualStatus = "NEW";

    assertThat(status).isEqualTo(actualStatus);
  }

  @Test
  void shouldReturnFormattedStringWhenToStringCalled() {
    UUID expectedId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", "NEW");
    String expected = "Lead[id=c95b1eb2-1a94-45c5-8cf4-13035d321048, "
        + "contact=Contact[email=test@example.com, "
        + "phone=+71234567890, "
        + "address=Address"
        + "[city=Москва, street=Молодежная 12, zip=34345]], company=TestCorp, status=NEW]";

    String actual = lead.toString();

    assertThat(actual).isEqualTo(expected);
  }
}
