package ru.mentee.power.crm.repository;

import java.util.List;

import ru.mentee.power.crm.domain.StatusLead;

public interface StatusRepository<T> {
  T save(T status);

  List<T> findAll();

  List<T> findByStatus(StatusLead status);
}
