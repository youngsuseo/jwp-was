package webserver.http.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {

//    @DisplayName("Http Request 형식으로 문자열 전달시, 해당 값으로 분리됨을 확인")
//    @Test
//    void construct() {
//        String httpRequestData = "GET /docs/index.html HTTP/1.1\n" +
//                "Host: www.nowhere123.com\n" +
//                "Accept: image/gif, image/jpeg, */*\n" +
//                "Accept-Language: en-us\n" +
//                "Accept-Encoding: gzip, deflate\n" +
//                "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)";
//
//        HttpRequest httpRequest = new HttpRequest(httpRequestData);
//        assertThat(httpRequest.getRequestLine()).isEqualTo(new RequestLine("GET /docs/index.html HTTP/1.1"));
//        assertThat(httpRequest.getRequestHeaders()).hasSize(5);
//    }
}