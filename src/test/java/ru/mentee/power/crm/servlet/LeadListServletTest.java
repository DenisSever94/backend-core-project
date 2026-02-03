package ru.mentee.power.crm.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@ExtendWith(MockitoExtension.class)
class LeadListServletTest {

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private ServletContext servletContext;
  @Mock
  private LeadService leadService;
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
    // ===== ARRANGE (подготовка) =====

    // 1. Создаём тестовые данные (3 лида)
    Address address = new Address("Москва", "Тверская 1", "101000");

    Lead lead1 = new Lead(
        java.util.UUID.randomUUID(),
        new Contact("ivan@mail.ru", "+79991112233", address),
        "ООО Рога и копыта",
        LeadStatus.NEW
    );

    Lead lead2 = new Lead(
        java.util.UUID.randomUUID(),
        new Contact("anna@mail.ru", "+79994445566", address),
        "ИП Копыта",
        LeadStatus.QUALIFIED
    );

    Lead lead3 = new Lead(
        java.util.UUID.randomUUID(),
        new Contact("petr@mail.ru", "+79997778899", address),
        "ЗАО Рога",
        LeadStatus.CONVERTED
    );

    List<Lead> testLeads = List.of(lead1, lead2, lead3);

    // 2. Настраиваем моки:
    //    - Когда сервис вызовут findAll(), вернуть наш тестовый список
    when(leadService.findAll()).thenReturn(testLeads);

    //    - Настроить цепочку для получения сервиса из контекста
    //      Эмулируем, что в ServletContext лежит наш мок-сервис под ключом "leadService"
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);

    //    - Сервлет внутри doGet() вызывает getServletContext()
    //      Нужно подменить этот вызов. Используем Mockito.spy для частичного мокинга сервлета
    LeadListServlet servletSpy = spy(servlet);
    doReturn(servletContext).when(servletSpy).getServletContext();

    // ===== ACT (действие) =====
    // Вызываем тестируемый метод
    servletSpy.doGet(request, response);

    // Важно: flush writer, чтобы все данные попали в StringWriter
    printWriter.flush();

    // Получаем HTML, который сгенерировал сервлет
    String generatedHtml = responseWriter.toString();

    // ===== ASSERT (проверки) =====

    // 1. Проверяем, что сервис был вызван ровно 1 раз
    verify(leadService, times(1)).findAll();

    // 2. Проверяем, что установлен правильный Content-Type
    verify(response).setContentType("text/html; charset=UTF-8");

    // 3. Проверяем структуру HTML (ключевые части)
    assertThat(generatedHtml)
        .as("HTML должен содержать заголовок страницы")
        .contains("<title>CRM - Lead List</title>");

    assertThat(generatedHtml)
        .as("HTML должен содержать заголовок таблицы")
        .contains("<h1>Lead List</h1>");

    assertThat(generatedHtml)
        .as("HTML должен содержать начало таблицы")
        .contains("<table");

    // 4. Проверяем, что данные всех лидов присутствуют в таблице
    assertThat(generatedHtml)
        .as("Должен содержать email первого лида")
        .contains("ivan@mail.ru");

    assertThat(generatedHtml)
        .as("Должен содержать компанию второго лида")
        .contains("ИП Копыта");

    assertThat(generatedHtml)
        .as("Должен содержать статус третьего лида")
        .contains("CONVERTED");

    // 5. Проверяем, что для каждого лида создана строка таблицы
    //    (примерно 3 раза встречается <tr> внутри <tbody>)
    //    Можно подсчитать или проверить, что все email есть
    assertThat(generatedHtml)
        .as("Должен содержать все три email из тестовых данных")
        .contains("ivan@mail.ru", "anna@mail.ru", "petr@mail.ru");

    // 6. Проверяем, что writer был закрыт (благодаря try-with-resources)
    //    Это происходит автоматически, можно не проверять явно
  }

  @Test
  void shouldHandleEmptyListDoGet() throws Exception {
    // Тест на случай, если лидов нет
    // ===== ARRANGE =====
    when(leadService.findAll()).thenReturn(List.of()); // пустой список

    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    LeadListServlet servletSpy = spy(servlet);
    doReturn(servletContext).when(servletSpy).getServletContext();

    // ===== ACT =====
    servletSpy.doGet(request, response);
    printWriter.flush();
    String generatedHtml = responseWriter.toString();

    // ===== ASSERT =====
    // 1. Сервис был вызван
    verify(leadService).findAll();

    // 2. Таблица всё равно создана (но без строк данных)
    assertThat(generatedHtml)
        .contains("<table", "</table>") // таблица есть
        .doesNotContain("<td>"); // но нет ячеек с данными (если список пуст)

    // или проверяем, что нет строк в tbody (кроме заголовка)
    // assertThat(countOccurrences(generatedHtml, "<tr>")).isEqualTo(1); // только заголовок
  }
}