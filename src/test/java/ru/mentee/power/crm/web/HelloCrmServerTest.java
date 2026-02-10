package ru.mentee.power.crm.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HelloCrmServerTest {

  @Mock
  private HttpServer mockServer;

  @Test
  void shouldCreateServerWithCorrectPortWhenConstructorIsCalled() throws IOException {
    int expectedPort = 8080;
    HelloCrmServer helloServer = new HelloCrmServer(expectedPort);
    assertThat(helloServer).isNotNull();
  }

  @Test
  void shouldAcceptMockServerWhenTestConstructorIsCalled() {
    int testPort = 9090;
    HelloCrmServer helloServer = new HelloCrmServer(testPort, mockServer);
    assertThat(helloServer).isNotNull();
  }

  @Test
  void shouldStartServerAndRegisterContextWhenStartIsCalled() {
    HelloCrmServer helloServer = new HelloCrmServer(8080, mockServer);
    helloServer.start();
    verify(mockServer).createContext(eq("/hello"), any(HelloCrmServer.HelloHandler.class));
    verify(mockServer).start();
  }

  @Test
  void shouldStopServerWithZeroDelayWhenStopIsCalled() {
    HelloCrmServer helloServer = new HelloCrmServer(8080, mockServer);
    helloServer.stop();
    verify(mockServer).stop(0);
  }
}