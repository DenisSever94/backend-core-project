package ru.mentee.power.crm.domain.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;

class InMemoryLeadRepositoryTest {

  @Test
  void shouldAddLeadWithInMemory() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Big", LeadStatus.NEW);
    repository.save(lead);

    List<Lead> list = repository.findAll();
    assertThat(list).hasSize(1);
  }

  @Test
  void shouldReturnLeadWithId() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID expectedId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead lead = new Lead(expectedId, contact, "Big", LeadStatus.NEW);
    repository.save(lead);

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
    Lead firstLead = new Lead(UUID.randomUUID(), firstContact, "Big", LeadStatus.NEW);
    Lead secondLead = new Lead(UUID.randomUUID(), secondContact, "BigTech", LeadStatus.NEW);

    repository.save(firstLead);
    repository.save(secondLead);

    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  void shouldDeleteLeadWithID() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID leadId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead lead = new Lead(leadId, contact, "Big", LeadStatus.NEW);

    repository.save(lead);
    repository.delete(leadId);

    assertThat(repository.findAll())
        .as("Repository should be empty after removing the only lead")
        .isEmpty();

    Optional<Lead> notFound = repository.findById(leadId);
    assertThat(notFound)
        .as("Lead with ID %s should be not found after removal ", leadId)
        .isEmpty();
  }

  @Test
  void shouldOverwriteLeadWithSameId() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID leadId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address address = new Address("Москва", "Молодежная 12", "43454");

    // Первый лид
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead firstLead = new Lead(leadId, contact, "Big", LeadStatus.NEW);

    // Второй лид с ТЕМ ЖЕ ID (обновление)
    Lead secondLead = new Lead(leadId, contact, "Tech", LeadStatus.QUALIFIED);

    // Сохраняем первый
    repository.save(firstLead);
    assertThat(repository.findAll()).hasSize(1);

    // ПЕРЕЗАПИСЫВАЕМ вторым - теперь это разрешено!
    repository.save(secondLead);

    // Проверяем, что остался один элемент (не два!)
    assertThat(repository.findAll()).hasSize(1);

    // Проверяем, что теперь сохранён второй лид
    Optional<Lead> found = repository.findById(leadId);
    assertThat(found).isPresent();
    assertThat(found.get().company()).isEqualTo("Tech");
    assertThat(found.get().status()).isEqualTo(LeadStatus.QUALIFIED);
  }

  @Test
  void shouldDeleteSilentlyWhenLeadNotFound() {
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID leadId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
    Address address = new Address("Москва", "Молодежная 12", "43454");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead lead = new Lead(leadId, contact, "Big", LeadStatus.NEW);

    // Сохраняем
    repository.save(lead);
    assertThat(repository.findAll()).hasSize(1);

    // Удаляем существующий
    repository.delete(leadId);
    assertThat(repository.findAll()).isEmpty();

    // Пытаемся удалить НЕСУЩЕСТВУЮЩИЙ - НЕ ДОЛЖНО БЫТЬ ИСКЛЮЧЕНИЯ!
    // Просто ничего не происходит
    assertThatNoException()
        .isThrownBy(() -> repository.delete(leadId));

    // Убеждаемся, что всё ещё пусто
    assertThat(repository.findAll()).isEmpty();
  }
}