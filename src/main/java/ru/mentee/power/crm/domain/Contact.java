package ru.mentee.power.crm.domain;

public record Contact(String email, String phone, Address address) {
  public Contact {
    if (email == null && phone == null) {
      throw new IllegalArgumentException("Contact must not be null");
    }
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email must not be null or blank");
    }
    if (phone == null || phone.isBlank()) {
      throw new IllegalArgumentException("Phone must not be null or blank");
    }
    if (address == null) {
      throw new IllegalArgumentException("Address must not be null");
    }
  }
}
