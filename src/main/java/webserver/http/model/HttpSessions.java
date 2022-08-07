package webserver.http.model;

import java.util.Map;

public class HttpSessions {
    private Map<String, HttpSession> httpSessionMap;

    public HttpSessions(Map<String, HttpSession> httpSessionMap) {
        this.httpSessionMap = httpSessionMap;
    }

    public Map<String, HttpSession> getHttpSessionMap() {
        return httpSessionMap;
    }
}
