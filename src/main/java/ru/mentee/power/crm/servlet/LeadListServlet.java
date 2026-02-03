package ru.mentee.power.crm.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.service.LeadService;

@Slf4j
@WebServlet("/leads")
public class LeadListServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    log.info("GET /leads request received");

    ServletContext context = getServletContext();
    LeadService service = (LeadService) context.getAttribute("leadService");
    List<Lead> leads = service.findAll();

    response.setContentType("text/html; charset=UTF-8");

    try (PrintWriter writer = response.getWriter()) {

      writer.println("<!DOCTYPE html>");
      writer.println("<html>");
      writer.println("<head><title>CRM - Lead List</title></head>");
      writer.println("<body>");
      writer.println("<h1>Lead List</h1>");
      writer.println("<table border='1'>");
      writer.println("<thead>");
      writer.println("<tr>");
      writer.println("<th>Email</th>");
      writer.println("<th>Company</th>");
      writer.println("<th>Status</th>");
      writer.println("</tr>");
      writer.println("</thead>");
      writer.println("<tbody>");

      for (Lead lead : leads) {
        writer.println("<tr>");
        writer.println("<td>" + lead.contact().email() + "</td>");
        writer.println("<td>" + lead.company() + "</td>");
        writer.println("<td>" + lead.status().name() + "</td>");
      }

      writer.println("</tbody>");
      writer.println("</table>");
      writer.println("</body>");
      writer.println("</html>");
    }

    log.info("Response sent successfully");
  }
}
