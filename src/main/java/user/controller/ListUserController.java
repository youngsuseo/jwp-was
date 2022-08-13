package user.controller;

import user.service.RetrieveUserService;
import webserver.http.model.session.HttpSession;
import webserver.http.model.session.HttpSessions;
import webserver.http.model.request.HttpRequest;
import webserver.http.model.response.HttpResponse;
import webserver.http.model.response.Model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ListUserController extends AbstractController {

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.movePage(dispatch(httpRequest));
    }

    private Model dispatch(HttpRequest httpRequest) {
        if (isLogin(httpRequest)) {
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("users", RetrieveUserService.retrieveUsers());
            return new Model("user/list", modelMap);
        }
        return new Model("/user/login.html", null);
    }

    private boolean isLogin(HttpRequest httpRequest) {
        boolean accessiblePagesAfterLogin = ControllerEnum.accessiblePagesAfterLogin(httpRequest);

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

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {

    }
}
