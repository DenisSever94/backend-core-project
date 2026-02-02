package ru.mentee.power.crm.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.Repository;

public class LeadRepository implements Repository<Lead> {
  private final Map<UUID, Lead> storage = new HashMap<>();

  public Lead save(Lead lead) {
    storage.put(lead.id(), lead);
    return lead;
  }

  public Optional<Lead> findById(UUID id) {
    return Optional.ofNullable(storage.get(id));
  }

  public List<Lead> findAll() {
    List<Lead> findAllLeads = storage
        .values()
        .stream()
        .toList();
    return new ArrayList<>(findAllLeads);
  }

  public Optional<Lead> findByEmail(String email) {
    if (email == null || email.isBlank()) {
      return Optional.empty();
    }
    return storage.values().stream()
        .filter(lead -> lead.contact() != null)
        .filter(lead -> email.equals(lead.contact().email()))
        .findFirst();
  }

  public void delete(UUID id) {
    storage.remove(id);
  }

  public int size() {
    return storage.size();
  }
}
