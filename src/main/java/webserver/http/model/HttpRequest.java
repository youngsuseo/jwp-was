package webserver.http.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpRequest {
    public static final String TEMPLATE_PATH = "./templates";
    public static final String STATIC_PATH = "./static";
    private RequestLine requestLine;
    private List<String> requestHeaders;
    private String requestBody;

    public HttpRequest(String httpRequestData) {
        String[] lines = httpRequestData.split("\n");
        requestLine = new RequestLine(lines[0]);
        requestHeaders = Arrays.stream(lines).filter(line -> !line.equals(lines[0])).collect(Collectors.toList());
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public List<String> getRequestHeaders() {
        return requestHeaders;
    }

    public String responsePath() {
        return requestLine.fullPath();
    }
}
