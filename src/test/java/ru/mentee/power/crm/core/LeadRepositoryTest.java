package ru.mentee.power.crm.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class LeadRepositoryTest {
  private static final Address ADDRESS = new Address("Москва", "Молодежная 22", "45323");
  private static final Contact CONTACT = new Contact("test@mail.ru", "+7895485485", ADDRESS);
  private static final Contact CONTACT2 = new Contact("test@bk.ru", "+7895485485", ADDRESS);
  private static final UUID FIXED_ID = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");
  private LeadRepository repository;

  @BeforeEach
  void setUp() {
    repository = new LeadRepository();
  }

  @Test
  void shouldSavedLeadWithLeadId() {
    Lead lead = new Lead(FIXED_ID, CONTACT, "Big", "NEW");

    repository.save(lead);

    assertThat(repository.findById(FIXED_ID)).isNotNull();
  }

  @Test
  void shouldReturnAllLeadsWithFindAll() {
    Lead first = new Lead(UUID.randomUUID(), CONTACT, "Big", "NEW");
    Lead second = new Lead(UUID.randomUUID(), CONTACT, "Big", "NEW");
    Lead three = new Lead(UUID.randomUUID(), CONTACT, "Big", "NEW");

    repository.save(first);
    repository.save(second);
    repository.save(three);

    List<Lead> leads = repository.findAll();

    assertThat(leads)
        .hasSize(3);
  }

  @Test
  void shouldDeleteLeadWithHashMap() {
    Lead first = new Lead(FIXED_ID, CONTACT, "Big", "NEW");

    repository.save(first);
    repository.delete(FIXED_ID);

    assertThat(repository.findAll()).isEmpty();
  }

  @Test
  void shouldDuplicate() {
    Lead first = new Lead(UUID.randomUUID(), CONTACT, "Big", "NEW");
    Lead second = new Lead(UUID.randomUUID(), CONTACT, "Tech", "NEW");

    repository.save(first);
    repository.save(second);

    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  void shouldReturnNullWithLeadNotFound() {
    Optional<Lead> notFound = repository.findById(FIXED_ID);

    assertThat(notFound).isEmpty();
  }

  @Test
  void shouldOverwriteLeadWhenSaveWithSameId() {
    Lead first = new Lead(FIXED_ID, CONTACT, "Mig", "NEW");
    Lead second = new Lead(FIXED_ID, CONTACT2, "Web", "NEW");

    repository.save(first);
    repository.save(second);

    Optional<Lead> found = repository.findById(FIXED_ID);

    assertThat(found).contains(second);
  }

  @Test
  void shouldFindExistingLead() {
    Lead lead = new Lead(FIXED_ID, CONTACT, "Big", "NEW");

    repository.save(lead);

    Optional<Lead> result = repository.findById(FIXED_ID);

    assertThat(result).contains(lead);
  }

  @Test
  void shouldFindFasterWithMapThanWithListFilter() {
    // Given: Создать 1000 лидов
    List<Lead> leadList = new ArrayList<>();
    UUID targetId = UUID.fromString("c95b1eb2-1a94-45c5-8cf4-13035d321048");  // Фиксированный UUID

    for (int i = 0; i < 1000; i++) {
      // На 500-й позиции используем targetId, для остальных - случайные
      UUID id = (i == 500) ? targetId : UUID.randomUUID();
      Contact contact = new Contact(
          "email" + i + "@test.com",
          "+7" + i,
          new Address("City" + i, "Street" + i, "ZIP" + i)
      );
      Lead lead = new Lead(id, contact, "Company" + i, "NEW");
      repository.save(lead);
      leadList.add(lead);
    }

    // When: Поиск через Map
    long mapStart = System.nanoTime();
    Optional<Lead> foundInMap = repository.findById(targetId);
    long mapDuration = System.nanoTime() - mapStart;

    // When: Поиск через List.stream().filter()
    long listStart = System.nanoTime();
    Lead foundInList = leadList.stream()
        .filter(lead -> lead.id().equals(targetId))
        .findFirst()
        .orElse(null);
    long listDuration = System.nanoTime() - listStart;

    // Then: Map должен быть минимум в 10 раз быстрее
    assertThat(foundInMap).contains(foundInList);
    assertThat(listDuration).isGreaterThan(mapDuration * 10);

    System.out.println("Map поиск: " + mapDuration + " ns");
    System.out.println("List поиск: " + listDuration + " ns");
    System.out.println("Ускорение: " + (listDuration / mapDuration) + "x");
  }

  @Test
  void shouldSaveBothLeadsEvenWithSameEmailAndPhoneBecauseRepositoryDoesNotCheckBusinessRules() {
    Lead origin = new Lead(UUID.randomUUID(), CONTACT, "Big", "NEW");
    Lead duplicate = new Lead(UUID.randomUUID(), CONTACT, "Tech", "NEW");

    repository.save(origin);
    repository.save(duplicate);

    assertThat(repository.findAll()).hasSize(2);


  }
}