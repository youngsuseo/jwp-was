package utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileIoUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(FileIoUtilsTest.class);

    @Test
    void loadFileFromClasspath() throws Exception {
        byte[] body = FileIoUtils.loadFileFromClasspath("./templates/index.html");
        log.debug("file : {}", new String(body));
    }

    @Test
    void loadFileFromClasspath_css() throws Exception {
        byte[] body = FileIoUtils.loadFileFromClasspath("./static/css/bootstrap.min.css");
        log.debug("file : {}", new String(body));
    }
}
