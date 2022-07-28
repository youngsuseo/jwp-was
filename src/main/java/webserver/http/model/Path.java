package webserver.http.model;

import java.util.Objects;

public class Path {
    private String path;

    public Path(String path) {
        this.path = path;
    }

    public boolean isResource() {
        return path.split("\\.").length > 1;
    }

    public String resource() {
        if (!isResource()) {
            return path;
        }
        String[] splitPath = path.split("\\.");
        String extension = splitPath[splitPath.length - 1];
        return Extension.parentPath(extension) + path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path1 = (Path) o;
        return Objects.equals(path, path1.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}