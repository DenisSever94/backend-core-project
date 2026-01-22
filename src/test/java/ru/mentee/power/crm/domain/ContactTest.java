package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidData() {
    Address address = new Address("Москва", "Молодежная 12", "23345");
    Contact contact = new Contact(
        "ivanivanov@mail.ru", "+79813434455", address);

    assertThat(contact.address()).isEqualTo(address);
    assertThat(contact.address())
        .extracting(Address::city, Address::street, Address::zip)
        .containsExactlyInAnyOrder("Москва", "Молодежная 12", "23345");
  }

  @Test
  void shouldDelegateToAddressWhenAccessingCity() {
    Contact contact = new Contact(
        "ivanivanov@mail.ru", "+79813434455",
        new Address("Москва", "Молодежная 12", "23345"));

    assertThat(contact.address().city()).isEqualTo("Москва");
    assertThat(contact.address().street()).isEqualTo("Молодежная 12");
  }

  @Test
  void shouldThrowExceptionWithAddressIsNull() {

    assertThatThrownBy(() -> new Contact("ivanivanov@mail.ru", "+79813434455", null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Address");
  }

  @Test
  void shouldBeEqualWhenSameData() {
    Contact firstContact = new Contact(
        "ivanivanov@mail.ru", "+79813434455",
        new Address("Москва", "Молодежная 12", "23345"));

    Contact secondContact = new Contact(
        "ivanivanov@mail.ru", "+79813434455",
        new Address("Москва", "Молодежная 12", "23345"));


    assertThat(firstContact).isEqualTo(secondContact);
    assertThat(secondContact.hashCode()).isEqualTo(firstContact.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenDifferentData() {
    Contact firstContact = new Contact(
        "ivanivanov@mail.ru", "+79813434455",
        new Address("Москва", "Молодежная 12", "23345"));
    Contact secondContact = new Contact(
        "ivanivanov@mail.ru", "+79813434455",
        new Address("СПБ", "пр Невский 12", "23345"));

    assertThat(firstContact).isNotEqualTo(secondContact);
  }

  @Test
  void shouldThrowExceptionWithContactEmailIsNull() {
    Address address = new Address("Москва", "Молодежная 23", "43454");

    assertThatThrownBy(() -> new Contact(null, "+79118284556", address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email must not be null or blank");
  }

  @Test
  void shouldThrowExceptionWithContactEmailIsBlank() {
    Address address = new Address("Москва", "Молодежная 23", "43454");

    assertThatThrownBy(() -> new Contact("", "+79118284556", address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email must not be null or blank");
  }

  @Test
  void shouldThrowExceptionWithContactEmailIsNullOrPhoneIsNull() {
    Address address = new Address("Москва", "Молодежная 23", "43454");

    assertThatThrownBy(() -> new Contact(null, null, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Contact must not be null");
  }

  @Test
  void shouldThrowExceptionWithPhoneIsNull() {
    Address address = new Address("Москва", "Молодежная 23", "43454");

    assertThatThrownBy(() -> new Contact("test@mail.ru", null, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Phone must not be null or blank");
  }

  @Test
  void shouldThrowExceptionWithPhoneIsBlank() {
    Address address = new Address("Москва", "Молодежная 23", "43454");

    assertThatThrownBy(() -> new Contact("test@mail.ru", " ", address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Phone must not be null or blank");
  }
}
