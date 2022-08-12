package webserver.controller;

import db.DataBase;
import webserver.http.model.httpSession.HttpSession;
import webserver.http.model.httpSession.HttpSessions;
import webserver.http.model.request.HttpRequest;
import webserver.http.model.response.HttpResponse;
import webserver.http.model.response.Model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ListUserController extends AbstractController {

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.moveNotStaticResourcePage(httpResponse, dispatch(httpRequest));
    }

    private Model dispatch(HttpRequest httpRequest) {
        if (isLogin(httpRequest)) {
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("users", DataBase.findAll());
            return new Model("user/list", modelMap);
        }
        return new Model("/user/login.html", null);
    }

    public boolean isLogin(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeader("Cookie");
        if (cookie == null) {
            return false;
        }

        String[] split = cookie.split("=");
        if (split.length < 2) {
            return false;
        }

        if (!split[0].equals("JSESSIONID")) {
            return false;
        }

        String jsessionid = split[1];
        HttpSession httpSession = HttpSessions.getHttpSessionMap().get(jsessionid);
        String loginResult = (String) httpSession.getAttribute("login");
        return "true".equals(loginResult);
    }
}
