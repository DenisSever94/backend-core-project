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
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.domain.infrastructure.InMemoryLeadRepository;

class LeadServiceTest {
  private LeadService service;

  @BeforeEach
  void setUp() {
    LeadRepository repository = new InMemoryLeadRepository();
    service = new LeadService(repository);
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
    Address address = new Address("Москва", "Первая 1", "43434");

    service.addLead("one@mail.ru", "+89884", address, "One Company", LeadStatus.NEW);
    service.addLead("two@mail.ru", "+8988499", address, "Two Company", LeadStatus.NEW);

    List<Lead> list = service.findAll();
    assertThat(list).hasSize(2);
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
}