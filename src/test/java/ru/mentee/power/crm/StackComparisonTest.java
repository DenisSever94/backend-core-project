package ru.mentee.power.crm;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class StackComparisonTest {

  private static final int SERVLET_PORT = 8080;
  private static final int SPRING_PORT = 8081;
  private HttpClient httpClient;

  @BeforeEach
  void setUp() {
    httpClient = HttpClient.newHttpClient();
  }

  @Test
  void shouldReturnLeadsFromBothStacks() throws Exception {
    HttpRequest servletRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + SERVLET_PORT + "/leads"))
        .GET()
        .build();

    HttpRequest springRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + SPRING_PORT + "/leads"))
        .GET()
        .build();

    HttpResponse<String> servletResponse = httpClient.send(
        servletRequest, HttpResponse.BodyHandlers.ofString());

    HttpResponse<String> springResponse = httpClient.send(
        springRequest, HttpResponse.BodyHandlers.ofString());

    assertThat(servletResponse.statusCode()).isEqualTo(200);
    assertThat(springResponse.statusCode()).isEqualTo(200);

    assertThat(servletResponse.body()).contains("<table");
    assertThat(springResponse.body()).contains("<table");

    Document servletDoc = Jsoup.parse(servletResponse.body());
    Document springDoc = Jsoup.parse(springResponse.body());

    int servletRowCount = servletDoc.select("table tr").size();
    int springRowCount = springDoc.select("table tr").size();

    assertThat(servletRowCount).isEqualTo(springRowCount);
  }

  @Test
  void shouldMeasureStartupTime() throws LifecycleException {
    long servletStartupMs = measureServletStartup();
    long springStartupMs = measureSpringBootStartup();

    System.out.println("=== Сравнение времени старта ===");
    System.out.printf("Servlet стек: %d ms%n", servletStartupMs);
    System.out.printf("Spring Boot: %d ms%n", springStartupMs);
    System.out.printf("Разница: Spring %s на %d ms%n",
        springStartupMs > servletStartupMs ? "медленнее" : "быстрее",
        Math.abs(springStartupMs - servletStartupMs));

    assertThat(servletStartupMs).isLessThan(10_000L);
    assertThat(springStartupMs).isLessThan(15_000L);
  }

  private long measureServletStartup() throws LifecycleException {
    long start = System.nanoTime();
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(0);
    tomcat.getConnector();

    Context context = tomcat.addContext("", new File(".").getAbsolutePath());
    Tomcat.addServlet(context, "health", new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(200);
      }
    });

    context.addServletMappingDecoded("/health", "health");

    try {
      tomcat.start();
      long stop = System.nanoTime();
      long durationNanos = stop - start;
      long durationMillis = durationNanos / 1_000_000;
      tomcat.stop();
      tomcat.destroy();
      return durationMillis;
    } catch (LifecycleException e) {
      try {
        tomcat.stop();
      } catch (Exception _) {
      }
      try {
        tomcat.destroy();
      } catch (Exception _) {
      }
      throw e;
    }
  }

  private long measureSpringBootStartup() {
    long start = System.nanoTime();
    SpringApplication app = new SpringApplication(Application.class);
    app.setDefaultProperties(Collections.singletonMap("server.port", "0"));
    ConfigurableApplicationContext context = app.run();
    long stop = System.nanoTime();
    context.close();
    return TimeUnit.NANOSECONDS.toMillis(stop - start); // Заглушка
  }
}

