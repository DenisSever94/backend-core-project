package ru.mentee.power.crm.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@Service
public class LeadService {

  private final LeadRepository repository;

  public LeadService(LeadRepository repository) {
    this.repository = repository;
//    initTestData();
  }

  public Lead addLead(
      String email, String phone, Address address, String company, LeadStatus status) {
    Optional<Lead> existing = repository.findByEmail(email);
    if (existing.isPresent()) {
      throw new IllegalArgumentException("Lead with email already exists: " + email);
    }

    Contact contact = new Contact(email, phone, address);
    Lead lead = new Lead(
        UUID.randomUUID(),
        contact,
        company,
        status
    );

    return repository.save(lead);
  }

  public List<Lead> findAll() {
    return repository.findAll();
  }

  public Optional<Lead> findById(UUID id) {
    return repository.findById(id);
  }

  public Optional<Lead> findByEmail(String email) {
    return repository.findByEmail(email);
  }

  public List<Lead> findByStatus(LeadStatus status) {
    return repository.findAll().stream()
        .filter(lead -> lead.status().equals(status))
        .toList();
  }

//  private void initTestData() {
//    if (repository.findAll().isEmpty()) {
//      Address address = new Address("Москва", "Молодежная", "44344");
//      Contact contact = new Contact("spring@mail.ru", "+4055", address);
//
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 1", LeadStatus.NEW));
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 2", LeadStatus.CONTACTED));
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 3", LeadStatus.CONTACTED));
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 4", LeadStatus.QUALIFIED));
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 5", LeadStatus.QUALIFIED));
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 6", LeadStatus.QUALIFIED));
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 7", LeadStatus.QUALIFIED));
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 8", LeadStatus.QUALIFIED));
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 9", LeadStatus.NEW));
//      repository.save(new Lead(UUID.randomUUID(), contact, "Company 10", LeadStatus.NEW));
//    }
//  }
}
