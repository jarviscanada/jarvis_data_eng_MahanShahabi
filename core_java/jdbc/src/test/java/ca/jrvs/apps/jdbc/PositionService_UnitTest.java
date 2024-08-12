package ca.jrvs.apps.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PositionService_UnitTest {
    @Mock
    PositionDao positionDao;
    @Spy
    Position mockPosition;
    @InjectMocks
    PositionService positionService;
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void buy() throws Exception {
        mockPosition.setTicker("AAPL");
        mockPosition.setNumOfShares(100);
        mockPosition.setValuePaid(200);

        when(positionDao.findById("AAPL")).thenReturn(Optional.of(mockPosition));

        // Capture the Position object passed to save
        ArgumentCaptor<Position> positionCaptor = ArgumentCaptor.forClass(Position.class);

        Position serviceActual = positionService.buy("AAPL", 100, 200);

        verify(positionDao, times(1)).save(positionCaptor.capture());
        verify(positionDao, times(1)).findById("AAPL");
        assertEquals(mockPosition.getTicker(), serviceActual.getTicker());
        assertEquals(mockPosition.getNumOfShares(), serviceActual.getNumOfShares());
        assertEquals(mockPosition.getValuePaid(), serviceActual.getValuePaid());
    }

    @Test
    void sell() throws Exception {
        positionService.sell("AAPL");
        verify(positionDao, times(1)).deleteById("AAPL");
    }
}