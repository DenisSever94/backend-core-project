package ru.mentee.power.crm.domain;

public enum LeadStatus {
  NEW, QUALIFIED, CONVERTED, INVALID, CONTACTED;

  public static boolean isValid(String status) {
    try {
      valueOf(status);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
