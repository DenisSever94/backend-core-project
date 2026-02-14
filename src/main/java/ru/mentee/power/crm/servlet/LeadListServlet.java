package ru.mentee.power.crm.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.WriterOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@Slf4j
@WebServlet({"/leads", "/leads/new"})
public class LeadListServlet extends HttpServlet {
  private TemplateEngine templateEngine;
  private static final String UNKNOWN = "Unknown";

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    Path templatePath = Path.of("src/main/jte");
    DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(templatePath);
    this.templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String path = request.getServletPath();
    log.info("GET request for path: {}", path);

    if ("/leads/new".equals(path)) {
      showCreateForm(request, response);
    } else {
      showLeadList(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    log.info("POST /leads request received");

    try {
      String company = request.getParameter("company");
      if (company == null || company.isBlank()) {
        throw new IllegalArgumentException("Company must be null/is empty");
      }

      String statusString = request.getParameter("status");
      String email = request.getParameter("email");
      if (email == null || email.isBlank()) {
        throw new IllegalArgumentException("Email must be null/is empty");
      }

      LeadStatus status;
      try {
        status = LeadStatus.valueOf(statusString.toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(
            "Некорректный статус. Допустимые: NEW, CONTACTED, QUALIFIED");
      }
      String phone = request.getParameter("phone");
      phone = (phone == null || phone.isBlank()) ? UNKNOWN : phone;

      String city = request.getParameter("city");
      city = (city == null || city.isBlank()) ? UNKNOWN : city;

      String street = request.getParameter("street");
      street = (street == null || street.isBlank()) ? UNKNOWN : street;

      String zip = request.getParameter("zip");
      zip = (zip == null || zip.isBlank()) ? UNKNOWN : zip;

      ServletContext context = getServletContext();
      LeadService leadService = (LeadService) context.getAttribute("leadService");
      Address address = new Address(city, street, zip);
      leadService.addLead(email, phone, address, company, status);
      response.sendRedirect(request.getContextPath() + "/leads");

    } catch (IllegalArgumentException e) {
      log.warn("Validation error: {}", e.getMessage());
      request.setAttribute("error", e.getMessage());
      request.getRequestDispatcher("/form").forward(request, response);
    }
  }

  private void showCreateForm(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    log.info("GET /leads/new - showing create form");

    // Просто показываем форму создания лида
    // (форма лежит в src/main/jte/leads/form.jte)

    // Создаём пустую модель (можно передать null lead для чистой формы)
    Map<String, Object> model = new HashMap<>();
    model.put("lead", null);  // Для шаблона, который проверяет lead != null

    // Устанавливаем content type
    resp.setContentType("text/html; charset=UTF-8");

    // Рендерим шаблон формы
    templateEngine.render("leads/create.jte", model, new WriterOutput(resp.getWriter()));
  }

  private void showLeadList(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    ServletContext context = getServletContext();
    LeadService service = (LeadService) context.getAttribute("leadService");
    List<Lead> leads = service.findAll();

    Map<String, Object> model = new HashMap<>();
    model.put("leads", leads);

    resp.setContentType("text/html; charset=UTF-8");
    templateEngine.render("leads/list.jte", model, new WriterOutput(resp.getWriter()));
  }

}
