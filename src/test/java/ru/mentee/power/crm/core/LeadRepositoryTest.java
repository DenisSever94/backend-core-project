package ru.mentee.power.crm.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class LeadRepositoryTest {
  private static final Address ADDRESS = new Address("Москва", "Молодежная 22", "45323");
  private static final Contact CONTACT = new Contact("test@mail.ru", "+7895485485", ADDRESS);
  private static final UUID FIXED_ID = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
  private LeadRepository repository;

  @BeforeEach
  void setUp() {
    repository = new LeadRepository();
  }

  @Test
  void shouldDeduplicateLeadsById() {
    Lead lead = new Lead(FIXED_ID, CONTACT, "Big", "NEW");
    Lead deduplicateLead = new Lead(FIXED_ID, CONTACT, "Big", "NEW");

    assertThat(repository.add(lead)).isTrue();
    assertThat(repository.add(deduplicateLead)).isFalse();

    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  void shouldAllowDifferentLeads() {
    Lead firstLead = new Lead(UUID.randomUUID(), CONTACT, "Big", "NEW");
    Lead secondLead = new Lead(UUID.randomUUID(), CONTACT, "Big", "NEW");

    assertThat(repository.add(firstLead)).isTrue();
    assertThat(repository.add(secondLead)).isTrue();

    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  void shouldFindExistingLead() {
    Lead lead = new Lead(UUID.randomUUID(), CONTACT, "Big", "NEW");

    assertThat(repository.add(lead)).isTrue();

    assertThat(repository.contains(lead)).isTrue();
  }

  @Test
  void shouldReturnUnmodifiableSet() {
    Lead firstLead = new Lead(UUID.randomUUID(), CONTACT, "Big", "NEW");
    assertThat(repository.add(firstLead)).isTrue();
    Set<Lead> leadSet = repository.findAll();

    assertThatThrownBy(leadSet::clear)
        .isInstanceOf(UnsupportedOperationException.class);

    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  void shouldPerformFasterThanArrayList() {
    Set<Lead> leadSet = new HashSet<>();
    List<Lead> leadList = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      Lead lead = new Lead(
          UUID.randomUUID(), CONTACT,
          "Company" + i,
          "NEW");
      leadSet.add(lead);
      leadList.add(lead);
    }

    Lead leadToFind = new ArrayList<>(leadSet).get(999);

    long hashSetStart = System.nanoTime();
    for (int i = 0; i < 10000; i++) {
      leadSet.contains(leadToFind);
    }
    long hashSetTime = System.nanoTime() - hashSetStart;

    long arrayListStart = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
      leadList.contains(leadToFind);
    }
    long arrayListTime = System.nanoTime() - arrayListStart;

    System.out.println("HashSet time: " + hashSetTime + " ns");
    System.out.println("ArrayList time: " + arrayListTime + " ns");
    System.out.println("Difference: " + (arrayListTime / hashSetTime) + "x faster");

    assertThat(arrayListTime)
        .as("ArrayList should be slower than HashSet")
        .isGreaterThan(hashSetTime);
  }
}