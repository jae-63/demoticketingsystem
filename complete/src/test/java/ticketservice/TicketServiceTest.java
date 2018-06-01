package test.tickserv; 

import org.junit.Test; 
import org.junit.Before;
import org.junit.BeforeClass;
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
    private static TicketService virginTicketService; /* another service which hasn't been modified yet */

    private static tickserv.SeatHold testSeatHold1;
    private static tickserv.SeatHold testSeatHold2;
    private static tickserv.SeatHold testSeatHold3;
    private static tickserv.SeatHold testSeatHold4;
    private static tickserv.SeatHold testSeatHold5;
    private static tickserv.SeatHold testSeatHold6;

    private static final String customerEmail = "test@example.com";
    private static final String alternateCustomerEmail = "test2@bar.com";

    /* use a relatively small auditorium for these tests, for simplicity and readability */
    private static final int AUD_DEPTH = 6;
    private static final int AUD_WIDTH = 8;
    private static final int HOLD_EXP_IN_SECONDS = 200;

@BeforeClass
public static void beforeClass() throws Exception {
    testTicketService = new TicketService(AUD_DEPTH, AUD_WIDTH, HOLD_EXP_IN_SECONDS);
    virginTicketService = new TicketService(AUD_DEPTH, AUD_WIDTH, HOLD_EXP_IN_SECONDS);

    testSeatHold1 = testTicketService.findAndHoldSeats(5, customerEmail);
    testSeatHold2 = testTicketService.findAndHoldSeats(4, customerEmail);

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
* Method: getAuditoriumSeatsStatus(int seatsRow, int seatNum) 
* 
*/ 
@Test
public void testGetAuditoriumSeatsStatus() throws Exception { 
//TODO: Test goes here...
    assert(virginTicketService.getAuditoriumSeatsStatus(1,1) == TicketService.SeatStatus.AVAILABLE);
    assert(testTicketService.getAuditoriumSeatsStatus(1,3) != TicketService.SeatStatus.AVAILABLE);
} 


/** 
* 
* Method: showAuditoriumMap(int seatHoldId) 
* 
*/ 
@Test
public void testShowAuditoriumMap() throws Exception {
    String [] map = virginTicketService.showAuditoriumMap(0);

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
    int countAvailable = virginTicketService.numSeatsAvailable();

    assert(countAvailable == AUD_DEPTH * AUD_WIDTH);
}

/** 
* 
* Method: findAndHoldSeats(int numSeats, String customerEmail) 
* 
*/ 
@Test
public void testFindAndHoldSeats() throws Exception { 
    testSeatHold3 = testTicketService.findAndHoldSeats(4, customerEmail);

//    assert(testSeatHold3.getSeatHoldIndex() == 3);
} 

/** 
* 
* Method: reserveSeats(int seatHoldId, String customerEmail) 
* 
*/ 
@Test
public void testReserveSeats() throws Exception { 
    String reserve1 = testTicketService.reserveSeats(testSeatHold1.getSeatHoldIndex(),customerEmail);
    assert(! reserve1.startsWith("FAILURE"));
} 


} 
