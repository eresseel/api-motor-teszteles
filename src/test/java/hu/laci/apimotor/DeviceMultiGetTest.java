package hu.laci.apimotor;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class DeviceMultiGetTest {

    @BeforeAll
    static void setUp() {
        ApiTestConfig.configure();
    }

    @Test
    @DisplayName("TC-05 - Több eszköz lekérdezése query paraméterrel")
    void shouldReturnMultipleDevicesByQueryParams() {
        Response response = given()
                .queryParam("id", "1")
                .queryParam("id", "3")
                .when()
                .get("/devices")
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode(), "A HTTP státuszkód 200 legyen.");

        Object responseBody = response.jsonPath().get("$");
        assertInstanceOf(List.class, responseBody, "A válasz JSON tömb legyen.");

        List<?> devices = (List<?>) responseBody;
        assertEquals(2, devices.size(), "A válaszban pontosan 2 elem szerepeljen.");

        List<Integer> ids = response.jsonPath().getList("id", Integer.class);
        assertTrue(ids.contains(1), "A válaszban szerepeljen az 1 azonosítójú eszköz.");
        assertTrue(ids.contains(3), "A válaszban szerepeljen a 3 azonosítójú eszköz.");
    }

    @Test
    @DisplayName("TC-06 - Nem létező azonosító kezelése query paraméteres lekérdezésben")
    void shouldIgnoreNonExistingIdInQueryParams() {
        Response response = given()
                .queryParam("id", "1")
                .queryParam("id", "999999")
                .when()
                .get("/devices")
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode(), "A HTTP státuszkód 200 legyen.");

        List<?> devices = response.jsonPath().getList("$");
        assertEquals(1, devices.size(), "A válaszban pontosan 1 elem szerepeljen.");

        List<Integer> ids = response.jsonPath().getList("id", Integer.class);
        assertTrue(ids.contains(1), "A válaszban szerepeljen az 1 azonosítójú eszköz.");
        assertFalse(ids.contains(999999), "A válaszban ne szerepeljen a 999999 azonosítójú eszköz.");
    }
}
