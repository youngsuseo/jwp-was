package model.http;

import exception.IllegalHttpRequestException;

import java.util.Objects;

public class Path {
    private final String path;

    public Path(String path) {
        if (!path.startsWith("/")) {
            throw new IllegalHttpRequestException("path는 /를 시작으로 하여 경로를 나타냅니다.");
        }
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path1 = (Path) o;
        return Objects.equals(path, path1.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
