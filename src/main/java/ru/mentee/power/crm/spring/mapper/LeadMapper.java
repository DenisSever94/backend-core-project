package ru.mentee.power.crm.spring.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;

@Component
public class LeadMapper {

  public Lead toDomain(CreateLeadRequest request) {
    Contact contact = mapContact(request);
    return Lead.builder()
        .id(UUID.randomUUID())
        .contact(contact)
        .company(request.company())
        .status(request.status())
        .build();
  }

  private Address mapAddress(CreateLeadRequest request) {
    return new Address(
        request.city(),
        request.street(),
        request.zip()
    );
  }

  private Contact mapContact(CreateLeadRequest request) {
    return new Contact(
        request.email(),
        request.phone(),
        new Address(request.city(), request.street(), request.zip())
    );
  }
}
