
import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

public class StreamOperationsDemo {
    public static void main(String[] args) {
        /***********************Intermediate**********************/
        // 创建一个包含整数的集合
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 1, 2, 3, 4, 5);
        // filter: 过滤掉大于3的元素
        List<Integer> filteredList = numbers.stream()
                .filter(num -> num <= 3)
                .collect(Collectors.toList());
        System.out.println("Filtered List: " + filteredList);

        // map: 将每个元素乘以2
        List<Integer> mappedList = numbers.stream()
                .map(num -> num * 2)
                .collect(Collectors.toList());
        System.out.println("Mapped List: " + mappedList);
        
        // flatMap: 将每个元素转换成Stream对象，然后将所有的Stream连接成一个Stream
        List<String> words = Arrays.asList("Hello", "World");
        List<String> flatMappedList = words.stream()
                .flatMap(word -> Arrays.stream(word.split("")))
                .collect(Collectors.toList());
        System.out.println("FlatMapped List: " + flatMappedList);

        // distinct: 去除重复的元素
        List<Integer> distinctList = numbers.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Distinct List: " + distinctList);

        // sorted: 对元素进行排序
        List<Integer> sortedList = numbers.stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Sorted List: " + sortedList);

        // limit: 截取指定数量的元素
        List<Integer> limitedList = numbers.stream()
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("Limited List: " + limitedList);

        // skip: 跳过指定数量的元素
        List<Integer> skippedList = numbers.stream()
                .skip(3)
                .collect(Collectors.toList());
        System.out.println("Skipped List: " + skippedList);

        // peek: 对每个元素执行操作，不影响流中的其他元素
        List<Integer> peekedList = numbers.stream()
                .peek(num -> System.out.println("Peeking element: " + num))
                .collect(Collectors.toList());

        // takeWhile: 从开头开始连续取元素满足条件，直到遇到不满足条件的元素
        /*List<Integer> takenList = numbers.stream()
                .takeWhile(num -> num < 4)
                .collect(Collectors.toList());
        System.out.println("Taken List: " + takenList);*/

        // dropWhile: 从开头开始连续跳过元素满足条件，直到遇到不满足条件的元素
        /*List<Integer> droppedList = numbers.stream()
                .dropWhile(num -> num < 4)
                .collect(Collectors.toList());
        System.out.println("Dropped List: " + droppedList);*/
/*********************Terminal*********************/
        // collect: 将流转换为集合或其他数据结构
        List<Integer> collectedList = numbers.stream()
                .collect(Collectors.toList());
        System.out.println("Collected List: " + collectedList);

        // forEach: 遍历流中的元素，并对其执行操作
        numbers.stream()
                .forEach(System.out::println);

        // reduce: 使用给定的二元操作符将元素归约成一个值
        int sum = numbers.stream()
                .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        // max: 找出流中的最大值
        int max = numbers.stream()
                .max(Integer::compare)
                .orElse(-1);
        System.out.println("Max: " + max);

        // min: 找出流中的最小值
        int min = numbers.stream()
                .min(Integer::compare)
                .orElse(-1);
        System.out.println("Min: " + min);

        // toArray: 将流中的元素转换为数组
        Integer[] array = numbers.stream()
                .toArray(Integer[]::new);
        System.out.println("Array: " + Arrays.toString(array));

        // count: 统计流中的元素数量
        long count = numbers.stream()
                .count();
        System.out.println("Count: " + count);
/************************short-circuiting**********************/
        // findFirst: 返回满足条件的第一个元素
        int first = numbers.stream()
                .findFirst()
                .orElse(-1);
        System.out.println("First: " + first);

        // findAny: 返回任意满足条件的元素
        int any = numbers.stream()
                .findAny()
                .orElse(-1);
        System.out.println("Any: " + any);

        // anyMatch: 判断流中是否存在任意一个元素满足给定条件
        boolean anyMatch = numbers.stream()
                .anyMatch(num -> num % 2 == 0);
        System.out.println("Any Match: " + anyMatch);

        // allMatch: 判断流中所有元素是否都满足给定条件
        boolean allMatch = numbers.stream()
                .allMatch(num -> num % 2 == 0);
        System.out.println("All Match: " + allMatch);

        // noneMatch: 判断流中是否没有任何元素满足给定条件
        boolean noneMatch = numbers.stream()
                .noneMatch(num -> num > 10);
        System.out.println("None Match: " + noneMatch);
    }
}
