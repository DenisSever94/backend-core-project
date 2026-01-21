package ru.mentee.power.crm.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class LeadStorageTest {

  @Test
  void shouldAddLeadInArray() {
    UUID id = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead lead = new Lead(id, contact, "TestCorp", "NEW");
    LeadStorage leadStorage = new LeadStorage();

    boolean added = leadStorage.add(lead);

    assertThat(added).isTrue();
    assertThat(leadStorage.size()).isEqualTo(1);
    assertThat(leadStorage.findAll()).containsExactly(lead);
  }

  @Test
  void shouldRejectDuplicateWhenEmailAlreadyExist() {
    UUID id = UUID.randomUUID();
    Address address = new Address("Москва", "Молодежная 12", "34345");
    Contact contact = new Contact("test@example.com", "+71234567890", address);
    Lead existingLead = new Lead(id, contact, "TestCorp", "NEW");
    Lead duplicateLead = new Lead(id, contact, "TestCorp", "NEW");

    LeadStorage leadStorage = new LeadStorage();
    leadStorage.add(existingLead);

    boolean added = leadStorage.add(duplicateLead);

    assertThat(added).isFalse();
    assertThat(leadStorage.size()).isEqualTo(1);
    assertThat(leadStorage.findAll()).containsExactly(existingLead);
  }

  @Test
  void shouldThrowExceptionWhenStorageIsFull() {
    LeadStorage leadStorage = new LeadStorage();

    // Создаём 100 УНИКАЛЬНЫХ Lead (с разными email)
    for (int i = 0; i < 100; i++) {
      // Каждый Lead должен иметь УНИКАЛЬНЫЙ contact с уникальным email
      Contact contact = new Contact(
          "ivaner" + i + "@mail.ru",  // Уникальный email для каждого!
          "+79119829801",
          new Address("Москва", "Молодежная 12", "32345")
      );

      Lead lead = new Lead(
          UUID.randomUUID(),  // Уникальный UUID
          contact,            // Уникальный contact
          "Tech",
          "NEW"
      );

      boolean added = leadStorage.add(lead);
      assertThat(added).isTrue(); // Убеждаемся, что каждый добавился
    }

    // 101-й Lead (тоже с уникальным email)
    Contact hundredFirstContact = new Contact(
        "hundredfirst@mail.ru",  // Ещё один уникальный email
        "+79119829801",
        new Address("Москва", "Молодежная 12", "32345")
    );

    Lead hundredFirstLead = new Lead(
        UUID.randomUUID(),
        hundredFirstContact,
        "Tech",
        "NEW"
    );

    // Теперь должно бросить исключение
    assertThatThrownBy(() -> leadStorage.add(hundredFirstLead))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Storage is full");
  }

  @Test
  void shouldReturnAddedOnlyWhenFindAllCalled() {
    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();

    Address firstAddress = new Address("Москва", "Молодежная 12", "34345");
    Address secondAddress = new Address("СПБ", "пр Невский 12", "32345");
    Contact firstContact = new Contact("test@example.com", "+71234567890", firstAddress);
    Contact secondContact = new Contact("testing@mail.ru", "+5495849303", secondAddress);

    Lead firstLead = new Lead(firstId, firstContact, "TestCorp", "NEW");
    Lead secondLead = new Lead(secondId, secondContact, "Tech", "NEW");
    LeadStorage leadStorage = new LeadStorage();

    leadStorage.add(firstLead);
    leadStorage.add(secondLead);

    Lead[] result = leadStorage.findAll();

    assertThat(result).hasSize(2)
        .containsExactly(firstLead, secondLead);
  }
}
