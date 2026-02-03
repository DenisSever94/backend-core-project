package ru.mentee.power.crm;

import java.io.File;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.LeadStatus;
import ru.mentee.power.crm.domain.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;

@Slf4j
public class ApplicationLead {
  public static void main(String[] args) throws Exception {

    int port = 8080;

    LeadRepository repository = new InMemoryLeadRepository();
    LeadService service = new LeadService(repository);
    Address address = new Address("Москва", "Первая 1", "05944");
    service.addLead("test1@mail.ru", "+795444", address, "Company 1", LeadStatus.NEW);
    service.addLead("test2@mail.ru", "+7953454", address, "Company 2", LeadStatus.NEW);
    service.addLead("test3@mail.ru", "+7954454", address, "Company 3", LeadStatus.NEW);
    service.addLead("test4@mail.ru", "+795454", address, "Company 4", LeadStatus.NEW);
    service.addLead("test5@mail.ru", "+795454", address, "Company 5", LeadStatus.NEW);
    service.addLead("<script>alert('XSS')</script>", "+795454", address, "Company 5", LeadStatus.NEW);

    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);
    tomcat.getConnector();
    Context context = tomcat.addContext("", new File(".").getAbsolutePath());
    context.getServletContext().setAttribute("leadService", service);
    Tomcat.addServlet(context, "LeadListServlet", new LeadListServlet());

    context.addServletMappingDecoded("/leads", "LeadListServlet");
    log.info("Starting Tomcat...");
    tomcat.start();
    tomcat.getServer().await();

    log.info("Tomcat started on port:{}", port);
    log.info("Open http://localhost:8080/leads in browser");
  }
}