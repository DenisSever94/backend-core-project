package ru.mentee.power.crm;

import java.io.File;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
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

    Tomcat tomcat = new Tomcat();
    tomcat.setPort(port);
    tomcat.getConnector();
    Context context = tomcat.addContext("", new File(".").getAbsolutePath());
    context.getServletContext().setAttribute("leadService", service);
    Tomcat.addServlet(context, "LeadListServlet", new LeadListServlet());

    context.addServletMappingDecoded("/leads", "LeadListServlet");
    context.addServletMappingDecoded("/leads/new", "LeadListServlet");

    log.info("Starting Tomcat...");
    tomcat.start();
    tomcat.getServer().await();

    log.info("Tomcat started on port:{} ", port);
    log.info(" ");
  }
}