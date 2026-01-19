package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidData() {
    Contact contact = new Contact("Ivan", "Ivanov", "ivanivanov@mail.ru");
    String firstName = contact.firstName();
    String lastName = contact.lastName();
    String email = contact.email();

    String actualFirstName = "Ivan";
    String actualLastName = "Ivanov";
    String actualEmail = "ivanivanov@mail.ru";

    assertThat(firstName).isEqualTo(actualFirstName);
    assertThat(lastName).isEqualTo(actualLastName);
    assertThat(email).isEqualTo(actualEmail);
  }

  @Test
  void shouldBeEqualWhenSameData() {
    Contact firstContact = new Contact("Ivan", "Ivanov", "ivanivanov@mail.ru");
    Contact secondContact = new Contact("Ivan", "Ivanov", "ivanivanov@mail.ru");

    assertThat(firstContact).isEqualTo(secondContact);
    assertThat(secondContact.hashCode()).isEqualTo(firstContact.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenDifferentData() {
    Contact firstContact = new Contact("Ivan", "Ivanov", "ivanivanov@mail.ru");
    Contact secondContact = new Contact("Oleg", "Ivanov", "olegivanov@mail.ru");

    assertThat(firstContact).isNotEqualTo(secondContact);
  }
}
