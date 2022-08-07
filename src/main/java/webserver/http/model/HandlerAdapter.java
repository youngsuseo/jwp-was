package webserver.http.model;

import webserver.controller.UserController;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public enum HandlerAdapter {
    CREATE_USER_GET(HttpMethod.GET, "/user/create", UserController.of(), "createUserGet", false),
    CREATE_USER_POST(HttpMethod.POST, "/user/create", UserController.of(), "createUserPost", false),
    LOGIN(HttpMethod.POST, "/user/login", UserController.of(), "login", false),
    USER_LIST(HttpMethod.GET, "/user/list", UserController.of(), "retrieveUsers", true),
    INDEX(HttpMethod.GET, "/index.html", UserController.of(), "index", false);

    private HttpMethod httpMethod;
    private String path;
    private Object classObject;
    private String methodName;
    private boolean accessibleAfterLogin;

    HandlerAdapter(HttpMethod httpMethod, String path, Object classObject, String methodName, boolean accessibleAfterLogin) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.classObject = classObject;
        this.methodName = methodName;
        this.accessibleAfterLogin = accessibleAfterLogin;
    }

    public static Object handlerMapping(HttpRequest httpRequest) {
        HandlerAdapter handlerAdapter = Arrays.stream(values()).filter(handlerAdapterEnum -> handlerAdapterEnum.httpMethod == httpRequest.getRequestLine().getMethod())
                .filter(handlerAdapterEnum -> handlerAdapterEnum.path.equals(httpRequest.getPath()))
                .findFirst().orElse(HandlerAdapter.INDEX);

        Object instance = handlerAdapter.classObject;
        Class<?> instanceClass = instance.getClass();
        try {
            java.lang.reflect.Method declaredMethod = instanceClass.getDeclaredMethod(handlerAdapter.methodName, HttpRequest.class);
            return declaredMethod.invoke(instance, httpRequest);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean accessiblePagesAfterLogin(HttpRequest httpRequest) {
        return Arrays.stream(values()).filter(handlerAdapterEnum -> handlerAdapterEnum.httpMethod == httpRequest.getRequestLine().getMethod())
                .filter(handlerAdapterEnum -> handlerAdapterEnum.path.equals(httpRequest.getPath()))
                .map(handlerAdapterEnum -> handlerAdapterEnum.accessibleAfterLogin).findFirst().orElse(true);
    }
}
