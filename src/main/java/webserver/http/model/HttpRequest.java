package webserver.http.model;

import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpRequest {
    public static final String TEMPLATE_PATH = "./templates";
    public static final String STATIC_PATH = "./static";
    private RequestLine requestLine;
    private RequestHeaders requestHeaders;
    private RequestBody requestBody;

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        this.requestLine = new RequestLine(line);
        this.requestHeaders = new RequestHeaders(bufferedReader, line);
        this.requestBody = null;
        if (requestLine.getMethod() == Method.POST) { // FIXME getter 대신 메시지
            this.requestBody = new RequestBody(bufferedReader, requestHeaders);
        } // FIXME this() : 주 생성자로 호출 가능할지?
//        new HttpRequest(requestLine, requestHeaders, requestBody); // FIXME 왜 호출이 안될까?
    }

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
