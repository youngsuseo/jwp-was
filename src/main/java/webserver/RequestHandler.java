package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

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

            String line = bufferedReader.readLine();
            RequestLine requestLine = new RequestLine(line);

            StringBuilder stringBuilder = new StringBuilder();
            while (!"".equals(line)) {
                if (line == null) {
                    return;
                }
                line = bufferedReader.readLine();
                stringBuilder.append(line).append("\n");
            }

            RequestHeaders requestHeaders = new RequestHeaders(stringBuilder.toString());
            String contentLength = requestHeaders.getRequestHeadersMap().get("Content-Length");

            RequestBody requestBody = null;
            if (requestLine.getMethod() == Method.POST) { // FIXME getter 대신 메시지
                String requestBodyText = IOUtils.readData(bufferedReader, Integer.parseInt(contentLength));
                requestBody = new RequestBody(requestBodyText);
            }

            HttpRequest httpRequest = new HttpRequest(requestLine, requestHeaders, requestBody);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body;
            // FIXME if-else 분기 말고 다른 방식 고려
            if (httpRequest.isResource()) {
                String filePath = httpRequest.responsePath();
                body = FileIoUtils.loadFileFromClasspath(filePath);
                response200Header(dos, body.length); // 200 의 Response Header, 하위 내용들을 생성한다.
                responseBody(dos, body); // 읽어온 페이지를 전달한다.
            } else {
                // FIXME 디미터 법칙 고려
                if ("/user/create".equals(httpRequest.getRequestLine().getPathInformation().getPath().getPath())
                        && Method.GET == httpRequest.getRequestLine().getMethod()) {
                    User user = new User(httpRequest.getRequestLine().getPathInformation().getQueryStrings());
                    System.out.println("user = " + user);
                } else if ("/user/create".equals(httpRequest.getRequestLine().getPathInformation().getPath().getPath())
                        && Method.POST == httpRequest.getRequestLine().getMethod()) {
                    User user = new User(httpRequest.getRequestBody());
                    System.out.println("user = " + user);
                }
                body = FileIoUtils.loadFileFromClasspath("./templates/index.html");
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
