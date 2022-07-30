package webserver.http.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpRequest {
    public static final String TEMPLATE_PATH = "./templates";
    public static final String STATIC_PATH = "./static";
    private RequestLine requestLine;
    private RequestHeaders requestHeaders;
    private RequestBody requestBody;

//    public HttpRequest(String httpRequestData) {
//        String[] lines = httpRequestData.split("\n");
//        requestLine = new RequestLine(lines[0]);
//        requestHeaders = Arrays.stream(lines).filter(line -> !line.equals(lines[0])).collect(Collectors.toList());
//    }

    public HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders, RequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    public boolean isResource() {
        return requestLine.isResource();
    }

    public String responsePath() {
        return requestLine.fullPath();
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}
