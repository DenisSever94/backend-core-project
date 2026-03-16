package ru.mentee.power.crm.spring;

import java.util.List;
import java.util.UUID;

import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

public class MockLeadService extends LeadService {
  private final List<Lead> mockLeads;

  Address address1 = new Address("Moscow", "One", "12343");
  Address address2 = new Address("Moscow", "Two", "12343");
  Contact contact1 = new Contact("Test 1", "+843239", address1);
  Contact contact2 = new Contact("Test 2", "+84343239", address2);

  public MockLeadService() {
    super(null);
    this.mockLeads = List.of(
        new Lead(UUID.randomUUID(), contact1, "Tech", LeadStatus.NEW),
        new Lead(UUID.randomUUID(), contact2, "Big", LeadStatus.NEW)
    );
  }

  @Override
  public List<Lead> findAll() {
    return mockLeads;
  }
}
