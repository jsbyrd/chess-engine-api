# Chess Engine
### Demo
https://www.youtube.com/watch?v=tBTz22TgpOw

### Description
Chess engine API built in Java with Spring Boot. The api requires two inputs. The first input is a String called fen,
which represents the chess position you would like to find the best move for. The second input is an integer called depth,
which is how "deep" in the move tree you would like to look. Made this for fun to see just how far I could get with minimal
research, and while it is very inefficient, I am proud that I managed to make it work as intended. There are still a ton
of improvements that can be made (most notably changing the chess board implementation from a 2D array to bitboards), but
that shall be for another time...

### Compilation Instructions
If you would like to run this application yourself
1) Install Java version 17 or higher
2) Download the code
3) If you have IntelliJ installed on your machine, use it to open the project. From there, just locate
`ChessEngineApiApplication.java` in `./src/main/java/com/jsbyrd02/chessengineapi`, right click the file, and select `Run`
4) If you do not have IntelliJ, open up the application in a terminal. In the project's root directory, run
`./mvnw spring-boot:run`
5) From here, the Spring Boot application should be running. In order to send an HTTP request to the API,
open up an application such as Postman and send a request with the following information:
   * Method: POST
   * Route: http://localhost:8080/api/moves
   * Body: JSON, in the following format:
     * "fen": "{FEN representation of the position}"
     * "depth": {Integer}
     * Example: {"fen": "5Kbk/6pp/6P1/8/8/8/7R/8 w - - 0 1", "depth": 6}
6) After a while, the application will respond with a 4 character string. The first two characters represent the piece-to-be-moved's
original position, and the next two characters represent the piece-to-be-moved's new position