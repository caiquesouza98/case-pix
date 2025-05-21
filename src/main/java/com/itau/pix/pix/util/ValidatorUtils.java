package com.itau.pix.pix.util;

public class ValidatorUtils {

  public static boolean isValidEmail(String email) {
    String pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    return email != null && email.matches(pattern);
  }

  public static boolean isValidPhone(String phone) {
    String pattern = "^(\\+55)?\\s?\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$";
    return phone != null && phone.matches(pattern);
  }

  public static boolean isValidCPF(String cpf) {
    if (cpf == null || !cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) {
      return false;
    }

    int sum1 = 0, sum2 = 0;
    for (int i = 0; i < 9; i++) {
      int digit = Character.getNumericValue(cpf.charAt(i));
      sum1 += digit * (10 - i);
      sum2 += digit * (11 - i);
    }

    int check1 = sum1 % 11 < 2 ? 0 : 11 - (sum1 % 11);
    sum2 += check1 * 2;
    int check2 = sum2 % 11 < 2 ? 0 : 11 - (sum2 % 11);

    return check1 == Character.getNumericValue(cpf.charAt(9)) &&
            check2 == Character.getNumericValue(cpf.charAt(10));
  }

  public static boolean isValidCNPJ(String cnpj) {
    if (cnpj == null || !cnpj.matches("\\d{14}") || cnpj.chars().distinct().count() == 1) {
      return false;
    }

    int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    int sum1 = 0, sum2 = 0;

    for (int i = 0; i < 12; i++) {
      int digit = Character.getNumericValue(cnpj.charAt(i));
      sum1 += digit * weights1[i];
      sum2 += digit * weights2[i];
    }

    int check1 = sum1 % 11 < 2 ? 0 : 11 - (sum1 % 11);
    sum2 += check1 * weights2[12];
    int check2 = sum2 % 11 < 2 ? 0 : 11 - (sum2 % 11);

    return check1 == Character.getNumericValue(cnpj.charAt(12)) &&
            check2 == Character.getNumericValue(cnpj.charAt(13));
  }

}
