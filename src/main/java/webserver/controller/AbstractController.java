package webserver.controller;

import webserver.http.model.HttpMethod;
import webserver.http.model.HttpRequest;
import webserver.http.model.HttpResponse;

import java.io.IOException;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        if (HttpMethod.isPost(request.getMethod())) {
            doPost(request, response);
            return;
        }
        doGet(request, response);
    }

    public void doPost(HttpRequest request, HttpResponse response) throws IOException {

    }

    public void doGet(HttpRequest request, HttpResponse response) throws IOException {

    }
}
