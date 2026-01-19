package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class LeadEqualsHashCodeTest {

  @Test
  void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(lead).isEqualTo(lead);
  }

  @Test
  void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
    UUID id = UUID.randomUUID();
    Lead firstLead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead secondLead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(secondLead).isEqualTo(firstLead);
  }

  @Test
  void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
    UUID id = UUID.randomUUID();
    Lead firstLead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead secondLead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead thirdLead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(secondLead).isEqualTo(firstLead);
    assertThat(firstLead).isEqualTo(thirdLead);
  }

  @Test
  void shouldReturnFalseWhenEqualsComparedWithNull() {
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(lead).isNotEqualTo(null);
  }

  @Test
  void shouldHaveSameHashCodeWhenObjectsAreEqual() {
    UUID id = UUID.randomUUID();
    Lead firstLead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead secondLead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(firstLead.hashCode()).isEqualTo(secondLead.hashCode());
  }

  @Test
  void shouldWorkInHashMapWhenLeadUsedAsKey() {
    UUID id = UUID.randomUUID();
    Lead keyLead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead lookupLead = new Lead(id, "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    Map<Lead, String> map = new HashMap<>();
    map.put(keyLead, "CONTACTED");

    String status = map.get(lookupLead);

    assertThat(status).isEqualTo("CONTACTED");
  }

  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    UUID firstLeadId = UUID.randomUUID();
    UUID differentLeadId = UUID.randomUUID();
    Lead firstLead = new Lead(firstLeadId, "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead differentLead = new Lead(differentLeadId, "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(firstLead).isNotEqualTo(differentLead);
  }
}
