package ru.mentee.power.crm.domain.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadRepository;

public class InMemoryLeadRepository implements LeadRepository {
  private final Map<UUID, Lead> storage = new HashMap<>();
  private final Map<String, UUID> emailIndex = new HashMap<>();

  @Override
  public Lead save(Lead lead) {
    storage.put(lead.id(), lead);
    emailIndex.put(lead.contact().email(), lead.id());
    return lead;
  }

  @Override
  public void delete(UUID id) {
    Lead removed = storage.remove(id);

    if (removed != null) {
      emailIndex.remove(removed.contact().email());
    }
  }

  @Override
  public Optional<Lead> findById(UUID id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Optional<Lead> findByEmail(String email) {
    UUID id = emailIndex.get(email);
    if (id == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public List<Lead> findAll() {
    return new ArrayList<>(storage.values());
  }
}
