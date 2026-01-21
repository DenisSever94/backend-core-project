package ru.mentee.power.crm.domain;

public record Address(String city, String street, String zip) {
  public Address {
    if (city == null || city.isBlank()) {
      throw new IllegalArgumentException("City not must be null or blank");
    }
    if (street == null || street.isBlank()) {
      throw new IllegalArgumentException("Street not must be null or blank");
    }
  }
}
