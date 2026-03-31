package ca.ulaval.glo2003.persistence;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DockerSmokeTest {

  @Test
  void shouldStartContainer() {
    try (GenericContainer<?> container = new GenericContainer<>("mongo:8.0")
        .withExposedPorts(27017)) {
      container.start();
      assertTrue(container.isRunning());
    }
  }
}