package network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import network.interfaces.Handler;

/**
 * Class to be used as a way for a {@code Server} and {@code Client} to communicate over a socket.
 *
 * @see network.interfaces.Client
 * @see Handler
 * @see network.interfaces.Server
 */
public final class Connection {
    private final Socket socket;
    private final BufferedReader inputStream;
    private final BufferedWriter outputStream;
    private final Handler handler;

    /**
     * Constructs a new {@code Connection} with the provided socket.
     * @param socket The socket to communicate over
     * @param handler The handler to send received communications to
     * @throws IOException Thrown if there is an issue opening the socket
     */
    public Connection(Socket socket, Handler handler) throws IOException {
        this.socket = socket;
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.handler = handler;
        new Thread(this::readInput).start();
    }

    /**
     * Constructs a new {@code Connection} with the provided IP address and port number.
     * @param address The IP address to connect to
     * @param port The port number to connect to
     * @param handler The handler to send received communications to
     * @throws IOException Thrown if there is an issue opening the socket
     */
    public Connection(InetAddress address, int port, Handler handler) throws IOException {
        this(new Socket(address, port), handler);
    }

    /**
     * Writes the provided output to the output stream; in other words, send the provided output to
     * the {@code Connection} on the other side of the socket.
     * @param output The output to write
     */
    public void writeOutput(String output) {
        try {
            synchronized (outputStream) {
                outputStream.write(output);
                outputStream.newLine();
                outputStream.flush();
            }
        } catch (IOException _) {
            close();
        }
    }

    /**
     * Reads the input from the input stream and send it to the {@code Handler}; in other words,
     * receives the input from the {@code Connection} on the other side of the socket.
     */
    private void readInput() {
        String input;
        try {
            while ((input = inputStream.readLine()) != null) {
                handler.readInput(input);
            }
        } catch (IOException _) {
            close();
        }
    }

    /**
     * Closes the socket and informs the {@code Handler} of such.
     */
    private void close() {
        try {
            socket.close();
        } catch (IOException _) {
        }
        handler.handleDisconnect();
    }
}
