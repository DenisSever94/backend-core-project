package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.Test;

class FieldInjectionProblemTest {

  @Test
  void fieldInjectionCausesNullPointerWithoutSpring() {
    DemoController demoController = new DemoController(null);
    demoController.demo();
  }
}
