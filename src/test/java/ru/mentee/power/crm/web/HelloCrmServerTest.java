package ru.mentee.power.crm.web;


import static org.mockito.Mockito.*;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class HelloCrmServerTest {

  @Mock
  private HttpServer mockHttpServer;
  private HelloCrmServer server;

  @BeforeEach
  void setUp() {
    server = new HelloCrmServer(8080, mockHttpServer);
  }

  @Test
  void shouldCreatedContextAndStartServer() throws IOException {
    server.start();
    verify(mockHttpServer).createContext(eq("/hello"), any(HelloCrmServer.HelloHandler.class));
    verify(mockHttpServer).start();
  }

  @Test
  void shouldStopServerWithZeroDelay() throws IOException {
    server.stop();
    verify(mockHttpServer).stop(0);
  }
}