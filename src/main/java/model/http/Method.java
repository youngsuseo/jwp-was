package model.http;

public enum Method {
    GET;

    public static Method of(String methodName) {
        return valueOf(methodName);
    }
}
