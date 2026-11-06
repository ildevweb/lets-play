package com.example.app.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void passwordCanBeAcceptedInRequestsButIsNeverSerialized() throws Exception {
        User user = objectMapper.readValue(
            """
                {"name":"Casey","email":"casey@example.com","password":"correct-horse"}
                """,
            User.class
        );

        assertEquals("correct-horse", user.getPassword());
        assertFalse(objectMapper.writeValueAsString(user).contains("correct-horse"));
    }
}
