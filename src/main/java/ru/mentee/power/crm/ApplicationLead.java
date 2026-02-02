package ru.mentee.power.crm;

import lombok.extern.slf4j.Slf4j;
import ru.mentee.power.crm.web.HelloCrmServer;

@Slf4j
public class ApplicationLead {
  static void main() throws Exception {

    int port = 8080;
    HelloCrmServer server = new HelloCrmServer(port);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Stopping server...");
      server.stop();
    }));

    server.start();
    Thread.currentThread().join();
  }
}