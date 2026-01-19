package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class LeadEqualsHashCodeTest {

  @Test
  void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
    Lead lead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(lead).isEqualTo(lead);
  }

  @Test
  void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
    Lead firstLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead secondLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(secondLead).isEqualTo(firstLead);
  }

  @Test
  void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
    Lead firstLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead secondLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead thirdLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(secondLead).isEqualTo(firstLead);
    assertThat(firstLead).isEqualTo(thirdLead);
  }

  @Test
  void shouldReturnFalseWhenEqualsComparedWithNull() {
    Lead lead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(lead).isNotEqualTo(null);
  }

  @Test
  void shouldHaveSameHashCodeWhenObjectsAreEqual() {
    Lead firstLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead secondLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(firstLead.hashCode()).isEqualTo(secondLead.hashCode());
  }

  @Test
  void shouldWorkInHashMapWhenLeadUsedAsKey() {
    Lead keyLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead lookupLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    Map<Lead, String> map = new HashMap<>();
    map.put(keyLead, "CONTACTED");

    String status = map.get(lookupLead);

    assertThat(status).isEqualTo("CONTACTED");
  }

  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    Lead firstLead = new Lead("L1", "ivan@mail.ru", "+79219329934", "Tech", "NEW");
    Lead differentLead = new Lead("L2", "ivan@mail.ru", "+79219329934", "Tech", "NEW");

    assertThat(firstLead).isNotEqualTo(differentLead);
  }
}
