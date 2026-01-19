package ru.mentee.power.crm.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class ContactLombok {
  private String firstName;
  private String lastName;
  private String email;
}
