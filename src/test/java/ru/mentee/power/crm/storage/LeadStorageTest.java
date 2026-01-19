package ru.mentee.power.crm.storage;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Lead;

class LeadStorageTest {

  @Test
  void shouldAddLeadInArray() {
    UUID id = UUID.randomUUID();
    LeadStorage leadStorage = new LeadStorage();
    Lead lead = new Lead(id, "ivan@mail.ru", "+79119829801", "Tech", "New");

    boolean added = leadStorage.add(lead);

    assertThat(added).isTrue();
    assertThat(leadStorage.size()).isEqualTo(1);
    assertThat(leadStorage.findAll()).containsExactly(lead);
  }

  @Test
  void shouldRejectDuplicateWhenEmailAlreadyExist() {
    UUID id = UUID.randomUUID();
    LeadStorage leadStorage = new LeadStorage();
    Lead existingLead = new Lead(id, "ivan@mail.ru", "+79119829801", "Tech", "New");
    Lead duplicateLead = new Lead(id, "ivan@mail.ru", "+79119829801", "Tech", "New");
    leadStorage.add(existingLead);

    boolean added = leadStorage.add(duplicateLead);

    assertThat(added).isFalse();
    assertThat(leadStorage.size()).isEqualTo(1);
    assertThat(leadStorage.findAll()).containsExactly(existingLead);
  }

  @Test
  void shouldThrowExceptionWhenStorageIsFull() {
    UUID id = UUID.randomUUID();
    LeadStorage leadStorage = new LeadStorage();
    for (int i = 0; i < 100; i++) {
      leadStorage.add(new Lead(UUID.randomUUID(), "lead" + i + "ivan@mail.ru", "+79119829801", "Tech", "New"));
    }

    Lead hundredFirstLead = new Lead(id, "ivaner@mail.ru", "+79119829801", "Tech", "New");

    assertThatThrownBy(() -> leadStorage.add(hundredFirstLead))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Storage is full");
  }

  @Test
  void shouldReturnAddedOnlyWhenFindAllCalled() {
    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();
    LeadStorage leadStorage = new LeadStorage();
    Lead firstLead = new Lead(firstId, "ivan@mail.ru", "+79119829801", "Tech", "New");
    Lead secondLead = new Lead(secondId, "ivan43@mail.ru", "+79119829801", "Tech", "New");
    leadStorage.add(firstLead);
    leadStorage.add(secondLead);

    Lead[] result = leadStorage.findAll();

    assertThat(result).hasSize(2)
        .containsExactly(firstLead, secondLead);
  }
}
