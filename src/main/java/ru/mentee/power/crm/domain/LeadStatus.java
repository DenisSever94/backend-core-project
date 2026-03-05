package ru.mentee.power.crm.domain;

public enum LeadStatus {
  NEW("НОВЫЙ"),
  QUALIFIED("КВАЛИФИЦИРОВАННЫЙ"),
  CONTACTED("СВЯЗАТЬСЯ");

  private String name;

  LeadStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static boolean isValid(String status) {
    try {
      valueOf(status);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
