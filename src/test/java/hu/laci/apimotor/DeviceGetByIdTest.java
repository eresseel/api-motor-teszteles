package hu.laci.apimotor;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class DeviceGetByIdTest {

    @BeforeAll
    static void setUp() {
        ApiTestConfig.configure();
    }

    @Test
    @DisplayName("TC-03 - Létező eszköz lekérdezése azonosító alapján")
    void shouldReturnExistingDeviceById() {
        Response response = given()
                .when()
                .get("/devices/2")
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode(), "A HTTP státuszkód 200 legyen.");

        assertEquals(2, (int) response.jsonPath().getInt("id"),
                "Az id mező értéke 2 legyen.");
        assertEquals("Dell UltraSharp U2723QE", response.jsonPath().getString("name"),
                "A name mező értéke 'Dell UltraSharp U2723QE' legyen.");
        assertEquals("monitor", response.jsonPath().getString("category"),
                "A category mező értéke 'monitor' legyen.");

        Object data = response.jsonPath().get("data");
        assertNotNull(data, "A válasz tartalmaz data objektumot.");
        assertInstanceOf(Map.class, data, "A data mező JSON objektum legyen.");

        assertEquals(2023, (int) response.jsonPath().getInt("data.year"),
                "A data.year értéke 2023 legyen.");
        assertEquals(229000, (int) response.jsonPath().getInt("data.price"),
                "A data.price értéke 229000 legyen.");
    }

    @Test
    @DisplayName("TC-04 - Nem létező eszköz lekérdezése (negatív teszt)")
    void shouldReturn404ForNonExistingDevice() {
        Response response = given()
                .when()
                .get("/devices/999999")
                .then()
                .extract()
                .response();

        assertEquals(404, response.statusCode(), "A HTTP státuszkód 404 legyen.");
        assertEquals("Not Found", response.jsonPath().getString("error"),
                "Az error mező értéke 'Not Found' legyen.");
        assertEquals("Device eszköz a megadott azonosítóval 999999 nem található.",
                response.jsonPath().getString("message"),
                "A message mező értéke megfelelő hibaüzenet legyen.");
    }
}
