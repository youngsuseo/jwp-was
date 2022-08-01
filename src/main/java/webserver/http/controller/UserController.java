package webserver.http.controller;

import db.DataBase;
import model.User;
import webserver.http.model.HttpRequest;

import javax.servlet.http.Cookie;

public class UserController {

    public String index(HttpRequest httpRequest) {
        return "/index.html";
    }

    public String createUserGet(HttpRequest httpRequest) {
        User user = new User(httpRequest.getRequestLine().getPathInformation().getQueryStrings());
        System.out.println("user = " + user);
        return "/index.html";
    }

    public String createUserPost(HttpRequest httpRequest) {
        User user = new User(httpRequest.getRequestBody());
        DataBase.addUser(user);
        System.out.println("user = " + user);
        return "/index.html";
    }

    public String login(HttpRequest httpRequest) {
        String userId = httpRequest.getRequestBody().getRequestBodyMap().get("userId");
        String password = httpRequest.getRequestBody().getRequestBodyMap().get("password");
        User user = DataBase.findUserById(userId);

        // 로그인 성공
        // 로그인 성공시 cookie header 값이 logined=true
        if (user.getPassword().equals(password)) {
            return "/index.html";
        } else {
            // 로그인 실패
            // 로그인 실패시 cookie header 값이 logined=false

            return "/user/login_fail.html";
        }


    }
}
