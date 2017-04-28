package com.example.easyhttp.http.exception;

public class ApiException extends RuntimeException {
    public static final int Code_TimeOut = 1000;
    public static final int Code_UnConnected = 1001;
    public static final int Code_MalformedJson = 1020;
    public static final int Code_Default = 1003;
    public static final String CONNECT_EXCEPTION = "网络连接异常，请检查您的网络状态";
    public static final String SOCKET_TIMEOUT_EXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";
    public static final String MALFORMED_JSON_EXCEPTION = "数据解析错误";

    public ApiException(int resultCode, String msg) {
        this(getApiExceptionMessage(resultCode, msg));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code, String msg) {
        String message;
        switch (code) {
            default:
                message = code + "#" + msg;
        }
        return message;
    }
}

