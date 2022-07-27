package webserver.http.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    @DisplayName("html 페이지는 Resource 입니다.")
    @Test
    void isResource_html() {
        Path path = new Path("/index.html");
        assertThat(path.isResource()).isTrue();
    }

    @DisplayName("확장자명이 없다면 Resource가 아닙니다.")
    @Test
    void isResource_action() {
        Path path = new Path("/user/create");
        assertThat(path.isResource()).isFalse();
    }

    @Test
    void resource() {
        Path path = new Path("/index.html");
        assertThat(path.getPath()).
    }
}