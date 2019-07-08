package com.eomcs.lms.handler;

import java.util.Scanner;
import com.eomcs.lms.domain.Member;
import com.eomcs.lms.util.Input;

public class MemberHandler {
  private static Member[] members = new Member[100];
  private static int memberSize = 0;
  public static Scanner keyScan;

  public static void listMember() {
    for (int i = 0; i < memberSize; i++) {
      Member member = members[i];
      System.out.printf("%s,%s,%s,%s,%s,%s,%s\n", member.no, member.name, member.email,
          member.password, member.photo, member.tel, member.registeredDate);
    }
  }

  public static void addMember() {

    Member member = new Member();

    member.no = Input.getIntValue("번호?");
    member.name = Input.getStringValue("이름?");
    member.email = Input.getStringValue("이메일?");
    member.password = Input.getStringValue("암호?");
    member.photo = Input.getStringValue("사진?");
    member.tel = Input.getStringValue("전화?");
    member.registeredDate = Input.getDateValue("가입일?");

    members[memberSize++] = member;

    System.out.println("저장했습니다.");

  }

}
