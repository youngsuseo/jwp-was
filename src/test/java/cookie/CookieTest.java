package cookie;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieTest {

    @Test
    void setLoginCookie_success() {
        Cookie cookie = new Cookie();
        cookie.setResponseLoginCookie(true);
        assertThat(cookie.getCookie()).isEqualTo("logined=true");
    }

    @Test
    void setLoginCookie_fail() {
        Cookie cookie = new Cookie();
        cookie.setResponseLoginCookie(false);
        assertThat(cookie.getCookie()).isEqualTo("logined=false");
    }

    @Test
    void getRequestCookie() {
        Cookie cookie = new Cookie();
        cookie.setResponseLoginCookie(true);

        assertThat(Cookie.getRequestCookie()).isEqualTo("Cookie: logined=true;");
    }

    @Test
    void getResponseCookie() {
        Cookie cookie = new Cookie();
        cookie.setResponseLoginCookie(true);

        assertThat(Cookie.getResponseCookie()).isEqualTo("Set-Cookie: logined=true; Path=/");
    }

    @Test
    void exists() {
        Cookie cookie = new Cookie();
        cookie.setResponseLoginCookie(true);

        assertThat(Cookie.exists()).isTrue();
    }
}