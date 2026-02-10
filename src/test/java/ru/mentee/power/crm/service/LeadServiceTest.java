package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.domain.infrastructure.InMemoryLeadRepository;

class LeadServiceTest {
  private LeadService service;

  @BeforeEach
  void setUp() {
    var repository = new InMemoryLeadRepository();
    service = new LeadService(repository);

    Address address = new Address("Москва", "Первая 1", "43434");

    service.addLead("one@mail.ru", "+89884", address, "One Company", LeadStatus.NEW);
    service.addLead("two@mail.ru", "+8988499", address, "Two Company", LeadStatus.NEW);
    service.addLead("three@mail.ru", "+8988499", address, "Three Company", LeadStatus.NEW);

    service.addLead("four@mail.ru", "+8988499", address, "Four Company", LeadStatus.CONTACTED);
    service.addLead("five@mail.ru", "+8988499", address, "Five Company", LeadStatus.CONTACTED);
    service.addLead("six@mail.ru", "+8988499", address, "Six Company", LeadStatus.CONTACTED);
    service.addLead("seven@mail.ru", "+8988499", address, "Seven Company", LeadStatus.CONTACTED);
    service.addLead("eight@mail.ru", "+8988499", address, "Eight Company", LeadStatus.CONTACTED);

    service.addLead("nine@mail.ru", "+8988499", address, "Nine Company", LeadStatus.QUALIFIED);
    service.addLead("ten@mail.ru", "+8988499", address, "Ten Company", LeadStatus.QUALIFIED);
  }

  @Test
  void shouldCreatedLeadWithEmailInUnique() {
    String email = "test@mail.ru";
    String phone = "+434345455";
    String company = "Test Company";
    Address address = new Address("Москва", "Первая 1", "43434");

    Lead result = service.addLead(email, phone, address, company, LeadStatus.NEW);

    assertThat(result).isNotNull();
    assertThat(result.contact().email()).isEqualTo(email);
    assertThat(result.company()).isEqualTo(company);
    assertThat(result.status()).isEqualTo(LeadStatus.NEW);
    assertThat(result.id()).isNotNull();
  }

  @Test
  void shouldThrowExceptionWithEmailAlreadyExist() {
    String email = "duplicate@mail.ru";
    Address address = new Address("Москва", "Первая 1", "43434");

    service.addLead(email, "+44434", address, "First Company", LeadStatus.NEW);

    assertThatThrownBy(
        () -> service.addLead(email, "+988434", address, "Second Company", LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Lead with email already exists: " + email);
  }

  @Test
  void shouldFindAllLeads() {
    List<Lead> all = service.findAll();
    assertThat(all).hasSize(10);
  }

  @Test
  void shouldFindLeadById() {
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Lead createdLead = service.addLead(
        "test@mail.ru",
        "+09554433",
        address,
        "Company",
        LeadStatus.NEW);

    UUID actualId = createdLead.id();
    Optional<Lead> found = service.findById(actualId);

    assertThat(found).isPresent();
    assertThat(found.get().contact().email()).isEqualTo("test@mail.ru");
  }

  @Test
  void shouldFindLeadByEmail() {
    Address address = new Address("Москва", "Молодежная 12", "43454");
    service.addLead(
        "test@mail.ru",
        "+95454",
        address,
        "Company",
        LeadStatus.NEW);

    Optional<Lead> found = service.findByEmail("test@mail.ru");

    assertThat(found).isPresent();
    assertThat(found.get().contact().email()).isEqualTo("test@mail.ru");
  }

  @Test
  void shouldReturnEmptyWhenLeadNotFound() {
    Optional<Lead> result = service.findByEmail("nonexistent@example.com");

    assertThat(result).isEmpty();
  }

  @Test
  void shouldReturnOnlyNewLeadsWithFindByStatusNew() {
    List<Lead> statusNew = service.findByStatus(LeadStatus.NEW);

    assertThat(statusNew)
        .hasSize(3)
        .allMatch(lead -> lead.status().equals(LeadStatus.NEW));
  }

  @Test
  void shouldReturnEmptyListWhenNoLeadsWithStatus() {
    var emptyRepository = new InMemoryLeadRepository();
    var serviceWithEmptyRepository = new LeadService(emptyRepository);

    List<Lead> notQualified = serviceWithEmptyRepository.findByStatus(LeadStatus.QUALIFIED);

    assertThat(notQualified).isEmpty();
  }

  @Test
  void shouldReturnOnlyNewLeadsWithFindByStatusQualified() {
    List<Lead> statusQualified = service.findByStatus(LeadStatus.QUALIFIED);

    assertThat(statusQualified)
        .hasSize(2)
        .allMatch(lead -> lead.status().equals(LeadStatus.QUALIFIED));
  }

  @Test
  void shouldReturnOnlyNewLeadsWithFindByStatusContacted() {
    List<Lead> statusContacted = service.findByStatus(LeadStatus.CONTACTED);

    assertThat(statusContacted)
        .hasSize(5)
        .allMatch(lead -> lead.status().equals(LeadStatus.CONTACTED));
  }
}