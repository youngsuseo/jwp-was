package webserver.controller;

import db.DataBase;
import webserver.http.model.HandlerAdapter;
import webserver.http.model.HttpRequest;
import webserver.http.model.HttpResponse;
import webserver.http.model.Model;

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
        return HandlerAdapter.accessiblePagesAfterLogin(httpRequest) && "logined=true".equals(httpRequest.getRequestHeaders().get("Cookie"));
    }
}
