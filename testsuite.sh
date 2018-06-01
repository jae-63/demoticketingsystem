echo "STARTING TEST SUITE"
curl -i http://localhost:8080//v1/ticketService/numSeatsAvailable; echo
echo "INITIAL MAP"
curl -i http://localhost:8080//v1/ticketService/showAuditoriumMap 2>/dev/null | sed 's/\["/","/;s/"\]//;s/","/,/g' | tr , "\012"; echo
echo "HOLDING 3 SEATS"
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=3&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=4&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=5&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=2&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=7&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=5&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=9&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=8&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=4&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=4&customerEmail=foo@bar.com'; echo
echo "INTERMEDIATE MAP"
curl -i http://localhost:8080//v1/ticketService/showAuditoriumMap 2>/dev/null | sed 's/\["/","/;s/"\]//;s/","/,/g' | tr , "\012"; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=8&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=4&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=4&customerEmail=foo@bar.com'; echo

curl -i http://localhost:8080//v1/ticketService/reserveSeats -X POST -d 'seatHoldId=7&customerEmail=foo@bar.com'; echo
echo "MAP FOLLOWING 1ST COMPLETED RESERVATION"
curl -i 'http://localhost:8080//v1/ticketService/showAuditoriumMap?seatHoldId=7' -X GET 2>/dev/null | sed 's/\["/","/;s/"\]//;s/","/,/g' | tr , "\012"; echo
echo "NUMBER OF SEATS AVAILABLE FOLLOWING 1ST COMPLETED RESERVATION"
curl http://localhost:8080//v1/ticketService/numSeatsAvailable; echo

echo "SLEEPING FOR 16 SECONDS"
sleep 16

curl -i http://localhost:8080//v1/ticketService/reserveSeats -X POST -d 'seatHoldId=999&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/reserveSeats -X POST -d 'seatHoldId=4&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/showAuditoriumMap 2>/dev/null | sed 's/\["/","/;s/"\]//;s/","/,/g' | tr , "\012"; echo
echo "ASKING FOR 40 seats which is impossible"
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=40&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=3&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=4&customerEmail=foo@bar.com'; echo
curl -i http://localhost:8080//v1/ticketService/findAndHoldSeats -X POST -d 'numSeats=5&customerEmail=foo@bar.com'; echo
curl -i 'http://localhost:8080//v1/ticketService/showAuditoriumMap?seatHoldId=16' 2>/dev/null | sed 's/\["/","/;s/"\]//;s/","/,/g' | tr , "\012"; echo
