package airport.cargos.com.spring.starter.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
    private Long total;

    public static <T> Result<T> success() {
        return new Result(0, "success", (Object) null, (Long) null);
    }

    public static <T> Result<T> success(T data) {
        return new Result(0, "success", data, (Long) null);
    }

    public static <T> Result<T> success(T data, Long total) {
        return new Result(0, "success", data, total);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result(0, msg, data, (Long) null);
    }

    public static <T> Result<T> fail() {
        return new Result(1, "fail", (Object) null, (Long) null);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result(1, msg, (Object) null, (Long) null);
    }

}
