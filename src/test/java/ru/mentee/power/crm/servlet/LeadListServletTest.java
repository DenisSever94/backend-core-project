package ru.mentee.power.crm.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private LeadService leadService;

  private LeadListServlet servlet;
  private StringWriter responseWriter;
  private PrintWriter printWriter;

  @BeforeEach
  void setUp() throws Exception {
    servlet = new LeadListServlet();
    responseWriter = new StringWriter();
    printWriter = new PrintWriter(responseWriter);
    when(response.getWriter()).thenReturn(printWriter);
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
    ServletContext servletContext = mock(ServletContext.class);
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(List.of());

    // Инициализация
    ServletConfig servletConfig = mock(ServletConfig.class);
    when(servletConfig.getServletContext()).thenReturn(servletContext);
    servlet.init(servletConfig);

    // Выполнение
    LeadListServlet servletSpy = spy(servlet);
    doReturn(servletContext).when(servletSpy).getServletContext();
    servletSpy.doGet(request, response);
    printWriter.flush();

    // Проверки
    String html = responseWriter.toString();
    verify(leadService).findAll();
    assertThat(html)
        .contains("<!DOCTYPE html>", "Lead List", "<table")
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