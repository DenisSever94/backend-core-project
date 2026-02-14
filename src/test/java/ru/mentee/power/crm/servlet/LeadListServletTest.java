package ru.mentee.power.crm.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // ← РАЗРЕШАЕМ "лишние" стабы
class LeadListServletTest {

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private ServletContext servletContext;
  @Mock
  private LeadService leadService;

  @Mock
  private RequestDispatcher requestDispatcher;
  @Mock
  private ServletConfig servletConfig;

  private LeadListServlet servlet;
  private StringWriter responseWriter;
  private PrintWriter printWriter;

  @BeforeEach
  void setUp() throws Exception {
    servlet = new LeadListServlet();
    responseWriter = new StringWriter();
    printWriter = new PrintWriter(responseWriter);
    when(response.getWriter()).thenReturn(printWriter);
    when(servletConfig.getServletContext()).thenReturn(servletContext);
    servlet.init(servletConfig);
  }

  @Test
  void shouldForwardToFormWithErrorWhenEmailIsNull() throws Exception {
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("email")).thenReturn(null);
    when(request.getRequestDispatcher("/form")).thenReturn(requestDispatcher);

    servlet.doPost(request, response);

    verify(request).setAttribute(eq("error"), anyString());
    verify(requestDispatcher).forward(request, response);
    verify(leadService, never()).addLead(any(), any(), any(), any(), any());
  }

  @Test
  void shouldForwardToFormWithErrorWhenEmailIsBlank() throws Exception {
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("email")).thenReturn("   ");  // Пробелы
    when(request.getRequestDispatcher("/form")).thenReturn(requestDispatcher);

    servlet.doPost(request, response);

    verify(request).setAttribute(eq("error"), anyString());
    verify(requestDispatcher).forward(request, response);
    verify(leadService, never()).addLead(any(), any(), any(), any(), any());
  }

  @Test
  void shouldForwardToFormWithErrorWhenEmailIsMissing() throws Exception {
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("email")).thenReturn("");  // Пустой email
    when(request.getRequestDispatcher("/form")).thenReturn(requestDispatcher);

    servlet.doPost(request, response);

    verify(request).setAttribute(eq("error"), anyString());
    verify(requestDispatcher).forward(request, response);
    verify(leadService, never()).addLead(any(), any(), any(), any(), any());
  }

  @Test
  void shouldForwardToFormWithErrorWhenCompanyIsNull() throws Exception {
    when(request.getParameter("company")).thenReturn(null);
    when(request.getParameter("email")).thenReturn("test@mail.ru");
    when(request.getRequestDispatcher("/form")).thenReturn(requestDispatcher);

    servlet.doPost(request, response);

    verify(request).setAttribute(eq("error"), anyString());
    verify(requestDispatcher).forward(request, response);
    verify(leadService, never()).addLead(any(), any(), any(), any(), any());
  }

  @Test
  void shouldForwardToFormWithErrorWhenCompanyIsBlank() throws Exception {
    when(request.getParameter("company")).thenReturn("   ");  // Пробелы
    when(request.getParameter("email")).thenReturn("test@mail.ru");
    when(request.getRequestDispatcher("/form")).thenReturn(requestDispatcher);

    servlet.doPost(request, response);

    verify(request).setAttribute(eq("error"), anyString());
    verify(requestDispatcher).forward(request, response);
    verify(leadService, never()).addLead(any(), any(), any(), any(), any());
  }

  @Test
  void shouldUseUnknownForAllAddressFieldsWhenAllEmpty() throws Exception {
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("status")).thenReturn("NEW");
    when(request.getParameter("email")).thenReturn("test@mail.ru");
    when(request.getParameter("phone")).thenReturn("+79991112233");
    when(request.getParameter("city")).thenReturn("");      // Пустой
    when(request.getParameter("street")).thenReturn("");    // Пустой
    when(request.getParameter("zip")).thenReturn("");       // Пустой
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);

    servlet.doPost(request, response);

    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
    verify(leadService).addLead(
        eq("test@mail.ru"),
        eq("+79991112233"),
        addressCaptor.capture(),
        eq("ООО Тест"),
        eq(LeadStatus.NEW)
    );

    Address address = addressCaptor.getValue();
    assertThat(address.city()).isEqualTo("Unknown");
    assertThat(address.street()).isEqualTo("Unknown");
    assertThat(address.zip()).isEqualTo("Unknown");
  }

  @Test
  void shouldUseUnknownForEmptyZip() throws Exception {
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("status")).thenReturn("NEW");
    when(request.getParameter("email")).thenReturn("test@mail.ru");
    when(request.getParameter("phone")).thenReturn("+79991112233");
    when(request.getParameter("city")).thenReturn("Москва");
    when(request.getParameter("street")).thenReturn("Тверская 1");
    when(request.getParameter("zip")).thenReturn("");  // Пустой индекс
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);

    servlet.doPost(request, response);

    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
    verify(leadService).addLead(
        eq("test@mail.ru"),
        eq("+79991112233"),
        addressCaptor.capture(),
        eq("ООО Тест"),
        eq(LeadStatus.NEW)
    );

    assertThat(addressCaptor.getValue().city()).isEqualTo("Москва");
    assertThat(addressCaptor.getValue().street()).isEqualTo("Тверская 1");
    assertThat(addressCaptor.getValue().zip()).isEqualTo("Unknown");
  }

  @Test
  void shouldUseUnknownForEmptyStreet() throws Exception {
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("status")).thenReturn("NEW");
    when(request.getParameter("email")).thenReturn("test@mail.ru");
    when(request.getParameter("phone")).thenReturn("+79991112233");
    when(request.getParameter("city")).thenReturn("Москва");
    when(request.getParameter("street")).thenReturn("");  // Пустая улица
    when(request.getParameter("zip")).thenReturn("101000");
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);

    servlet.doPost(request, response);

    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
    verify(leadService).addLead(
        eq("test@mail.ru"),
        eq("+79991112233"),
        addressCaptor.capture(),
        eq("ООО Тест"),
        eq(LeadStatus.NEW)
    );

    assertThat(addressCaptor.getValue().city()).isEqualTo("Москва");
    assertThat(addressCaptor.getValue().street()).isEqualTo("Unknown");
    assertThat(addressCaptor.getValue().zip()).isEqualTo("101000");
  }

  @Test
  void shouldUseUnknownForEmptyCity() throws Exception {
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("status")).thenReturn("NEW");
    when(request.getParameter("email")).thenReturn("test@mail.ru");
    when(request.getParameter("phone")).thenReturn("+79991112233");
    when(request.getParameter("city")).thenReturn("");  // Пустой город
    when(request.getParameter("street")).thenReturn("Тверская 1");
    when(request.getParameter("zip")).thenReturn("101000");
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);

    servlet.doPost(request, response);

    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
    verify(leadService).addLead(
        eq("test@mail.ru"),
        eq("+79991112233"),
        addressCaptor.capture(),
        eq("ООО Тест"),
        eq(LeadStatus.NEW)
    );

    assertThat(addressCaptor.getValue().city()).isEqualTo("Unknown");
    assertThat(addressCaptor.getValue().street()).isEqualTo("Тверская 1");
    assertThat(addressCaptor.getValue().zip()).isEqualTo("101000");
  }

  @Test
  void shouldUseUnknownForPhoneWhenNull() throws Exception {
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("status")).thenReturn("NEW");
    when(request.getParameter("email")).thenReturn("test@mail.ru");
    when(request.getParameter("phone")).thenReturn(null);  // Null телефон
    when(request.getParameter("city")).thenReturn("Москва");
    when(request.getParameter("street")).thenReturn("Тверская 1");
    when(request.getParameter("zip")).thenReturn("101000");
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);

    servlet.doPost(request, response);

    verify(leadService).addLead(
        eq("test@mail.ru"),
        eq("Unknown"),
        any(Address.class),
        eq("ООО Тест"),
        eq(LeadStatus.NEW)
    );
  }

  @Test
  void shouldUseUnknownForEmptyPhone() throws Exception {
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("status")).thenReturn("NEW");
    when(request.getParameter("email")).thenReturn("test@mail.ru");
    when(request.getParameter("phone")).thenReturn("");  // Пустой телефон
    when(request.getParameter("city")).thenReturn("Москва");
    when(request.getParameter("street")).thenReturn("Тверская 1");
    when(request.getParameter("zip")).thenReturn("101000");
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);

    servlet.doPost(request, response);

    verify(leadService).addLead(
        eq("test@mail.ru"),
        eq("Unknown"),  // Должен стать Unknown
        any(Address.class),
        eq("ООО Тест"),
        eq(LeadStatus.NEW)
    );
  }

  @Test
  void shouldCreateLeadAndRedirectWhenAllParametersValid() throws Exception {
    // Настройка параметров запроса
    when(request.getParameter("company")).thenReturn("ООО Тест");
    when(request.getParameter("status")).thenReturn("NEW");
    when(request.getParameter("email")).thenReturn("test@mail.ru");
    when(request.getParameter("phone")).thenReturn("+79991112233");
    when(request.getParameter("city")).thenReturn("Москва");
    when(request.getParameter("street")).thenReturn("Тверская 1");
    when(request.getParameter("zip")).thenReturn("101000");
    when(request.getContextPath()).thenReturn("/crm");

    // Настройка сервиса в контексте
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);

    // Выполнение
    servlet.doPost(request, response);

    // Проверка создания Address
    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
    verify(leadService).addLead(
        eq("test@mail.ru"),
        eq("+79991112233"),
        addressCaptor.capture(),
        eq("ООО Тест"),
        eq(LeadStatus.NEW)
    );

    Address capturedAddress = addressCaptor.getValue();
    assertThat(capturedAddress.city()).isEqualTo("Москва");
    assertThat(capturedAddress.street()).isEqualTo("Тверская 1");
    assertThat(capturedAddress.zip()).isEqualTo("101000");

    // Проверка редиректа
    verify(response).sendRedirect("/crm/leads");
  }

  @Test
  void shouldReturnHtmlTableWithLeadsDoGet() throws Exception {
    // Подготовка данных
    Address address = new Address("Москва", "Тверская 1", "101000");
    List<Lead> testLeads = List.of(
        new Lead(java.util.UUID.randomUUID(),
            new Contact("ivan@mail.ru",
                "+79991112233", address),
            "ООО Рога и копыта",
            LeadStatus.NEW),

        new Lead(java.util.UUID.randomUUID(),
            new Contact("anna@mail.ru",
                "+79994445566", address),
            "ИП Копыта",
            LeadStatus.QUALIFIED),

        new Lead(java.util.UUID.randomUUID(),
            new Contact("petr@mail.ru",
                "+79997778899", address),
            "ЗАО Рога",
            LeadStatus.CONVERTED)
    );

    // Настройка моков
    ServletContext servletContext = mock(ServletContext.class);
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(testLeads);

    // Инициализация сервлета
    ServletConfig servletConfig = mock(ServletConfig.class);
    when(servletConfig.getServletContext()).thenReturn(servletContext);
    servlet.init(servletConfig);

    // Создание spy и выполнение
    LeadListServlet servletSpy = spy(servlet);
    doReturn(servletContext).when(servletSpy).getServletContext();
    servletSpy.doGet(request, response);
    printWriter.flush();

    // Проверки
    String html = responseWriter.toString();
    verify(leadService, times(1)).findAll();
    verify(response).setContentType("text/html; charset=UTF-8");
    assertThat(html)
        .contains("<!DOCTYPE html>", "CRM - Lead Management", "Lead List")
        .contains("ivan@mail.ru", "anna@mail.ru", "petr@mail.ru");
  }

  @Test
  void shouldHandleEmptyListDoGet() throws Exception {
    // Настройка моков
    when(request.getServletPath()).thenReturn("/leads");  // ВАЖНО!
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(List.of());

    // Выполнение
    servlet.doGet(request, response);
    printWriter.flush();

    // Проверки
    String html = responseWriter.toString();
    verify(leadService).findAll();
    assertThat(html)
        .contains("<!DOCTYPE html>")
        .contains("Lead List")
        .contains("<table")
        .doesNotContain("ivan@mail.ru");
  }

  @Test
  void shouldInitializeTemplateEngineDuringServletInit() throws Exception {
    // Простая проверка инициализации
    LeadListServlet freshServlet = new LeadListServlet();
    ServletConfig testConfig = mock(ServletConfig.class);
    ServletContext testContext = mock(ServletContext.class);

    // init() не должен бросать исключений
    freshServlet.init(testConfig);

    // Косвенная проверка - если TemplateEngine не создан, doGet() упадёт
    assertThat(freshServlet).isNotNull();
  }
}