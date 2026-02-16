package ru.mentee.power.crm.spring.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;

class LeadMapperTest {
  private final LeadMapper mapper = new LeadMapper();

  @Test
  void shouldMapRequestToDomain() {
    CreateLeadRequest request = new CreateLeadRequest(
        "test@email.com",
        "+123456789",
        "Test Company",
        "Moscow",
        "Tverskaya",
        "123456",
        LeadStatus.NEW
    );

    Lead lead = mapper.toDomain(request);

    assertNotNull(lead.id());
    assertEquals("test@email.com", lead.contact().email());
    assertEquals("Test Company", lead.company());
    assertEquals("Moscow", lead.contact().address().city());
    assertEquals(LeadStatus.NEW, lead.status());
  }
}