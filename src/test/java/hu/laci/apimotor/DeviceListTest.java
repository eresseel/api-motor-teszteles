package hu.laci.apimotor;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class DeviceListTest {

    @BeforeAll
    static void setUp() {
        ApiTestConfig.configure();
    }

    @Test
    @DisplayName("TC-01 - Eszközlista sikeres lekérdezése")
    void shouldReturnDeviceListSuccessfully() {
        Response response = given()
                .when()
                .get("/devices")
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode(), "A HTTP státuszkód 200 legyen.");

        Object responseBody = response.jsonPath().get("$");
        assertInstanceOf(List.class, responseBody, "A válasz JSON tömb legyen.");

        List<?> devices = (List<?>) responseBody;
        assertFalse(devices.isEmpty(), "A válasz legalább egy eszközt tartalmazzon.");
    }

    @Test
    @DisplayName("TC-02 - Listaelemek alapmezőinek ellenőrzése")
    void shouldContainRequiredFieldsWithExpectedTypesInFirstDevice() {
        Response response = given()
                .when()
                .get("/devices")
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode(), "A HTTP státuszkód 200 legyen.");

        Object responseBody = response.jsonPath().get("$");
        assertInstanceOf(List.class, responseBody, "A válasz JSON tömb legyen.");

        List<?> devices = (List<?>) responseBody;
        assertFalse(devices.isEmpty(), "A válasz legalább egy eszközt tartalmazzon.");

        Object firstElement = devices.get(0);
        assertInstanceOf(Map.class, firstElement, "A lista első eleme JSON objektum legyen.");

        Map<?, ?> firstDevice = (Map<?, ?>) firstElement;

        assertTrue(firstDevice.containsKey("id"), "Az első eszköz tartalmazza az id mezőt.");
        assertTrue(firstDevice.containsKey("name"), "Az első eszköz tartalmazza a name mezőt.");
        assertTrue(firstDevice.containsKey("category"), "Az első eszköz tartalmazza a category mezőt.");

        Object id = firstDevice.get("id");
        assertInstanceOf(Number.class, id, "Az id mező numerikus érték legyen.");
        assertTrue(firstDevice.get("name") instanceof String,
                "A name mező szöveges érték legyen.");
        assertTrue(firstDevice.get("category") instanceof String,
                "A category mező szöveges érték legyen.");
    }
}
