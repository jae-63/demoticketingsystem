package test.tickserv; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;

import tickserv.TicketService;
import tickserv.SeatHold;

/** 
* SeatHold Tester. 
* 
* @author Jonathan Epstein
* @since <pre>May 31, 2018</pre> 
* @version 1.0 
*/ 
public class SeatHoldTest {
    private static TicketService testTicketService;
    /* test 4 different SeatHold's to address different scenarios */
    private static SeatHold testSeatHold1;
    private static SeatHold testSeatHold2;
    private static SeatHold testSeatHold3;
    private static SeatHold testSeatHold4;

    /* use a relatively small auditorium for these tests, for simplicity and readability */
    private static final int AUD_DEPTH = 6;
    private static final int AUD_WIDTH = 7;
    private static final int HOLD_EXP_IN_SECONDS = 2;
    private static final String customerEmail = "test@example.com";

@Before
public void before() throws Exception {
    testTicketService = new TicketService(AUD_DEPTH, AUD_WIDTH, HOLD_EXP_IN_SECONDS);

    testSeatHold1 = new SeatHold(5, customerEmail, testTicketService);
    testSeatHold2 = new SeatHold(4, customerEmail, testTicketService);

    /* by the time we get here the good seats should already be taken */
    testSeatHold3 = new SeatHold(6, customerEmail, testTicketService);

    /* this request should fail entirely since we can't fit 9 customers into a 6-seat row */
    testSeatHold4 = new SeatHold(9, customerEmail, testTicketService);
}

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: getSeatHoldIndex() 
* 
*/ 
@Test
public void testGetSeatHoldIndex() throws Exception {

    /* ensure that the holdIndices were assigned sequentially as expected */

    assert(testSeatHold1.getSeatHoldIndex() == 1);
    assert(testSeatHold2.getSeatHoldIndex() == 2);
    assert(testSeatHold3.getSeatHoldIndex() == 3);
    assert(testSeatHold4.getSeatHoldIndex() == 4);
}

/** 
* 
* Method: getSeatsRow() 
* 
*/ 
@Test
public void testGetSeatsRow() throws Exception { 
    assert(testSeatHold1.getSeatsRow() >= 1);
    assert(testSeatHold1.getSeatsRow() <= 2); /* not back in the cheap seats */

/*    assert(testSeatHold3.getSeatsRow() >= 1);
    assert(testSeatHold3.getSeatsRow() > 2); /* back in the cheap seats! */
} 

/** 
* 
* Method: getSeatsLeftmost() 
* 
*/ 
@Test
public void testGetSeatsLeftmost() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getNumSeats() 
* 
*/ 
@Test
public void testGetNumSeats() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getCustomerEmail() 
* 
*/ 
@Test
public void testGetCustomerEmail() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getTimeHeld() 
* 
*/ 
@Test
public void testGetTimeHeld() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getIsValid() 
* 
*/ 
@Test
public void testGetIsValid() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: scoreSeatQuality(int row, int seatNumberInRow, int auditoriumDepth, int auditoriumWidth) 
* 
*/ 
@Test
public void testScoreSeatQuality() throws Exception { 
//TODO: Test goes here... 
} 


/** 
* 
* Method: findBlockOfSeats(int numSeats, tickserv.TicketService ticketService) 
* 
*/ 
@Test
public void testFindBlockOfSeats() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = SeatHold.getClass().getMethod("findBlockOfSeats", int.class, tickserv.TicketService.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
