package ru.mentee.power.crm.storage;

import java.util.UUID;

import ru.mentee.power.crm.domain.Lead;

public class LeadStorage {
  private Lead[] leads = new Lead[100];

  public boolean add(Lead lead) {
    for (int index = 0; index < leads.length; index++) {
      if (leads[index] != null) {

        String existingEmail = leads[index].contact().email();
        String newEmail = lead.contact().email();

        if (existingEmail != null && existingEmail.equals(newEmail)) {
          return false;
        }
      }
    }
    for (int index = 0; index < leads.length; index++) {
      if (leads[index] == null) {
        leads[index] = lead;
        return true;
      }
    }
    throw new IllegalArgumentException("Storage is full");
  }

  public Lead[] findAll() {
    int count = 0;
    int resultIndex = 0;
    for (int index = 0; index < leads.length; index++) {
      if (leads[index] != null) {
        count++;
      }
    }

    Lead[] result = new Lead[count];

    for (int index = 0; index < result.length; index++) {
      if (leads[index] != null) {
        result[resultIndex++] = leads[index];
      }

    }
    return result;
  }

  public int size() {
    int count = 0;
    for (int index = 0; index < leads.length; index++) {
      if (leads[index] != null) {
        count++;
      }
    }
    return count;
  }

  public Lead findById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    for (int i = 0; i < leads.length; i++) {
      Lead currentLead = leads[i];
      if (currentLead != null && currentLead.id().equals(id)) {
        return currentLead;
      }
    }
    return null;
  }
}
