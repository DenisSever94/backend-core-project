package ru.mentee.power.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import ru.mentee.power.crm.domain.StatusLead;
import ru.mentee.power.crm.repository.StatusRepository;

@Service
public class LeadStatusService {
  private final StatusRepository<StatusLead> repository;

  public LeadStatusService(StatusRepository<StatusLead> repository) {
    this.repository = repository;
  }

  public void addStatus(StatusLead status) {
    List<StatusLead> statuses = repository.findByStatus(status);
    if (!statuses.isEmpty()) {
      throw new IllegalArgumentException("Статус уже существует");
    }
    repository.save(status);
  }

  public List<StatusLead> getAllStatuses() {
    return repository.findAll();
  }
}
