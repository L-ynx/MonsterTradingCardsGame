package http;

import org.fhtw.http.Request;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {
    @Test
    void validRequest() {
        String req = """
                POST /users/Tanjiro HTTP/1.1\r
                Host: localhost:10001\r
                Content-Type: application/json\r
                Content-Length: 12\r
                Authorization: Bearer admin-mtcgToken\r
                \r
                Hello World!""";

        BufferedReader br = new BufferedReader(new StringReader(req));

        Request request = new Request(br);

        assertEquals("POST", request.getMethod());
        assertEquals("/users", request.getPath());
        assertEquals("Hello World!", request.getBody());
        assertEquals("admin-mtcgToken", request.getToken());
        assertEquals("admin", request.getUsername());
        assertEquals("Tanjiro", request.getPathUser());
        assertEquals("application/json", request.getContentType());
        assertEquals(12, request.getContentLength());
    }
}
