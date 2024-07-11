package util;

import static java.lang.System.out;

import java.util.Scanner;

public class UserInput {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getUsername() {
        out.print("Введіть логін: ");
        return scanner.nextLine();
    }

    public static String getPassword() {
        out.print("Введіть пароль: ");
        return scanner.nextLine();
    }
}
