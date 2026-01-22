package ru.mentee.power.crm.domain.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.Repository;

public class InMemoryLeadRepository implements Repository<Lead> {
  private final List<Lead> storage = new ArrayList<>();

  @Override
  public void add(Lead lead) {
    if (storage.contains(lead)) {
      throw new IllegalArgumentException("Lead with ID " + lead.id() + " already exists");
    }
    storage.add(lead);
  }

  @Override
  public void remove(UUID id) {
    Objects.requireNonNull(id, "ID cannot be null");

    boolean removed = storage.removeIf(lead -> lead.id().equals(id));

    if (!removed) {
      throw new IllegalArgumentException("Lead not found with ID: " + id);
    }
  }

  @Override
  public Optional<Lead> findById(UUID id) {
    return storage.stream()
        .filter(lead -> lead.id()
            .equals(id)).findFirst();
  }

  @Override
  public List<Lead> findAll() {
    return new ArrayList<>(storage);
  }
}
