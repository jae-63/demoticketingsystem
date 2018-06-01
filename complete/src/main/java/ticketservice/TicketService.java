package tickserv;

import tickserv.SeatHold;
import java.util.*;

public class TicketService {
    public enum SeatStatus {
        AVAILABLE,
        HELD,
        RESERVED
    };

    private int auditoriumDepth;
    private int auditoriumWidth;
    private long holdExpirationInSeconds;

    private SeatStatus[][] auditoriumSeatStatus;
    private int[][] auditoriumHoldIds;
    private long[][] auditoriumHoldTimes;
    private String[][] auditoriumReservedSeatEmails;

    private HashMap<Integer,SeatHold> seatHoldMap;


    public TicketService(int auditoriumDepth, int auditoriumWidth, int holdExpirationInSeconds) {
        this.auditoriumDepth = auditoriumDepth;
        this.auditoriumWidth = auditoriumWidth;
        this.holdExpirationInSeconds = holdExpirationInSeconds;
        this.auditoriumSeatStatus = new SeatStatus[auditoriumDepth][auditoriumWidth];
        this.auditoriumHoldIds = new int[auditoriumDepth][auditoriumWidth];
        this.auditoriumHoldTimes = new long[auditoriumDepth][auditoriumWidth];
        this.auditoriumReservedSeatEmails = new String[auditoriumDepth][auditoriumWidth];
        this.seatHoldMap = new HashMap<Integer,SeatHold>();

        /* initially mark all seats as AVAILABLE */
        for (int row = 1; row <= this.auditoriumDepth; row++) {
            for (int seat = 1; seat <= this.auditoriumWidth; seat++) {
                setAuditoriumSeatsStatus(row, seat, SeatStatus.AVAILABLE);
            }
        }
    }

    public TicketService() {
        this(9, 33, 10);
    }

    public int getAuditoriumDepth() {
        return this.auditoriumDepth;
    }

    public int getAuditoriumWidth() {
        return this.auditoriumWidth;
    }

    /**
     * Traverse the 2D array of auditorium seats and check every seat marked HELD.   If its associated timer
     * has expired then re-mark that seat as AVAILABLE.
     */
    public void revertExpiredHolds() {
        long expirationTime = java.time.Instant.now().getEpochSecond() - (long)holdExpirationInSeconds;

        for (int row = 1; row <= this.auditoriumDepth; row++) {
            for (int seat = 1; seat <= this.auditoriumWidth; seat++) {
                if (getAuditoriumHoldTime(row,seat) <= expirationTime && getAuditoriumSeatsStatus(row,seat) == SeatStatus.HELD) {
                    setAuditoriumSeatsStatus(row, seat, SeatStatus.AVAILABLE);
                }
            }
        }
    }

    public void setAuditoriumSeatsStatus(int seatsRow, int seatNum, SeatStatus newStatus)
    {
        this.auditoriumSeatStatus[seatsRow-1][seatNum-1] = newStatus;
    }

    public SeatStatus getAuditoriumSeatsStatus(int seatsRow, int seatNum)
    {
        return this.auditoriumSeatStatus[seatsRow-1][seatNum-1];
    }

    public void setAuditoriumHoldId(int seatsRow, int seatNum, int holdId)
    {
    }

    public void setAuditoriumHoldTime(int seatsRow, int seatNum, long timeHeld)
    {
        this.auditoriumHoldTimes[seatsRow-1][seatNum-1] = timeHeld;
    }

    public long getAuditoriumHoldTime(int seatsRow, int seatNum)
    {
        return this.auditoriumHoldTimes[seatsRow-1][seatNum-1];
    }


    public void setAuditoriumReservedSeatEmails(int seatsRow, int seatNum, String email) {
        this.auditoriumReservedSeatEmails[seatsRow-1][seatNum-1] = email;
    }


    public String getAuditoriumReservedSeatEmails(int seatsRow, int seatNum) {
        return this.auditoriumReservedSeatEmails[seatsRow-1][seatNum-1];
    }

    /**
     *
     * @return     An array of strings which is a human-readable 2D representation of the auditorium's seats
     *             and their status
     */
    public String[] showAuditoriumMap(int seatHoldId)
    {
        String[] strArray = new String[this.auditoriumDepth];
        int seatHoldRow = -1;
        int seatHoldLeftmost = -1;
        int seatHoldNumSeats = -1;

        /* ensure that the map which we're about to generate accurately reflects expired Holds */
        revertExpiredHolds();

        SeatHold theSeatHold = seatHoldMap.get(seatHoldId);

        if (theSeatHold != null) {
            seatHoldRow = theSeatHold.getSeatsRow();
            seatHoldLeftmost = theSeatHold.getSeatsLeftmost();
            seatHoldNumSeats = theSeatHold.getNumSeats();
        }

        for (int row = 1; row <= this.auditoriumDepth; row++) {
            String displayString = ""; /* generate one output line per auditorium row, with one char per seat */
            for (int seat = 1; seat <= this.auditoriumWidth; seat++) {
                char displayChar = '-';
                SeatStatus seatStatus = getAuditoriumSeatsStatus(row,seat);

                if (seatStatus == SeatStatus.AVAILABLE) {
                    displayChar = '.';
                } else if (seatStatus == SeatStatus.HELD) {
                    displayChar = 'h';
                    if (row == seatHoldRow && seat >= seatHoldLeftmost && seat < (seatHoldLeftmost+seatHoldNumSeats)) {
                        displayChar = 'H'; /* label the held seat, if any */
                    }
                } else if (seatStatus == SeatStatus.RESERVED){
                    displayChar = 'r';
                    if (row == seatHoldRow && seat >= seatHoldLeftmost && seat < (seatHoldLeftmost+seatHoldNumSeats)) {
                        displayChar = 'R'; /* label the reserved seat, if any */
                    }
                }

                displayString += displayChar;
            }
            strArray[row-1] = displayString;
        }

        return strArray;
    }

    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    public int numSeatsAvailable() {
        int availableCount = 0;

        /* discard any holds which have expired */
        revertExpiredHolds();

        /* traverse the 2D array of seats and count all the AVAILABLE ones */
        for (int row = 1; row <= this.auditoriumDepth; row++) {
            for (int seat = 1; seat <= this.auditoriumWidth; seat++) {
                if (getAuditoriumSeatsStatus(row,seat) == SeatStatus.AVAILABLE) {
                    availableCount++;
                }
            }
        }

        return availableCount;
    }

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related information
     */
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail)
    {
        /* discard any holds which have expired */
        revertExpiredHolds();

        /* attempt to hold the requested number of seats */
        SeatHold theSeatHold = new SeatHold(numSeats, customerEmail, this);

        /* record this entry for future reference in a HashMap, indexed by the SeatHoldIndex */
        this.seatHoldMap.put(theSeatHold.getSeatHoldIndex(),theSeatHold);

        return theSeatHold;
    }

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold is assigned
     * @return a reservation confirmation code
     */
    public String reserveSeats(int seatHoldId, String customerEmail)
    {
        SeatHold theSeatHold = seatHoldMap.get(seatHoldId);

        revertExpiredHolds();

        if (theSeatHold == null) {
            return String.format("FAILURE-%d.   No such seat-hold", seatHoldId);
        } else if (customerEmail.toLowerCase().equals(theSeatHold.getCustomerEmail().toLowerCase())) {
            int row = theSeatHold.getSeatsRow();
            int leftmost = theSeatHold.getSeatsLeftmost();
            int numSeats = theSeatHold.getNumSeats();

            /* sanity check since the hold may have expired */
            for (int i = 0, seatNum = leftmost; i < numSeats; i++, seatNum++) {
                if (getAuditoriumSeatsStatus(row, seatNum) != SeatStatus.HELD) {
                    return (String.format("FAILURE-%d.   At least one held seat %d in row %d is no longer 'held'",
                            seatHoldId, seatNum, row));
                }
            }

            /* now that the sanity checks are completed, mark the seats reserved and return with a "success" value */
            for (int i = 0, seatNum = leftmost; i < numSeats; i++, seatNum++) {
               setAuditoriumSeatsStatus(row, seatNum, SeatStatus.RESERVED);
            }

            /* For the returned reservation confirmation code we're just prepending "RESERVED-" to the seatHoldId.
             * In a real reservation system we might for example digitally sign that seatHoldId in this context.
             */
            return String.format("RESERVED-%d", seatHoldId);
        } else {
            return String.format("FAILURE-%d.   Email %s MISMATCHES %s", seatHoldId, customerEmail, theSeatHold.getCustomerEmail());
        }
    }

}
