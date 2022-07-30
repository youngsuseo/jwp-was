package webserver.http.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestHeaders {
    private Map<String, String> requestHeadersMap;

    public RequestHeaders(String requestHeadersText) {
        String[] requestHeadersTexts = requestHeadersText.split("\n");
        requestHeadersMap = Arrays.stream(requestHeadersTexts)
                .collect(Collectors.toMap(header -> header.split(":")[0].trim(), header -> header.split(":")[1].trim()));
    }

    public Map<String, String> getRequestHeadersMap() {
        return requestHeadersMap;
    }
}
