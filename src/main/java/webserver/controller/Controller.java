package webserver.controller;

import webserver.http.model.HttpRequest;
import webserver.http.model.HttpResponse;

import java.io.IOException;

public interface Controller {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}
