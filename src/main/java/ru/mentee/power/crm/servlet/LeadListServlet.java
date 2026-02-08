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
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.service.LeadService;

@WebServlet("/leads")
public class LeadListServlet extends HttpServlet {
  private TemplateEngine templateEngine;

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

    System.out.println("GET /leads request received");

    ServletContext context = getServletContext();
    LeadService service = (LeadService) context.getAttribute("leadService");
    List<Lead> leads = service.findAll();

    Map<String, Object> model = new HashMap<>();
    model.put("leads", leads);
    response.setContentType("text/html; charset=UTF-8");

    templateEngine.render("leads/list.jte", model, new WriterOutput(response.getWriter()));

  }
}
