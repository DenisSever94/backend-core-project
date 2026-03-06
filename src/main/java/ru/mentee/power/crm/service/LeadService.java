package ru.mentee.power.crm.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@Slf4j
@Service
public class LeadService {

  private final LeadRepository repository;

  public LeadService(LeadRepository repository) {
    this.repository = repository;
    log.info("LeadService constructor called");
  }

  @PostConstruct
  public void init() {
    log.info("LeadService @PostConstruct init() called - Bean lifecycle phase");
  }

  public void addLead(Lead lead) {
    String email = lead.contact().email();
    repository.findByEmail(email).ifPresent(existingLead -> {
      throw new IllegalArgumentException("Lead with email already exists: " + email);
    });

    repository.save(lead);
  }

  @Deprecated
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
}
