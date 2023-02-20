package model;

import org.fhtw.application.model.ModelSerializer;
import org.fhtw.application.model.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelSerializerTest {
    @Test
    void testSerializer() {
        ModelSerializer serializer = new ModelSerializer();
        Profile userProfile = new Profile("Kaneki", "I am a Ghoul", "O_O");

        assertEquals("{\r\n  \"Name\" : \"Kaneki\",\r\n  \"Bio\" : \"I am a Ghoul\",\r\n  \"Image\" : \"O_O\"\r\n}", serializer.serialize(userProfile));
    }
}
