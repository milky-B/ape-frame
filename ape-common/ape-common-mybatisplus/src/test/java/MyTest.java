import java.util.Scanner;

public class MyTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            char a = scanner.next().charAt(0);
            if('a'<=a&&a<='z'){
                System.out.println((char)('A'+a-'a'));
            }
            if('A'<=a&&a<='Z'){
                System.out.println((char)('a'+a-'A'));
            }
        }
    }
}
