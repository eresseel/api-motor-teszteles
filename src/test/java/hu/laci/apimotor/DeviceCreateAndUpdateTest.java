package hu.laci.apimotor;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class DeviceCreateAndUpdateTest {

    @BeforeAll
    static void setUp() {
        ApiTestConfig.configure();
    }

    private static int createDevice(String name, String category) {
        String requestBody = "{\"name\": \"" + name + "\", \"category\": \"" + category + "\","
                + "\"data\": {\"year\": 2026, \"price\": 350000, \"available\": true}}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/devices")
                .then()
                .extract()
                .response();

        assertEquals(201, response.statusCode(), "Az eszkoz letrehozasa sikeres legyen (201).");
        return ((Number) response.jsonPath().get("id")).intValue();
    }

    @Test
    @DisplayName("TC-07 - Uj eszkoz letrehozasa")
    void shouldCreateNewDeviceSuccessfully() {
        String requestBody = "{\"name\": \"Test Notebook\", \"category\": \"laptop\","
                + "\"data\": {\"year\": 2026, \"price\": 350000, \"available\": true}}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/devices")
                .then()
                .extract()
                .response();

        assertEquals(201, response.statusCode(), "A HTTP statuszod 201 legyen.");

        Object id = response.jsonPath().get("id");
        assertNotNull(id, "A valasz tartalmaz id mezot.");
        assertInstanceOf(Number.class, id, "Az id mezo numerikus ertek legyen.");

        assertEquals("Test Notebook", response.jsonPath().getString("name"),
                "A name mezo erteke 'Test Notebook' legyen.");
        assertEquals("laptop", response.jsonPath().getString("category"),
                "A category mezo erteke 'laptop' legyen.");

        Object data = response.jsonPath().get("data");
        assertNotNull(data, "A valasz tartalmaz data objektumot.");
        assertInstanceOf(Map.class, data, "A data mezo JSON objektum legyen.");

        assertNotNull(response.jsonPath().getString("createdAt"),
                "A valasz tartalmaz createdAt mezot.");
    }

    @Test
    @DisplayName("TC-08 - Letrehozott eszkoz modositasa")
    void shouldUpdateCreatedDeviceSuccessfully() {
        int createdId = createDevice("Test Notebook For Update", "laptop");

        String requestBody = "{\"name\": \"Updated Test Notebook\", \"category\": \"laptop\","
                + "\"data\": {\"year\": 2026, \"price\": 325000, \"available\": false}}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/devices/" + createdId)
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode(), "A HTTP statuszod 200 legyen.");
        assertEquals(createdId, (int) response.jsonPath().getInt("id"),
                "A valasz id mezoje megegyezik a letrehozott eszkoz azonositojával.");
        assertEquals("Updated Test Notebook", response.jsonPath().getString("name"),
                "A name mezo erteke 'Updated Test Notebook' legyen.");
        assertEquals("laptop", response.jsonPath().getString("category"),
                "A category mezo erteke 'laptop' legyen.");
        assertEquals(325000, (int) response.jsonPath().getInt("data.price"),
                "A data.price erteke 325000 legyen.");
        assertFalse(response.jsonPath().getBoolean("data.available"),
                "A data.available erteke false legyen.");
        assertNotNull(response.jsonPath().getString("updatedAt"),
                "A valasz tartalmaz updatedAt mezot.");
    }
}
