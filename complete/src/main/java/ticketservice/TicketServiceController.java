package tickserv;


import java.util.AbstractMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import tickserv.TicketService;
import tickserv.SeatHold;

@RestController
public class TicketServiceController {
    private static TicketService tServ = new TicketService();

    /* a simple query with no parameters */
    @RequestMapping("/v1/ticketService/numSeatsAvailable")
    AbstractMap.SimpleEntry numSeatsAvail() {
        return new AbstractMap.SimpleEntry("Num-seats-available",String.valueOf(tServ.numSeatsAvailable()));
    }

    /* display a map of the auditorium; this JSON "report" can readily be transformed in to a human-
     * readable image by the use of simple tools such as sed and tr, e.g.:
     *      curl -i http://localhost:8080//v1/ticketService/showAuditoriumMap 2>/dev/null \
     *         | sed 's/\["/","/;s/"\]//;s/","/,/g' | tr , "\012"; echo
     */
    @RequestMapping(value="/v1/ticketService/showAuditoriumMap", method=RequestMethod.GET)
    String[] showAuditoriumMap(@RequestParam(value="seatHoldId",defaultValue="0",required=false) String seatHoldId) {
        return tServ.showAuditoriumMap(Integer.parseInt(seatHoldId));
    }

    /* hold some seats, with the potential to transform into a reservation if the timeout doesn't elapse first */
    @RequestMapping(value="/v1/ticketService/findAndHoldSeats", method=RequestMethod.POST)
    ResponseEntity<SeatHold> findAndHoldSeats(@RequestParam(value="numSeats",defaultValue="1") String numSeats,
                                       @RequestParam(value="customerEmail",defaultValue="") String customerEmail) {
        SeatHold seatHoldResult = tServ.findAndHoldSeats (Integer.parseInt(numSeats), customerEmail);

        if (seatHoldResult.getIsValid()) {
            return new ResponseEntity<SeatHold>(seatHoldResult,HttpStatus.OK);
        } else {
            /* want to send an HTTP error code 422 (UNPROCESSABLE_ENTITY) for errors which aren't
             *        invalid parameters per se, but rather are invalid in a given situation.   Note that
             *        here we are able to package a "reason" code into the HTTP response.
             */
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    /* turn a previously issued "hold request" into a reservation */
    @RequestMapping(value="/v1/ticketService/reserveSeats", method=RequestMethod.POST)
    ResponseEntity<AbstractMap.SimpleEntry> reserveSeats(@RequestParam(value="seatHoldId",defaultValue="0") String seatHoldId,
                            @RequestParam(value="customerEmail",defaultValue="") String customerEmail) {
        /* NOTES:
         *   o    using an AbstractMap.SimpleEntry as a packaging mechanism for a string (value) in a
         *        simple JSON key-value pair
         *   o    want to send an HTTP error code 422 (UNPROCESSABLE_ENTITY) for errors which aren't
         *        invalid parameters per se, but rather are invalid in a given situation.   Note that
         *        here we are able to package a "reason" code into the HTTP response.
         */
        String reserveResult = tServ.reserveSeats(Integer.parseInt(seatHoldId), customerEmail);
        if (reserveResult.startsWith("FAILURE")) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new AbstractMap.SimpleEntry("reason",reserveResult));
        } else { /* return values which don't begin with "FAILURE" are successful, returning HTTP code 200 */
            return new ResponseEntity<AbstractMap.SimpleEntry>(new AbstractMap.SimpleEntry("Reservation-result",reserveResult),HttpStatus.OK);
        }
        /* return new AbstractMap.SimpleEntry("Reservation-result",reserveResult); */
    }
}
