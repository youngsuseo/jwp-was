package webserver.http.model;

import exception.IllegalHttpRequestException;

import java.util.Objects;

public class RequestLine {
    private static final int VALID_REQUEST_LINE_LENGTH = 3;

    private Method method;
    private PathInformation pathInformation;
    private ProtocolVersion protocolVersion;

    public RequestLine(String requestLine) {
        String[] requestData = requestLine.split(" ");

        if (requestData.length != VALID_REQUEST_LINE_LENGTH) {
            throw new IllegalHttpRequestException("RequestLine은 method, pathInformation, protocol을 공백 기준으로 나누어 전송해야 합니다.");
        }

        this.method = Method.of(requestData[0]);
        this.pathInformation = new PathInformation(requestData[1]);
        this.protocolVersion = new ProtocolVersion(requestData[2]);
    }

    public String path() {
        return pathInformation.path();
    }

    public Method getMethod() {
        return method;
    }

    public PathInformation getPathInformation() {
        return pathInformation;
    }

    public ProtocolVersion getProtocol() {
        return protocolVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestLine that = (RequestLine) o;
        return method == that.method && Objects.equals(pathInformation, that.pathInformation) && Objects.equals(protocolVersion, that.protocolVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, pathInformation, protocolVersion);
    }
}
