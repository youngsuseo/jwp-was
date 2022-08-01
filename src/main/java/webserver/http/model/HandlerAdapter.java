package webserver.http.model;

import webserver.http.controller.UserController;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public enum HandlerAdapter {
    CREATE_USER_GET(Method.GET, "/user/create", new UserController(), "createUserGet"), // FIXME new 생성자 대신 싱글턴 패턴으로 가능한지 확인
    CREATE_USER_POST(Method.POST, "/user/create", new UserController(), "createUserPost"),
    LOGIN(Method.POST, "/user/login", new UserController(), "login"),
    INDEX(Method.GET, "/index.html", new UserController(), "index"); // FIXME 없는 메서드

    private Method httpMethod;
    private String path;
    private Object classObject;
    private String methodName;

    HandlerAdapter(Method httpMethod, String path, Object classObject, String methodName) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.classObject = classObject;
        this.methodName = methodName;
    }

    public static String handlerMapping(HttpRequest httpRequest) {
        HandlerAdapter handlerAdapter = Arrays.stream(values()).filter(handlerAdapterEnum -> handlerAdapterEnum.httpMethod == httpRequest.getRequestLine().getMethod())
                .filter(handlerAdapterEnum -> handlerAdapterEnum.path.equals(httpRequest.getRequestLine().getPathInformation().getPath().getPath()))
                .findFirst().orElse(HandlerAdapter.INDEX);

        Object instance = handlerAdapter.classObject;
        Class<?> instanceClass = instance.getClass();
        try { // FIXME 우선 try-catch 로 감쌈
            java.lang.reflect.Method declaredMethod = instanceClass.getDeclaredMethod(handlerAdapter.methodName, HttpRequest.class);
            Object invoked = declaredMethod.invoke(instance, httpRequest);
            return Extension.fullPath((String) invoked);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
