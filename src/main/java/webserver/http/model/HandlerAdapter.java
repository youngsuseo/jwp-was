package webserver.http.model;

import webserver.http.controller.UserController;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public enum HandlerAdapter {
    CREATE_USER_GET(Method.GET, "/user/create", new UserController(), "createUserGet", false), // FIXME new 생성자 대신 싱글턴 패턴으로 가능한지 확인
    CREATE_USER_POST(Method.POST, "/user/create", new UserController(), "createUserPost", false),
    LOGIN(Method.POST, "/user/login", new UserController(), "login", false),
    USER_LIST(Method.GET, "/user/list", new UserController(), "retrieveUsers", true),
    INDEX(Method.GET, "/index.html", new UserController(), "index", false); // FIXME 없는 메서드

    private Method httpMethod;
    private String path;
    private Object classObject;
    private String methodName;
    private boolean accessibleAfterLogin;

    HandlerAdapter(Method httpMethod, String path, Object classObject, String methodName, boolean accessibleAfterLogin) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.classObject = classObject;
        this.methodName = methodName;
        this.accessibleAfterLogin = accessibleAfterLogin;
    }

    public static Object handlerMapping(HttpRequest httpRequest) {
        HandlerAdapter handlerAdapter = Arrays.stream(values()).filter(handlerAdapterEnum -> handlerAdapterEnum.httpMethod == httpRequest.getRequestLine().getMethod())
                .filter(handlerAdapterEnum -> handlerAdapterEnum.path.equals(httpRequest.getRequestLine().getPathInformation().getPath().getPath()))
                .findFirst().orElse(HandlerAdapter.INDEX);

        Object instance = handlerAdapter.classObject;
        Class<?> instanceClass = instance.getClass();
        try { // FIXME 우선 try-catch 로 감쌈
            java.lang.reflect.Method declaredMethod = instanceClass.getDeclaredMethod(handlerAdapter.methodName, HttpRequest.class);
            return declaredMethod.invoke(instance, httpRequest);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean accessiblePagesAfterLogin(HttpRequest httpRequest) {
        return Arrays.stream(values()).filter(handlerAdapterEnum -> handlerAdapterEnum.httpMethod == httpRequest.getRequestLine().getMethod())
                .filter(handlerAdapterEnum -> handlerAdapterEnum.path.equals(httpRequest.getRequestLine().getPathInformation().getPath().getPath()))
                .map(handlerAdapterEnum -> handlerAdapterEnum.accessibleAfterLogin).findFirst().orElse(true);
    }
}
