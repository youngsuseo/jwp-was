package webserver.http.model.request;

import exception.IllegalHttpRequestException;
import org.checkerframework.checker.units.qual.A;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpRequestLines {
    private final List<String> lines;

    public HttpRequestLines(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        lines = initial(readLine(bufferedReader));
    }

    public HttpRequestLines(String textLines) {
        lines = initial(textLines);
    }

    private List<String> initial(String textLines) {
        final List<String> lines;
        if (textLines == null || textLines.length() < 1) {
            throw new IllegalHttpRequestException("httpRequest는 빈 값이 전달 될 수 없습니다.");
        }
        lines = Arrays.asList(textLines.split("\n"));
        if (lines.size() < 2) {
            throw new IllegalHttpRequestException("httpRequest는 적어도 두 줄 이상의 정보로 구성되어 있습니다.");
        }
        return lines;
    }

    private String readLine(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        String beforeLine;
        do {
            beforeLine = line;
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            stringBuilder.append(line).append("\n");
        } while (!("".equals(line) && "".equals(beforeLine)));
        return stringBuilder.toString();
    }

    public String requestLine() {
        return lines.get(0);
    }

    public List<String> requestHeader() {
        return requestHeaderAndBody().get(0).stream().filter(header -> !"".equals(header)).collect(Collectors.toList());
    }

    public List<String> requestBody() {
        if (requestHeaderAndBody().get(1).size() < 1) {
            return null;
        }
        return requestHeaderAndBody().get(1);
    }

    private List<List<String>> requestHeaderAndBody() {
        return new ArrayList<>(lines.stream().filter(text -> !text.equals(lines.get(0)))
                .collect(Collectors.partitioningBy(this::partitionCondition)).values());
    }

    private boolean partitionCondition(String line) {
        int emptyLine = lines.indexOf("");
        if (emptyLine < 0) {
            return false;
        }
        return lines.indexOf(line) > emptyLine;
    }
}
