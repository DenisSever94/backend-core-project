package ru.mentee.power.crm.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  void shouldCreateAddressWhenValidData() {
    Address address = new Address("Москва", "Молодежная 12", "43545");
    String city = address.city();
    String street = address.street();
    String zip = address.zip();

    String resultCity = "Москва";
    String resultStreet = "Молодежная 12";
    String resultZip = "43545";

    assertThat(city).isEqualTo(resultCity);
    assertThat(street).isEqualTo(resultStreet);
    assertThat(zip).isEqualTo(resultZip);
  }

  @Test
  void shouldBeEqualWhenSameData() {
    Address firstAddress = new Address("Москва", "Молодежная 12", "43545");
    Address secondAddress = new Address("Москва", "Молодежная 12", "43545");

    assertThat(firstAddress).isEqualTo(secondAddress);
    assertThat(firstAddress.hashCode()).isEqualTo(secondAddress.hashCode());
  }

  @Test
  void shouldThrowExceptionWhenCityIsNull() {
    assertThatThrownBy(() -> new Address(null, "Молодежная 12", "43545"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("City not must be null or blank");
  }

  @Test
  void shouldThrowExceptionWhenCityIsBlank() {
    assertThatThrownBy(() -> new Address("", "Молодежная 12", "43545"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("City not must be null or blank");
  }
}