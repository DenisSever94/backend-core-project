package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@ExtendWith(MockitoExtension.class)
class LeadServiceMockTest {

  @Mock
  private LeadRepository mockRepository;
  private LeadService service;

  @BeforeEach
  void setUp() {
    service = new LeadService(mockRepository);
  }

  @Test
  void shouldCallRepositorySaveWithAdderNewLead() {
    Address address = new Address("Москва", "Первая 1", "43434");
    when(mockRepository.findByEmail(anyString()))
        .thenReturn(Optional.empty());

    when(mockRepository.save(any(Lead.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Lead result = service.addLead(
        "test@mail.ru", "+79322", address, "Company", LeadStatus.NEW);

    verify(mockRepository, times(1)).save(any(Lead.class));

    assertThat(result.contact().email()).isEqualTo("test@mail.ru");
  }

  @Test
  void shouldNotCallSaveWithEmailExists() {
    Address address = new Address("Москва", "Первая 1", "43434");
    Contact contact = new Contact("test@mail.ru", "+79884323454", address);
    Lead existingLead = new Lead(UUID.randomUUID(), contact, "Company", LeadStatus.CONTACTED);

    when(mockRepository.findByEmail("test@mail.ru"))
        .thenReturn(Optional.of(existingLead));

    assertThatThrownBy(
        () -> service.addLead("test@mail.ru", "+79884323454", address, "Company", LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class);

    verify(mockRepository, never()).save(any(Lead.class));

  }

  @Test
  void shouldCallFindByEmailBeforeSave() {
    Address address = new Address("Москва", "Первая 1", "43434");
    when(mockRepository.findByEmail(anyString()))
        .thenReturn(Optional.empty());

    when(mockRepository.save(any(Lead.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    service.addLead("test@mail.ru", "+895459", address, "Company", LeadStatus.NEW);

    var inOrder = inOrder(mockRepository);
    inOrder.verify(mockRepository).findByEmail("test@mail.ru");
    inOrder.verify(mockRepository).save(any(Lead.class));
  }
}
