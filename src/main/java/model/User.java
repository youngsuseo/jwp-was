package model;

import webserver.http.model.QueryString;
import webserver.http.model.QueryStrings;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(QueryStrings queryStrings) {
        for (QueryString queryString : queryStrings.getQueryStringList()) {
            // FIXME s 이 부분 수정 필요
        }
    }

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
