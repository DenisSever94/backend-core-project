package ru.mentee.power.crm.domain.infrastructure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.domain.StatusLead;
import ru.mentee.power.crm.repository.StatusRepository;

@Repository
public class StatusMemory implements StatusRepository<StatusLead> {
  Set<StatusLead> storage = new HashSet<>();

  @Override
  public StatusLead save(StatusLead status) {
    boolean statusLead = storage.contains(status);
    if (statusLead) {
      throw new IllegalArgumentException("Статус такой уже существует");
    }
    storage.add(status);
    return status;
  }

  @Override
  public List<StatusLead> findAll() {
    return new ArrayList<>(storage.stream().toList());
  }

  @Override
  public List<StatusLead> findByStatus(StatusLead status) {
    return storage.stream()
        .filter(statusLead -> statusLead.name().equals(status.name())).toList();
  }
}
