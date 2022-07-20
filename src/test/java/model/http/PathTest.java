package model.http;

import exception.IllegalHttpRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathTest {

    @DisplayName("path는 /를 시작으로하여 resource를 표기한다.")
    @Test
    void construct_notStartWithSeparator() {
        assertThatThrownBy(() -> new Path("users")).isInstanceOf(IllegalHttpRequestException.class)
                .hasMessageContaining("path는 /를 시작으로 하여 경로를 나타냅니다.");
    }
}
