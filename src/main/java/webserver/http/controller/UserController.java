package webserver.http.controller;

import model.User;
import webserver.http.model.HttpRequest;

public class UserController {

    public String createUserGet(HttpRequest httpRequest) {
        User user = new User(httpRequest.getRequestLine().getPathInformation().getQueryStrings());
        System.out.println("user = " + user);
        return "/index.html";
    }

    public String createUserPost(HttpRequest httpRequest) {
        User user = new User(httpRequest.getRequestBody());
        System.out.println("user = " + user);
        return "/index.html";
    }
}
