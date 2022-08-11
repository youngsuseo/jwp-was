package webserver.http.model.httpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSessions {
    private Map<String, HttpSession> httpSessionMap;

    public HttpSessions() {
        this(new HashMap<>());
    }

    public HttpSessions(Map<String, HttpSession> httpSessionMap) {
        this.httpSessionMap = httpSessionMap;
    }

    public void create(HttpSession httpSession) {
        String uuid = UUID.randomUUID().toString();
        httpSessionMap.put(uuid, httpSession);
    }

    public Map<String, HttpSession> getHttpSessionMap() {
        return httpSessionMap;
    }
}
