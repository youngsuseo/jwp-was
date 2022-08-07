package webserver.controller;

import db.DataBase;
import model.User;
import webserver.http.model.HttpRequest;
import webserver.http.model.HttpResponse;

import java.io.IOException;

public class CreateUserController extends AbstractController {

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        DataBase.addUser(new User(httpRequest.getRequestBody()));
        httpResponse.moveNotStaticResourcePage(httpResponse, "/index.html");
    }
}
