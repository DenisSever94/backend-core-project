package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class LeadEqualsHashCodeTest {

  @Test
  void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
    UUID expectedId = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", LeadStatus.NEW);

    assertThat(lead).isEqualTo(lead);
  }

  @Test
  void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
    UUID id = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead firstLead = new Lead(id, contact, "Tech", LeadStatus.NEW);
    Lead secondLead = new Lead(id, contact, "Tech", LeadStatus.NEW);

    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(secondLead).isEqualTo(firstLead);
  }

  @Test
  void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
    UUID id = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead firstLead = new Lead(id, contact, "Tech", LeadStatus.NEW);
    Lead secondLead = new Lead(id, contact, "Tech", LeadStatus.NEW);
    Lead thirdLead = new Lead(id, contact, "Tech", LeadStatus.NEW);

    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(secondLead).isEqualTo(firstLead);
    assertThat(firstLead).isEqualTo(thirdLead);
  }

  @Test
  void shouldReturnFalseWhenEqualsComparedWithNull() {
    UUID expectedId = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(expectedId, contact, "TestCorp", LeadStatus.NEW);

    assertThat(lead).isNotEqualTo(null);
  }

  @Test
  void shouldHaveSameHashCodeWhenObjectsAreEqual() {
    UUID id = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead firstLead = new Lead(id, contact, "Tech", LeadStatus.NEW);
    Lead secondLead = new Lead(id, contact, "Tech", LeadStatus.NEW);

    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(firstLead.hashCode()).isEqualTo(secondLead.hashCode());
  }

  @Test
  void shouldWorkInHashMapWhenLeadUsedAsKey() {
    UUID id = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead keyLead = new Lead(id, contact, "Tech", LeadStatus.NEW);
    Lead lookupLead = new Lead(id, contact, "Tech", LeadStatus.NEW);
    ;

    Map<Lead, String> map = new HashMap<>();
    map.put(keyLead, "CONTACTED");

    String status = map.get(lookupLead);

    assertThat(status).isEqualTo("CONTACTED");
  }

  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    UUID firstLeadId = UUID.randomUUID();
    UUID differentLeadId = UUID.randomUUID();
    Address firstAddress = new Address("Москва", "Молодежная 12", "34345");
    Address secondAddress = new Address("СПБ", "пр Невский 12", "43454");
    Contact firstContact = new Contact("test@example.com", "+71234567890", firstAddress);
    Contact secondContact = new Contact("seconttest@example.com", "+45334554333", secondAddress);
    Lead firstLead = new Lead(firstLeadId, firstContact, "Tech", LeadStatus.NEW);
    Lead differentLead = new Lead(differentLeadId, secondContact, "Tech", LeadStatus.NEW);

    assertThat(firstLead).isNotEqualTo(differentLead);
  }
}
