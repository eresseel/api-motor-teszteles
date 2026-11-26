package hu.laci.apimotor;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class DeviceCreateAndDeleteTest {

    @BeforeAll
    static void setUp() {
        ApiTestConfig.configure();
    }

    private static int createDevice() {
        String requestBody = "{\"name\": \"Temp Device\", \"category\": \"laptop\","
                + "\"data\": {\"year\": 2026, \"price\": 100000, \"available\": true}}";

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
    @DisplayName("TC-09 - Letrehozott eszkoz torlese")
    void shouldDeleteCreatedDeviceSuccessfully() {
        int createdId = createDevice();

        Response response = given()
                .when()
                .delete("/devices/" + createdId)
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode(), "A HTTP statuszod 200 legyen.");

        String message = response.jsonPath().getString("message");
        assertNotNull(message, "A valasz tartalmaz message mezot.");
        assertTrue(message.contains(String.valueOf(createdId)),
                "A message tartalmazza a torolt eszkoz azonositojat.");
    }

    @Test
    @DisplayName("TC-10 - Torolt eszkoz nem kerdezhetoe le")
    void shouldReturn404ForDeletedDevice() {
        int createdId = createDevice();

        given().when().delete("/devices/" + createdId);

        Response response = given()
                .when()
                .get("/devices/" + createdId)
                .then()
                .extract()
                .response();

        assertEquals(404, response.statusCode(), "A HTTP statuszod 404 legyen.");
        assertEquals("Not Found", response.jsonPath().getString("error"),
                "Az error mezo erteke 'Not Found' legyen.");

        String message = response.jsonPath().getString("message");
        assertNotNull(message, "A valasz tartalmaz message mezot.");
        assertTrue(message.contains(String.valueOf(createdId)),
                "A message jelzi, hogy az adott azonositoju eszkoz nem talalhato.");
    }
}
