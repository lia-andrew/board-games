# Board Games

This project provides a way to play your favorite turn-based board games online via a Client-Server
architecture. You can also choose how to play the game: by inputting your own choices, having
a move randomly chosen, or using an implementation of
the [Monte-Carlo Tree Search](https://en.wikipedia.org/wiki/Monte_Carlo_tree_search) algorithm.
Other Client-Server functionalities are also supported, such as having a global group chat, private
messaging, and more!

The purpose of this project is to showcase the value of highly clean, maintainable code. A great
level of care and detail was invested to ensure that future extensions to this project (e.g. new
board games variants) can be integrated into the existing project seamlessly and require a very
minimal amount of new code to be written. Not only that, but it should be extremely clear from the
existing code and javadoc *how* to implement those future extensions.

## Getting Started

The following steps will instruct you on how to get a local version of this project working in no
time; whether to play a game or develop your own.

### Pre-Requisites

The following software must be installed to follow the installation steps:

* Git - [How to install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* Java 23 (or higher) - [How to install Java 23](https://docs.oracle.com/en/java/javase/23/install/overview-jdk-installation.html)
* Apache Maven - two options:
    * Install Apache Maven to be used throughout your machine - [How to install Apache Maven](https://maven.apache.org/install.html)
    * Use an IDE, such as IntelliJ, and use the Apache Maven plugin - [How to use Apache Maven in IntelliJ](https://www.jetbrains.com/help/idea/maven-support.html)

### Installing

1. Start by copying this repository into a local folder:
    ```
    git clone github.com/alias012/board-games
    ```

2. Enter the new `board-games` directory:
    ```
    cd board-games
    ```

3. Run the following Apache Maven goals:
    ```
    mvn clean compile package
    ```

4. Now enter the newly generated `target` directory:
    ```
    cd target
    ```

5. The following jar files should all be found inside:
    * `RunServer.jar`
    * `RunClient.jar`
    * `board_games-1.0.0.jar`
    * `board_games-1.0.0-javadoc.jar`
    * `board_games-1.0.0-test.jar`

6. Follow the [How To Use](#how-to-use) section and enjoy!

7. You can also import the jar files starting with `board_game-1.0.0` as an external library into
   another project

## How To Use

The expected "happy path" will be demonstrated below:

1. Open a new terminal and run the Server:
    ```
    java -jar RunServer.jar
    ```

2. Enter the type of server you would like to run (e.g. QUARTO):
    ```
    QUARTO
    Please input one of the options: QUARTO
    ```

3. Enter the port number to listen on (e.g. 12345):
    ```
    Input the port number to connect to: 12345
    ```

4. Open two new terminals and run a Client in each:
    ```
    java -jar RunClient.jar
    ```

5. Enter the type of client you would like to run (e.g. 'QUARTO' for the first, 'AUTOPLAY_QUARTO'
   for the second)
    ```
    QUARTO, AUTOPLAY_QUARTO
    Please input one of the options: QUARTO
    ```

6. Enter the IP address to connect to (right now the server is running locally, so just hit
   enter/return)
    ```
    Input the IP address to connect to (leave blank for localhost):
    ```

7. Enter the port number to connect to (right now the server is listening on port 12345, so enter
   that)
    ```
    Input the port number to connect to: 12345
    ```

8. For the AUTOPLAY_QUARTO client, now input your username (e.g. 'autoplay')
    ```
    Successfully connected to server
    Input your desired username: autoplay
    ```

9. Still for the AUTOPLAY_QUARTO client, now input the strategy you want to employ (e.g. 'RANDOM')
    ```
    HUMAN, RANDOM, MCTS
    Please input one of the options: RANDOM
    ```

10. And finally, still for the AUTOPLAY_QUARTO client, now input the queue to join (e.g. 'abc')
    ```
    Enter the name of the queue to repeatedly join: abc
    ```

11. The AUTOPLAY_QUARTO client is all set up, and you do not need to do anything else for it!

12. Going back to the QUARTO client, you should have a list of options:
    ```
    Successfully connected to server
    Enter: LOGIN, LIST, QUEUE, MOVE, EXIT [Extras: QUEUE with name, RANK, CHAT, WHISPER]
    ```

13. An explanation of the options them follows:
    * `LOGIN`: attempts to log in to the server with a username
    * `LIST`: lists all active clients on the server
    * `QUEUE`: queues you up and will put you in a game with others in the same queue
    * `MOVE`: play a move for the game you are playing
    * `EXIT`: disconnect from the server and quit the program
    * `RANK`: get the number of wins of all users (active and inactive) on the server
    * `CHAT`: send a chat message to all active clients
    * `WHISPER`: send a private message to a specific client

14. Now, enter 'LOGIN' and enter your username (e.g. 'quarto'):
    ```
    LOGIN
    Input your desired username: quarto
    Successfully logged in
    ```

15. Next, enter 'QUEUE', choose a strategy (e.g. 'HUMAN') and the queue to join ('abc', so that we
    queue against the AUTOPLAY client):
    ```
    queue
    HUMAN, RANDOM, MCTS
    Please input one of the options: HUMAN
    Enter the name of the queue to join: abc
    A new Quarto game has started!
    You are versus autoplay
    Your opponent has the first turn.
    A new move has been made!
    X_X_X_X is the next piece to be played
        |    |    |         00 | 01 | 02 | 03
    ----+----+----+----    ----+----+----+----
        |    |    |         04 | 05 | 06 | 07
    ----+----+----+----    ----+----+----+----
        |    |    |         08 | 09 | 10 | 11
    ----+----+----+----    ----+----+----+----
        |    |    |         12 | 13 | 14 | 15
    ```

16. Finally, enter `MOVE` and choose what index to play the piece (e.g. '5'), and the number of the
    next piece to play (e.g. '1'):
    ```
    move
    Enter the index to play the next piece: 5
    Remaining pieces:
    1: LSRS, 2: DSRS, 3: DLRS, 4: LSSS, 5: DSSS, 6: LLSS, 7: DLSS, 8: LSRH, 9: DSRH, 10: LLRS,
    11: DLRH, 12: LSSH, 13: DSSH, 14: LLSH, 15: DLSH, 16: QUARTO,
    Opponent's next piece: 1
    A new move has been made!
    New move at index 5
    LIGHT_SMALL_ROUND_SOLID is the next piece to be played
        |    |    |         00 | 01 | 02 | 03
    ----+----+----+----    ----+----+----+----
        |LLRS|    |         04 | 05 | 06 | 07
    ----+----+----+----    ----+----+----+----
        |    |    |         08 | 09 | 10 | 11
    ----+----+----+----    ----+----+----+----
        |    |    |         12 | 13 | 14 | 15

    ```

17. You are now well-equipped to use the server and the client. Have fun!

## Built With

* [Maven](https://maven.apache.org/) - Dependency & Build Management
* [JUnit](https://junit.org/) - The Testing Framework

## Contributing

Contributing to this project is not desired at this time, but if you desire to do so, start by
making an issue on this repository describing the contribution you have in mind.

## Versioning

[SemVer](http://semver.org/) is used for versioning. Due to the very low-coupling in the project,
adding new board game variants should not qualify as a MAJOR change.

## Authors

* **Andrew Lia** - GitHub: [alias012](https://github.com/alias012) -
  LinkedIn: [AndrewLia](https://linkedin.com/in/andrew-lia)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Keywords

Java, Maven, JUnit, Client-Server Architecture, Monte Carlo Tree-Search, Clean Code, Maintainable
Code, Documentation, Low Coupling, High Cohesion, Defensive Programming, Absolutely 0% AI Generated.