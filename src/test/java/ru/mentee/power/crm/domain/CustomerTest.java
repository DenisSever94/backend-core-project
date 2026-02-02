package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class CustomerTest {

  @Test
  void shouldReuseContactWhenCreatingCustomer() {
    Address addressContact = new Address("Москва", "Молодежная 22", "34564");
    Address addressBilling = new Address("СПБ", "пр Невский 43", "456443");
    Contact contact = new Contact("test@mail.ru", "+79812434", addressContact);
    Customer customer = new Customer(UUID.randomUUID(), contact, addressBilling, "GOLD");

    assertThat(customer.contact().address()).isNotEqualTo(customer.billingAddress());
  }

  @Test
  void shouldDemonstrateContactReuseAcrossLeadAndCustomer() {
    Address address = new Address("Москва", "Молодежная 22", "34564");
    Address addressBilling = new Address("СПБ", "пр Невский 43", "456443");
    Contact contact = new Contact("test@mail.ru", "+79812434", address);

    Customer customer = new Customer(UUID.randomUUID(), contact, addressBilling, "GOLD");
    Lead lead = new Lead(UUID.randomUUID(), contact, "Big", LeadStatus.NEW);

    assertThat(customer.contact().address().city()).isEqualTo("Москва");
    assertThat(lead.contact().address().city()).isEqualTo("Москва");
  }

  @Test
  void shouldThrowExceptionWhenCustomerIdIsNull() {
    Address billingAddress = new Address("Москва", "Молодежная 12", "21345");
    Contact contact = new Contact("test@example.com", "+71234567890", billingAddress);

    assertThatThrownBy(() -> new Customer(null, contact, billingAddress, "GOLD"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("ID must not be null");
  }

  @Test
  void shouldThrowExceptionWhenCustomerContactIsNull() {
    UUID id = UUID.randomUUID();
    Address billingAddress = new Address("Москва", "Молодежная 12", "21345");

    assertThatThrownBy(() -> new Customer(id, null, billingAddress, "GOLD"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Contact must not be null");
  }

  @Test
  void shouldThrowExceptionWhenLoyaltyTierIsNull() {
    UUID id = UUID.randomUUID();
    Address addressContact = new Address("Москва", "Молодежная 22", "34564");
    Address billingAddress = new Address("Москва", "Молодежная 12", "21345");
    Contact contact = new Contact("test@example.com", "+71234567890", addressContact);

    assertThatThrownBy(() -> new Customer(id, contact, billingAddress, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Loyalty must be one of:  Got: BRONZE, SILVER, GOLD.");
  }

  @Test
  void shouldThrowExceptionWhenLoyaltyTierIsBlank() {
    UUID id = UUID.randomUUID();
    Address addressContact = new Address("Москва", "Молодежная 22", "34564");
    Address billingAddress = new Address("Москва", "Молодежная 12", "21345");
    Contact contact = new Contact("test@example.com", "+71234567890", addressContact);

    assertThatThrownBy(() -> new Customer(id, contact, billingAddress, ""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Loyalty must be one of:  Got: BRONZE, SILVER, GOLD.");
  }

  @Test
  void shouldThrowExceptionWhenBillingAddressIsNull() {
    UUID id = UUID.randomUUID();
    Address addressContact = new Address("Москва", "Молодежная 22", "34564");
    Contact contact = new Contact("test@example.com", "+71234567890", addressContact);

    assertThatThrownBy(() -> new Customer(id, contact, null, "GOLD"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Billing address must not be null");
  }
}
