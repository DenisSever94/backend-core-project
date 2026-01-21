package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
    Lead lead = new Lead(UUID.randomUUID(), contact, "Big", "NEW");

    assertThat(customer.contact().address().city()).isEqualTo("Москва");
    assertThat(lead.contact().address().city()).isEqualTo("Москва");
  }
}
