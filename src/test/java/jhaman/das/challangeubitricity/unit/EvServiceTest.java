package jhaman.das.challangeubitricity.unit;

import jhaman.das.challangeubitricity.EvUbiState;
import jhaman.das.challangeubitricity.enums.EvChargingUnits;
import jhaman.das.challangeubitricity.repository.EvUbiRepository;
import jhaman.das.challangeubitricity.services.EvUbiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EvServiceTest {

    @Mock
    private EvUbiRepository repo;
    @Mock
    private EvUbiState state;

    private EvUbiService service;

    @Before
    public void setup() {
        service = new EvUbiService(repo, state);
    }

    @Test(expected = ResponseStatusException.class)
    public void exceptionIfCarAlreadyPlugged() {
        when(repo.getStationValue(Mockito.anyInt())).thenReturn(EvChargingUnits.EV_CHARGE_HIGHER_RATE);
        service.add(3);
    }

    @Test(expected = ResponseStatusException.class)
    public void exceptionIfCarNotPluggedAlready() {
        when(repo.getStationValue(Mockito.anyInt())).thenReturn(EvChargingUnits.EV_CHARGING_POINT_AVAILABLE);
        service.delete(2);
    }

}
