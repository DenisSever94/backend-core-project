package ru.mentee.power.crm.domain.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class InMemoryLeadRepositoryTest {

  @Test
  void shouldAddLeadWithInMemory() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Big", "NEW");
    repository.add(lead);

    List<Lead> list = repository.findAll();
    assertThat(list).hasSize(1);
  }

  @Test
  void shouldReturnLeadWithId() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID expectedId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead lead = new Lead(expectedId, contact, "Big", "NEW");
    repository.add(lead);

    Optional<Lead> foundLead =
        repository.findById(UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048"));

    assertThat(foundLead)
        .isPresent()
        .contains(lead);
  }

  @Test
  void shouldReturnAllLeadWithFindAll() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    Address firstAddress = new Address("Москва", "Молодежная 12", "44454");
    Address secondAddress = new Address("СПБ", "пр Невский 12", "44432");
    Contact firstContact = new Contact("test@mail.ru", "+79884323454", firstAddress);
    Contact secondContact = new Contact("testtest@mail.ru", "+79884323477", secondAddress);
    Lead firstLead = new Lead(UUID.randomUUID(), firstContact, "Big", "NEW");
    Lead secondLead = new Lead(UUID.randomUUID(), secondContact, "BigTech", "NEW");

    repository.add(firstLead);
    repository.add(secondLead);

    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  void shouldRemoveLeadWithID() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID leadId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead lead = new Lead(leadId, contact, "Big", "NEW");

    repository.add(lead);
    repository.remove(leadId);

    assertThat(repository.findAll())
        .as("Repository should be empty after removing the only lead")
        .isEmpty();

    Optional<Lead> notFound = repository.findById(leadId);
    assertThat(notFound)
        .as("Lead with ID %s should be not found after removal ", leadId)
        .isEmpty();
  }

  @Test
  void shouldThrowExceptionWithLeadDuplicate() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID leadId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead lead = new Lead(leadId, contact, "Big", "NEW");

    repository.add(lead);
    assertThat(repository.findAll()).hasSize(1);

    Lead duplicated = new Lead(leadId, contact, "Tehc", "NEW");

    assertThatThrownBy(() -> repository.add(duplicated))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Lead with ID " + lead.id() + " already exists");
  }

  @Test
  void shouldThrowExceptionWithRemoveLeadIsNull() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID leadId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead lead = new Lead(leadId, contact, "Big", "NEW");

    repository.add(lead);
    assertThat(repository.findAll()).hasSize(1);
    repository.remove(leadId);
    assertThat(repository.findAll()).isEmpty();

    assertThatThrownBy(() -> repository.remove(leadId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Lead not found with ID: " + leadId);
  }
}