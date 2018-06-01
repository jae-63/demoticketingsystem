package test.tickserv; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;

import tickserv.TicketService;


/** 
* TicketService Tester. 
* 
* @author Jonathan Epstein
* @since <pre>May 31, 2018</pre> 
* @version 1.0 
*/ 
public class TicketServiceTest {
    private static TicketService testTicketService;
    /* use a relatively small auditorium for these tests, for simplicity and readability */
    private static final int AUD_DEPTH = 6;
    private static final int AUD_WIDTH = 7;
    private static final int HOLD_EXP_IN_SECONDS = 2;

@Before
public void before() throws Exception {
    testTicketService = new TicketService(AUD_DEPTH, AUD_WIDTH, HOLD_EXP_IN_SECONDS);
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getAuditoriumDepth() 
* 
*/ 
@Test
public void testGetAuditoriumDepth() throws Exception { 
    assert (testTicketService.getAuditoriumDepth() == AUD_DEPTH);
} 

/** 
* 
* Method: getAuditoriumWidth() 
* 
*/ 
@Test
public void testGetAuditoriumWidth() throws Exception {
    assert (testTicketService.getAuditoriumWidth() == AUD_WIDTH);
}
    /**
* 
* Method: revertExpiredHolds() 
* 
*/ 
@Test
public void testRevertExpiredHolds() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setAuditoriumSeatsStatus(int seatsRow, int seatNum, SeatStatus newStatus) 
* 
*/ 
@Test
public void testSetAuditoriumSeatsStatus() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getAuditoriumSeatsStatus(int seatsRow, int seatNum) 
* 
*/ 
@Test
public void testGetAuditoriumSeatsStatus() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setAuditoriumHoldId(int seatsRow, int seatNum, int holdId) 
* 
*/ 
@Test
public void testSetAuditoriumHoldId() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setAuditoriumHoldTime(int seatsRow, int seatNum, long timeHeld) 
* 
*/ 
@Test
public void testSetAuditoriumHoldTime() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getAuditoriumHoldTime(int seatsRow, int seatNum) 
* 
*/ 
@Test
public void testGetAuditoriumHoldTime() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setAuditoriumReservedSeatEmails(int seatsRow, int seatNum, String email) 
* 
*/ 
@Test
public void testSetAuditoriumReservedSeatEmails() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getAuditoriumReservedSeatEmails(int seatsRow, int seatNum) 
* 
*/ 
@Test
public void testGetAuditoriumReservedSeatEmails() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: showAuditoriumMap(int seatHoldId) 
* 
*/ 
@Test
public void testShowAuditoriumMap() throws Exception {
    String [] map = testTicketService.showAuditoriumMap(0);

    assert(map.length == AUD_DEPTH);
    assert(map[0].length() == AUD_WIDTH);
}

/** 
* 
* Method: numSeatsAvailable() 
* 
*/ 
@Test
public void testNumSeatsAvailable() throws Exception { 
    int countAvailable = testTicketService.numSeatsAvailable();

    assert(countAvailable == AUD_DEPTH * AUD_WIDTH);
}

/** 
* 
* Method: findAndHoldSeats(int numSeats, String customerEmail) 
* 
*/ 
@Test
public void testFindAndHoldSeats() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: reserveSeats(int seatHoldId, String customerEmail) 
* 
*/ 
@Test
public void testReserveSeats() throws Exception { 
//TODO: Test goes here... 
} 


} 
