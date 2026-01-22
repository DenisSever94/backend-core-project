package ru.mentee.power.crm.domain;

import java.util.Set;
import java.util.UUID;

public record Customer(UUID id, Contact contact, Address billingAddress, String loyaltyTier) {
  private static final Set<String> LOYALTY_TIER = Set.of("BRONZE", "SILVER", "GOLD");

  public Customer {
    if (id == null) {
      throw new IllegalArgumentException("ID must not be null");
    }
    if (contact == null) {
      throw new IllegalArgumentException("Contact must not be null");
    }
    if (billingAddress == null) {
      throw new IllegalArgumentException("Billing address must not be null");
    }
    if (loyaltyTier == null || !LOYALTY_TIER.contains(loyaltyTier)) {
      throw new IllegalArgumentException(
          "Loyalty must be one of:  Got: BRONZE, SILVER, GOLD. Got: " + loyaltyTier);
    }
  }
}
