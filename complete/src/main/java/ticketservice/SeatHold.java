package tickserv;

import tickserv.TicketService;

public class SeatHold {

    private static int masterSeatHoldIndex = 1;

    private int seatHoldIndex;
    private int seatsRow;
    private int seatsLeftmost;
    private int numSeats;
    private String customerEmail;
    private long timeHeld;
    private boolean isValid;

    /**
     * This is the constructor for the SeatHold class but it also does the significant work of
     * trying to find a block of seats per the customer's request
     *
     * @param numSeats
     * @param customerEmail
     * @param ticketService
     */
    public SeatHold(int numSeats, String customerEmail, TicketService ticketService) {
        this.seatHoldIndex = masterSeatHoldIndex++;

        /* revert any seat-holds for which too much time has elapsed */
        ticketService.revertExpiredHolds();


        findBlockOfSeats(numSeats, ticketService);

        /* the above call modifies this,isValid, this.seatsRow and this.seatsLeftMost, which are
         * referenced below */

        if (this.isValid) {
            this.numSeats = numSeats;
            this.customerEmail = customerEmail;
            this.timeHeld = java.time.Instant.now().getEpochSecond();
            int width = ticketService.getAuditoriumWidth();
            for (int i = 0, seatNum = this.seatsLeftmost; i < numSeats && seatNum <= width ; i++, seatNum++) {
                ticketService.setAuditoriumSeatsStatus(this.seatsRow, seatNum, TicketService.SeatStatus.HELD);
                ticketService.setAuditoriumHoldTime(this.seatsRow, seatNum, this.timeHeld);
                ticketService.setAuditoriumReservedSeatEmails(this.seatsRow, seatNum, customerEmail);
            }
        }

    }

    public int getSeatHoldIndex() {
        return this.seatHoldIndex;
    }

    public int getSeatsRow() {
        return this.seatsRow;
    }

    public int getSeatsLeftmost() {
        return this.seatsLeftmost;
    }

    public int getNumSeats() {
        return this.numSeats;
    }

    public String getCustomerEmail () {
        return this.customerEmail;
    }

    public long getTimeHeld() {
        return this.timeHeld;
    }

    public boolean getIsValid() {
        return this.isValid;
    }

    static int scoreSeatQuality(int row, int seatNumberInRow, int auditoriumDepth, int auditoriumWidth) {
        /**
         * Calculate the quality of a seat in an auditorium
         * @param row                The row number in the auditorium, wherr row #1 is the front row.
         * @param seatNumberInRow    The seat number in the row, starting with 1 and ordered from house left to house right
         * @param auditoriumDepth    The number of rows in our rectangular auditorium
         * @param auditoriumWidth    The width of our rectangular auditorium, measured in seats
         * @return                   The seat quality, or -1 for an invalid seat coordinate
         */
        /* Given a typical rectangular auditorium, the best seats are in the front, but not too far off to either side
         * due to their poor viewing angle.
         *
         * So we'll grade the seats as follows in decreasing order of quality:
         *     3 => in the front of the auditorium but within a trapezoid which extends from (auditoriumWidth/2) in the front row
         *          to a slightly wider section in row # (auditoriumDepth/3).  The trapezoid widens by two seats per row, away
         *          from the stage.
         *     2 => in the middle third of the auditorium in terms of distance from the stage, or the "side seats" from grade 3
         *          above which were rejected for their poor viewing angle
         *     1 => the rear third of the auditorium in terms of distance from the stage
         *
         *     For example:
         *            ----------[[ STAGE ]]----------
         *           ---------------------------------
         *           222222223333333333333333322222222
         *           222222233333333333333333332222222
         *           222222333333333333333333333222222
         *           222222222222222222222222222222222
         *           222222222222222222222222222222222
         *           222222222222222222222222222222222
         *           111111111111111111111111111111111
         *           111111111111111111111111111111111
         *           111111111111111111111111111111111
         */

        if (auditoriumDepth <= 1 || auditoriumWidth <= 1) {
            return -1;
        }

        if (row < 1 || row > auditoriumDepth || seatNumberInRow < 1 || seatNumberInRow > auditoriumWidth) {
            return -1;
        }

        if (row > (2*auditoriumDepth)/3) { /* the cheap seats in the rear of the auditorium */
            return 1;
        }

        if (row <= auditoriumDepth/3) {
            /* calculation for the trapezoid widening as discussed above */
            int leftBoundary = auditoriumWidth / 4 - (row-1);

            /* using this computational approach assures symmetry between the two sides of the auditorium */
            int rightBoundary = auditoriumWidth - leftBoundary;

            if (seatNumberInRow >= leftBoundary && seatNumberInRow <= rightBoundary) {
                return 3; /* this is one of the best quality seats; "front and center" */
            }
        }

        return 2; /* intermediate quality seats */
    }


    /**
     * Identify a contiguous block of seats in a single row, of the best possible quality.
     *
     * Returns void but modifies the following fields in this object:
     *   isValid               did we successfully identify a valid available block of the prescribed size?
     *   seatsRow              the row in which the selected block seats reside
     *   seatsLeftmost         the leftmost seat (from "house left") in that block
     *
     * @param numSeats         number of requested seats
     * @param ticketService    the central repository of information regarding our auditorium
     */
    private void findBlockOfSeats(int numSeats, tickserv.TicketService ticketService) {
        int bestRow = -1;
        int bestLeftSeatNumber = -1;
        int bestSumOfScores = -1;

        int auditoriumDepth = ticketService.getAuditoriumDepth();
        int auditoriumWidth = ticketService.getAuditoriumWidth();

        /* examine every block of numSeats contiguous available seats in the auditorium, and score each block as the
         * sum of the scores of the individual seats.   The top-scoring contiguous block which is closest
         * to the front is designated for the potential "customer"
         */
        for (int row = 1; row <= auditoriumDepth; row++) {
            for (int leftSeatNum = 1; leftSeatNum < auditoriumWidth - numSeats; leftSeatNum++) {
                int sumOfScores = 0;

                for (int i=0, seatNum = leftSeatNum; i < numSeats && seatNum <= auditoriumWidth; i++, seatNum++) {
                    int seatScore = scoreSeatQuality(row, seatNum, auditoriumDepth, auditoriumWidth);
                    if (seatScore < 0 || ticketService.getAuditoriumSeatsStatus(row,seatNum) != tickserv.TicketService.SeatStatus.AVAILABLE) {
                        sumOfScores = -1;
                        break;
                    }
                    sumOfScores += seatScore;
                }

                /* if we get this far with a non-zero sumOfScores then this is a candidate
                 * for the best seat block, for the customer */

                if (sumOfScores > bestSumOfScores) { /* this is the new best */
                    bestSumOfScores = sumOfScores;
                    bestRow = row;
                    bestLeftSeatNumber = leftSeatNum;
                }
            }
        }

        this.isValid = bestRow > 0;
        this.seatsRow = bestRow;
        this.seatsLeftmost = bestLeftSeatNumber;
    }

}