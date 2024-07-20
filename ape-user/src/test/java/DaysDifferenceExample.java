import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DaysDifferenceExample {
    public static void main(String[] args) {
        // 假设 start 和 end 是表示日期的字符串
        String startStr = "2023-01-01";
        String endStr = "2023-01-10";

        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 将字符串转换为 LocalDate 对象
        LocalDate startDate = LocalDate.parse(startStr, formatter);
        LocalDate endDate = LocalDate.parse(endStr, formatter);

        // 计算日期差
        long daysDifference = endDate.toEpochDay() - startDate.toEpochDay();

        // 打印结果
        System.out.println("The difference between end and start is " + daysDifference + " days.");
    }
}
