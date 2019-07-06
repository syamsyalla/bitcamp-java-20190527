/*
 * This Java source file was generated by the Gradle 'init' task.
 */
// 애플리케이션 메인클래스
// 애플리케이션을 실행할때 이 클래스를 실행한다.
package com.eomcs.lms;

import java.util.Scanner;
import java.sql.Date;

public class App2 {

  static Scanner scan;

  public static void main(String[] args) {
    scan = new Scanner(System.in);

    int[] no = new int[100];
    String[] name = new String[100];
    String[] email = new String[100];
    String[] password = new String[100];
    String[] image = new String[100];
    String[] phone = new String[100];
    Date[] joinDate = new Date[100];

    int i = 0;
    for (i = 0; i < no.length; i++) {

      no[i] = getIntValue("번호?");
      name[i] = getStringValue("이름?");
      email[i] = getStringValue("이메일?");
      password[i] = getStringValue("암호?");
      image[i] = getStringValue("사진?");
      phone[i] = getStringValue("전화?");
      joinDate[i] = getDateValue("가입일?");

      System.out.println("계속입력하시겠습니까? (y/n)");
      String response = scan.nextLine();

      if (response.equals("n")) {
        break;
      }
    }
    for (int i2 = 0; i2 <= i; i2++) {
      System.out.printf("%s,%s,%s,%s,%s,%s,%s\n", no[i2], name[i2], email[i2], password[i2],
          image[i2], phone[i2], joinDate[i2]);
    }
    
    System.out.println();
  }

  private static Date getDateValue(String message) {
    while (true) {
      try {
        System.out.print(message);
        return Date.valueOf(scan.nextLine());
      } catch (IllegalArgumentException e) {
        System.out.println("2019-07-05 형식으로 입력하세요");
      }
    }
  }

  private static int getIntValue(String message) {
    while (true) {
      try {
        System.out.print(message);
        return Integer.parseInt(scan.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("숫자를 입력하세요");
      }
    }
  }

  private static String getStringValue(String message) {
    while (true) {
      System.out.print(message);
      return scan.nextLine();
    }
  }

}
