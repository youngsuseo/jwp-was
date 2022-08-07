package webserver.http.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class HttpRequest {

    private final RequestLine requestLine;
    private final RequestHeaders requestHeaders;
    private RequestBody requestBody;

    public HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        this.requestLine = new RequestLine(line);
        this.requestHeaders = new RequestHeaders(bufferedReader, line);
        this.requestBody = null;
        if (HttpMethod.isPost(requestLine.getMethod())) {
            this.requestBody = new RequestBody(bufferedReader, requestHeaders);
        }
    }

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        this.requestLine = new RequestLine(line);
        this.requestHeaders = new RequestHeaders(bufferedReader, line);
        this.requestBody = null;
        if (HttpMethod.isPost(requestLine.getMethod())) {
            this.requestBody = new RequestBody(bufferedReader, requestHeaders);
        }
    }

    public HttpRequest(String httpRequestText) {
        List<String> textList = Arrays.asList(httpRequestText.split("\n"));
        List<List<String>> requestInformation = requestHeaderAndBody(textList);

        this.requestLine = new RequestLine(textList.get(0));
        this.requestHeaders = new RequestHeaders(requestInformation.get(0));
        this.requestBody = null;
        if (HttpMethod.isPost(requestLine.getMethod())) {
            this.requestBody = new RequestBody(requestInformation.get(1));
        }
    }

    private List<List<String>> requestHeaderAndBody(List<String> textList) {
        return new ArrayList<>(textList.stream().filter(text -> !text.equals(textList.get(0)))
                .collect(Collectors.partitioningBy(line -> textList.indexOf(line) > textList.indexOf(""))).values());
    }

    public HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders, RequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.path();
    }

    public String getHeader(String header) {
        return requestHeaders.getRequestHeadersMap().get(header);
    }

    public String getParameter(String parameter) {
        String value = null;
        if (requestBody != null) {
            value = this.requestBody.getRequestBodyMap().get(parameter);
        }

        if (requestLine.getQueryStrings() != null && value == null) {
            return requestLine.getQueryStrings().queryStringValue(parameter);
        }
        return value;
    }

    public boolean isStaticResource() {
        return requestLine.isStaticResource();
    }

    public String responsePath() {
        return requestLine.fullPath();
    }

    public QueryStrings getQueryStrings() {
        return requestLine.getQueryStrings();
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders.getRequestHeadersMap();
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }
}