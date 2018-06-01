package test.tickserv; 

import java.util.concurrent.TimeUnit;

import org.junit.Test; 
import org.junit.Before;
import org.junit.BeforeClass;
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
    private static SeatHold testSeatHold5;
    private static SeatHold testSeatHold6;
    private static SeatHold testSeatHold7;
    private static SeatHold testSeatHold8;
    private static SeatHold testSeatHold9;
    private static SeatHold testSeatHold10;





    /* use a relatively small auditorium for these tests, for simplicity and readability */
    private static final int AUD_DEPTH = 6;
    private static final int AUD_WIDTH = 8;
    private static final int HOLD_EXP_IN_SECONDS = 2;
    private static final String customerEmail = "test@example.com";
    private static final String alternateCustomerEmail = "test2@bar.com";

@BeforeClass
public static void beforeClass() throws Exception {
    testTicketService = new TicketService(AUD_DEPTH, AUD_WIDTH, HOLD_EXP_IN_SECONDS);

    testSeatHold1 = new SeatHold(5, customerEmail, testTicketService);
    testSeatHold2 = new SeatHold(4, alternateCustomerEmail, testTicketService);

    /* by the time we get here the good seats should already be taken */
    testSeatHold3 = new SeatHold(6, customerEmail, testTicketService);

    /* this request should fail entirely since we can't fit 9 customers into a 6-seat row */
    testSeatHold4 = new SeatHold(9, customerEmail, testTicketService);

    testSeatHold5 = new SeatHold(6, customerEmail, testTicketService);
    testSeatHold6 = new SeatHold(6, customerEmail, testTicketService);
    testSeatHold7 = new SeatHold(6, customerEmail, testTicketService);

    /* by the time we get here the house should be too full for a wide order like this */
    testSeatHold8 = new SeatHold(5, customerEmail, testTicketService);

    /* but with plenty of room for a narrow order like this */
    testSeatHold9 = new SeatHold(2, customerEmail, testTicketService);

    /* wait until these held-but-not-reserved orders expire */
    TimeUnit.SECONDS.sleep(HOLD_EXP_IN_SECONDS+1);

    /* now there should be plenty of room for this order, since time has elapsed*/
    testSeatHold10 = new SeatHold(5, customerEmail, testTicketService);

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

    assert(testSeatHold3.getSeatsRow() >= 1);
    assert(testSeatHold3.getSeatsRow() > 2); /* back in the cheap seats! */



    assert(testSeatHold7.getSeatsRow() == 6);

}

/** 
* 
* Method: getSeatsLeftmost() 
* 
*/ 
@Test
public void testGetSeatsLeftmost() throws Exception { 
    assert(testSeatHold1.getSeatsLeftmost() >= 2); /* not on the aisle since this is the front row */
    assert(testSeatHold3.getSeatsLeftmost() == 1);  /* further back in the auditorium so therefore on the aisle */
}

/** 
* 
* Method: getNumSeats() 
* 
*/ 
@Test
public void testGetNumSeats() throws Exception { 
    assert(testSeatHold1.getNumSeats() == 5);
    assert(testSeatHold5.getNumSeats() == 6);
}

/** 
* 
* Method: getCustomerEmail() 
* 
*/ 
@Test
public void testGetCustomerEmail() throws Exception {
    assert(testSeatHold1.getCustomerEmail().equals(customerEmail));
    assert(testSeatHold2.getCustomerEmail().equals(alternateCustomerEmail));
} 


/** 
* 
* Method: getIsValid() 
* 
*/ 
@Test
public void testGetIsValid() throws Exception {
    assert(! testSeatHold4.getIsValid()); /* couldn't fill this extra-wide request */

    assert(testSeatHold5.getIsValid());
    assert(testSeatHold6.getIsValid());
    assert(testSeatHold7.getIsValid()); /* these are the last seats in the back of the hall */

    assert(! testSeatHold8.getIsValid()); /* the hall couldn't accommodate such a large order */
    assert(testSeatHold9.getIsValid()); /* but there was room for a small order */

    assert(testSeatHold10.getIsValid()); /* after some time passed, an order which failed above could now be fulfilled */

}



} 
