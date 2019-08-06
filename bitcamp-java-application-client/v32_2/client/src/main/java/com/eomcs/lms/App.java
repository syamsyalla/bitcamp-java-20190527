// client-v32_2 : 데이터 관리를 서버에게 맡긴다.
package com.eomcs.lms;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import com.eomcs.lms.client.BoardDaoProxy;
import com.eomcs.lms.client.LessonDaoProxy;
import com.eomcs.lms.client.MemberDaoProxy;
import com.eomcs.lms.dao.BoardDao;
import com.eomcs.lms.dao.LessonDao;
import com.eomcs.lms.dao.MemberDao;
import com.eomcs.lms.handler.BoardAddCommand;
import com.eomcs.lms.handler.BoardDeleteCommand;
import com.eomcs.lms.handler.BoardDetailCommand;
import com.eomcs.lms.handler.BoardListCommand;
import com.eomcs.lms.handler.BoardUpdateCommand;
import com.eomcs.lms.handler.CalcPlusCommand;
import com.eomcs.lms.handler.Command;
import com.eomcs.lms.handler.HiCommand;
import com.eomcs.lms.handler.LessonAddCommand;
import com.eomcs.lms.handler.LessonDeleteCommand;
import com.eomcs.lms.handler.LessonDetailCommand;
import com.eomcs.lms.handler.LessonListCommand;
import com.eomcs.lms.handler.LessonUpdateCommand;
import com.eomcs.lms.handler.MemberAddCommand;
import com.eomcs.lms.handler.MemberDeleteCommand;
import com.eomcs.lms.handler.MemberDetailCommand;
import com.eomcs.lms.handler.MemberListCommand;
import com.eomcs.lms.handler.MemberUpdateCommand;
import com.eomcs.util.Input;

public class App {

  Scanner keyScan;

  private void service() {

    try (Socket socket = new Socket("localhost", 8888);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      // Command 객체가 사용할 데이터 처리 객체를 준비한다.
      BoardDao boardDao = new BoardDaoProxy(in, out);
      LessonDao lessonDao = new LessonDaoProxy(in, out);
      MemberDao memberDao = new MemberDaoProxy(in, out);

      keyScan = new Scanner(System.in);

      Deque<String> commandStack = new ArrayDeque<>(); // Stack 대신
      Queue<String> commandQueue = new LinkedList<>(); // Queue 대신

      Input input = new Input(keyScan);

      HashMap<String, Command> commandMap = new HashMap<>();

      commandMap.put("/lesson/add", new LessonAddCommand(input, lessonDao));
      commandMap.put("/lesson/delete", new LessonDeleteCommand(input, lessonDao));
      commandMap.put("/lesson/detail", new LessonDetailCommand(input, lessonDao));
      commandMap.put("/lesson/list", new LessonListCommand(input, lessonDao));
      commandMap.put("/lesson/update", new LessonUpdateCommand(input, lessonDao));

      commandMap.put("/board/add", new BoardAddCommand(input, boardDao));
      commandMap.put("/board/delete", new BoardDeleteCommand(input, boardDao));
      commandMap.put("/board/detail", new BoardDetailCommand(input, boardDao));
      commandMap.put("/board/list", new BoardListCommand(input, boardDao));
      commandMap.put("/board/update", new BoardUpdateCommand(input, boardDao));

      commandMap.put("/member/add", new MemberAddCommand(input, memberDao));
      commandMap.put("/member/delete", new MemberDeleteCommand(input, memberDao));
      commandMap.put("/member/detail", new MemberDetailCommand(input, memberDao));
      commandMap.put("/member/list", new MemberListCommand(input, memberDao));
      commandMap.put("/member/update", new MemberUpdateCommand(input, memberDao));

      commandMap.put("/hi", new HiCommand(input));
      commandMap.put("/calc/plus", new CalcPlusCommand(input));

      while (true) {

        String command = prompt();

        if (command.length() == 0)
          continue;

        commandStack.push(command); // 사용자가 입력한 명령을 보관한다.
        commandQueue.offer(command); // 사용자가 입력한 명령을 보관한다.

        Command executor = commandMap.get(command);

        if (command.equals("quit")) {
          out.writeUTF(command);
          out.flush();
          break;

        } else if (command.equals("history")) {
          printCommandHistory(commandStack);

        } else if (command.equals("history2")) {
          printCommandHistory(commandQueue);

        } else if (executor != null) {
          executor.execute(); // addLesson 메서드 블록에 묶어 놓은 코드를 실행한다.

        } else {
          System.out.println("해당 명령을 지원하지 않습니다!");

        }
        System.out.println();
      } // while


    } catch (Exception e) {
      System.out.println("서버통신오류");
    }
  }

  private void printCommandHistory(Iterable<String> list) {
    Iterator<String> iterator = list.iterator();
    int count = 0;
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
      if (++count % 5 == 0) {
        System.out.println(":");
        if (keyScan.nextLine().equalsIgnoreCase("q"))
          break;

      }
    }
  }



  private String prompt() {
    System.out.print("명령> ");
    return keyScan.nextLine();
  }

  public static void main(String[] args) {
    App app = new App();
    app.service();
  }
}
