package airport.cargos.com.spring.starter.constant;

public enum StateEnum {
    IS(1,"yes"),
    NOT(0,"no");
    private Integer code;
    private String message;
    StateEnum(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
