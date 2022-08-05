package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import cookie.Cookie;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;
import webserver.http.model.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            HttpRequest httpRequest = new HttpRequest(bufferedReader);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body;
            // FIXME if-else 분기 말고 다른 방식 고려
            if (httpRequest.isResource()) {
                if (httpRequest.getRequestLine().getPathInformation().getPath().getPath().endsWith(".css")) {
                    body = FileIoUtils.loadFileFromClasspath(httpRequest.responsePath());
                    response200CssHeader(dos, body.length); // 200 의 Response Header, 하위 내용들을 생성한다.
                    responseBody(dos, body); // 읽어온 페이지를 전달한다.
                    return;
                }
                body = FileIoUtils.loadFileFromClasspath(httpRequest.responsePath());
                response200Header(dos, body.length); // 200 의 Response Header, 하위 내용들을 생성한다.
                responseBody(dos, body); // 읽어온 페이지를 전달한다.
            } else {
                if (HandlerAdapter.accessiblePagesAfterLogin(httpRequest)
                        && !"logined=true".equals(httpRequest.getRequestHeaders().getRequestHeadersMap().get("Cookie"))) { // FIXME filter 역할이긴한데, if-else 말고 다른식으로
                    response302Header(dos, "/user/login.html");
                } else if (HandlerAdapter.accessiblePagesAfterLogin(httpRequest)
                        && "logined=true".equals(httpRequest.getRequestHeaders().getRequestHeadersMap().get("Cookie"))) {

                    TemplateLoader loader = new ClassPathTemplateLoader();
                    loader.setPrefix("/templates");
                    loader.setSuffix(".html");
                    Handlebars handlebars = new Handlebars(loader);

                    Template template = handlebars.compile("user/list");

                    User user = new User("javajigi", "password", "자바지기", "javajigi@gmail.com");
                    Map<String, User> map = new LinkedHashMap<>();
                    map.put("user", user);

                    String profilePage = template.apply(map);
                    body = profilePage.getBytes(StandardCharsets.UTF_8);
                    response200Header(dos, body.length); // 200 의 Response Header, 하위 내용들을 생성한다.
                    responseBody(dos, body); // 읽어온 페이지를 전달한다.
                } else {
                    Object handlerMapping = HandlerAdapter.handlerMapping(httpRequest);
                    if (handlerMapping instanceof String) {
                        response302Header(dos, String.valueOf(handlerMapping)); // 200 의 Response Header, 하위 내용들을 생성한다.
                    } else {
                        if (handlerMapping instanceof Collection) {
                            Collection<User> users = (Collection<User>) handlerMapping;

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                            objectOutputStream.writeObject(users);
                            body = byteArrayOutputStream.toByteArray();

                            response200Header(dos, body.length); // 200 의 Response Header, 하위 내용들을 생성한다.
                            responseBody(dos, body); // 읽어온 페이지를 전달한다.
                        }

                        System.out.println("handlerMapping = " + handlerMapping);
                        // 객체를 해당 타입으로 변환한 이후 return
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n"); // Status Code
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n"); // Response Header
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n"); // Response Header
            if (Cookie.exists()) {
                dos.writeBytes(Cookie.getResponseCookie() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n"); // Status Code
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n"); // Response Header
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n"); // Response Header
            if (Cookie.exists()) {
                dos.writeBytes(Cookie.getResponseCookie() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectPath) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n"); // Status Code
            dos.writeBytes("Location: " + redirectPath + " \r\n"); // Status Code
            if (Cookie.exists()) {
                dos.writeBytes(Cookie.getResponseCookie() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302HeaderToLogin(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n"); // Status Code
            dos.writeBytes("Location: /user/login.html \r\n"); // Status Code
            if (Cookie.exists()) {
                dos.writeBytes(Cookie.getResponseCookie() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length); // 해당 경로에 있는 파일 읽어와 쓰기
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
