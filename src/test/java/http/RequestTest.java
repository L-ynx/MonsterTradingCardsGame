package http;

import org.fhtw.application.model.Card;
import org.fhtw.application.model.User;
import org.fhtw.http.Request;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {
    Request request = new Request();

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

    @Test
    void jsonToUser() {
        String json = """
                {
                    "username": "Tanjiro",
                    "password": "1234"
                }""";

        request.setBody(json);

        User user = request.getBodyAs(User.class);

        assertEquals("Tanjiro", user.getUsername());
        assertEquals("1234", user.getPassword());
    }

    @Test
    void jsonToCardList() {
        String json = """
                [
                    {
                        "Name": "Fireball",
                        "Id" : "ad098afs-asfjsaf90-afasuhf0-asasf",
                        "Damage": 10
                    },
                    {
                        "Name": "Waterball",
                        "Id" : "ad098afs-asfjsaf90-afasuhf0-asasf",
                        "Damage": 10
                    },
                    {
                        "Name": "Earthball",
                        "Id" : "ad098afs-asfjsaf90-afasuhf0-asasf",
                        "Damage": 10
                    },
                    {
                        "Name": "Airball",
                        "Id" : "ad098afs-asfjsaf90-afasuhf0-asasf",
                        "Damage": 10
                    }
                ]""";

        request.setBody(json);

        List<Card> cards = request.getBodyAsList(Card.class);

        assertEquals("Fireball", cards.get(0).getCardName());
        assertEquals("Waterball", cards.get(1).getCardName());
        assertEquals("Earthball", cards.get(2).getCardName());
        assertEquals("Airball", cards.get(3).getCardName());
    }
}
