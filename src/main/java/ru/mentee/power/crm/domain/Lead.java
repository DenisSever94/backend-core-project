package ru.mentee.power.crm.domain;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import lombok.Builder;

@Builder
public record Lead(UUID id, Contact contact, String company, LeadStatus status) {
  private static final Set<LeadStatus> ALLOWED_STATUSES =
      Set.of(LeadStatus.NEW,
          LeadStatus.CONVERTED,
          LeadStatus.QUALIFIED,
          LeadStatus.INVALID,
          LeadStatus.CONTACTED);

  public Lead {
    if (id == null) {
      throw new IllegalArgumentException("ID must not be null");
    }
    if (status == null) {
      throw new IllegalArgumentException("Status must not be null");
    }
    if (!ALLOWED_STATUSES.contains(status)) {
      throw new IllegalArgumentException(
          "Status must be one of: NEW, QUALIFIED, CONVERTED. Got: " + status);
    }
    if (contact == null) {
      throw new IllegalArgumentException("Contact must not be null");
    }
    if (company == null || company.isBlank()) {
      throw new IllegalArgumentException("Company must not be null or blank");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Lead lead)) {
      return false;
    }
    return Objects.equals(id, lead.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}