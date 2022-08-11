package webserver.controller;

import cookie.Cookie;
import db.DataBase;
import org.checkerframework.checker.units.qual.C;
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
        Model model;
        if (isLogin(httpRequest)) {
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("users", DataBase.findAll());
            model = new Model("user/list", modelMap);
        } else {
            model = new Model("/user/login.html", null);
        }
        httpResponse.moveNotStaticResourcePage(httpResponse, model);
    }

    public boolean isLogin(HttpRequest httpRequest) {
        String jsessionid = httpRequest.getHeader("JSESSIONID");
        HttpSessions httpSessions = new HttpSessions();
        HttpSession httpSession = httpSessions.getHttpSessionMap().get(jsessionid);
        String loginResult = (String) httpSession.getAttribute("login");
        return "true".equals(loginResult);
    }
}
