package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;
import webserver.http.controller.UserController;
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
                body = FileIoUtils.loadFileFromClasspath(httpRequest.responsePath());
                response200Header(dos, body.length); // 200 의 Response Header, 하위 내용들을 생성한다.
                responseBody(dos, body); // 읽어온 페이지를 전달한다.
            } else {
                String handlerMappingPath = HandlerAdapter.handlerMapping(httpRequest);
                body = FileIoUtils.loadFileFromClasspath(handlerMappingPath);
                response302Header(dos, body.length); // 200 의 Response Header, 하위 내용들을 생성한다.
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
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n"); // Status Code
            dos.writeBytes("Location: /index.html \r\n"); // Status Code
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
