package mvc.controller;

import mvc.view.TemplateViewResolver;
import mvc.view.View;
import was.http.HttpMethod;
import was.http.HttpRequest;
import was.http.HttpResponse;

public abstract class AbstractController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        HttpMethod method = request.getMethod();

        if (method.isPost()) {
            doPost(request, response);
        } else {
            doGet(request, response);
        }
    }

    protected void doPost(HttpRequest request, HttpResponse response) throws Exception {
    }

    protected void doGet(HttpRequest request, HttpResponse response) throws Exception {
    }

    protected void render(HttpRequest request, HttpResponse response, String viewName) throws Exception {
        TemplateViewResolver viewResolver = new TemplateViewResolver();
        View view = viewResolver.resolveViewName(viewName);
        view.render(request, response);
    }
}