package model.http;

import exception.IllegalHttpRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HttpTest {

//    @DisplayName("RequestLine은 공백을 기준으로 데이터를 분리하고, method, path, protocol(, version)을 가진다.")
//    @Test
//    void construct() {
//        String requestLine = "GET /docs/index.html HTTP/1.1";
//        Http http = new Http(requestLine);
//    }

    @DisplayName("RequestLine에 유효하지 않는 데이터를 입력한 경우")
    @Test
    void construct_validationFail() {
        String requestLine = "GET /docs/index.html";

        assertThatThrownBy(() -> new Http(requestLine))
                .isInstanceOf(IllegalHttpRequestException.class)
                .hasMessageContaining("RequestLine은 method, path, protocol을 공백 기준으로 나누어 전송해야 합니다.");
    }

    @DisplayName("GET 요청 데이터를 전송한다.")
    @Test
    void httpGetRequest() {
        String requestLine = "GET /docs/index.html HTTP/1.1";

        Http http = new Http(requestLine);
        assertThat(http.getMethod()).isEqualTo(Method.GET);
        assertThat(http.getPath()).isEqualTo(new Path("/docs/index.html"));
        assertThat(http.getProtocol()).isEqualTo(new ProtocolVersion("HTTP/1.1"));
    }
}
