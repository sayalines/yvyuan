package cn.iocoder.yudao.server.util;

public class ResponseResult {
    //成功
    public static Integer SUCCESS = 200;
    //未登录
    public static Integer NOLOGIN = 201;
    //失败
    public static Integer ERROR = 202;
    /**
     * 请求结果，201为失败，200为成功
     */
    private Integer code;


    /**
     * 请求结果信息
     */
    private String message;


    /**
     * 请求的返回数据对象，也将被转为json格式
     */
    private Object data;

    public ResponseResult() {}

    public ResponseResult(Integer code,String message,Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
