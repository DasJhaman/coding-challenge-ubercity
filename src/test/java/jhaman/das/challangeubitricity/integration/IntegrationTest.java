package jhaman.das.challangeubitricity.integration;

import jhaman.das.challangeubitricity.enums.EvChargingUnits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    private static final String BASE_URL = "/v1/evubi/";
    private static final String PLUG_CAR = BASE_URL + "plug-car/";

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    private HttpEntity entity;

    @LocalServerPort
    private int port;

    @Before
    public void set() {
        entity = new HttpEntity(null, headers);
    }

    @Test
    public void testPlugCarToUbiSuccess() throws Exception {
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "1");
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "2");
        validate(new int[]{20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    public void testPlugCarToUbiFail() throws Exception {
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "1");
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "2");
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "3");
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "4");
        validate(new int[]{20, 20, 0, 20, 0, 0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    public void testPlugCarAndRemoveFromUbiSuccess() throws Exception {
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "1");
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "2");
        getStringResponseEntity(HttpMethod.DELETE, PLUG_CAR + "2");
        validate(new int[]{20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    public void testPlugCarAndRemoveFromUbiFail() throws Exception {
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "1");
        getStringResponseEntity(HttpMethod.POST, PLUG_CAR + "2");
        getStringResponseEntity(HttpMethod.DELETE, PLUG_CAR + "2");
        validate(new int[]{20, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    }

    private void validate(int[] inputs) {
        ConcurrentHashMap map = restTemplate.exchange(
                createURLWithPort(BASE_URL+"getUbiStatus"), HttpMethod.GET, entity, ConcurrentHashMap.class).getBody();
        for (int i = 0; i < 10; i++) {
            assertEquals(Objects.requireNonNull(map).get((i + 1) + ""), transform(inputs[i]));
        }
    }

    private String transform(int chargingValue) {
        switch (chargingValue) {
            case 0:
                return EvChargingUnits.EV_CHARGING_POINT_AVAILABLE.getChargingValue();
            case 10:
                return EvChargingUnits.EV_CHARGE_LOWER_RATE.getChargingValue();
            case 20:
                return EvChargingUnits.EV_CHARGE_HIGHER_RATE.getChargingValue();
            default:
                return "";
        }
    }


    private void getStringResponseEntity(HttpMethod method, String uri) {
        restTemplate.exchange(createURLWithPort(uri), method, entity, String.class);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}