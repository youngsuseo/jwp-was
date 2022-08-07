package webserver.http.model;

public class StatusLine {
    private Protocol protocol;
    private String statusCode;
    private String status;

    public StatusLine(Protocol protocol, String statusCode, String status) {
        this.protocol = protocol;
        this.statusCode = statusCode;
        this.status = status;
    }
}
