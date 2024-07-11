package util;

import static java.lang.System.out;

import java.util.Scanner;

public class UserInput {
    public static String getUsername() {
        Scanner scanner = new Scanner(System.in);
        out.print("Введіть логін: ");
        return scanner.nextLine();
    }

    public static String getPassword() {
        Scanner scanner = new Scanner(System.in);
        out.print("Введіть пароль: ");
        return scanner.nextLine();
    }
}
