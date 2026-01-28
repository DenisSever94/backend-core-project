package ru.mentee.power.crm.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import ru.mentee.power.crm.domain.Lead;

public class LeadRepository {

  private final Map<UUID, Lead> storage = new HashMap<>();

  public void save(Lead lead) {
    storage.put(lead.id(), lead);
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

  public void delete(UUID id) {
    storage.remove(id);
  }

  public int size() {
    return storage.size();
  }
}
