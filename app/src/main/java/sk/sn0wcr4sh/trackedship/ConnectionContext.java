package sk.sn0wcr4sh.trackedship;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by sn0wcr4sh on 9/18/2017.
 *
 */

public class ConnectionContext {
    private Socket mSocket;
    private BufferedWriter mWriter;
    private BufferedReader mReader;

    public ConnectionContext(Socket socket) throws IOException {
        mSocket = socket;

        mWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        mReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public BufferedReader getReader() {
        return mReader;
    }

    public BufferedWriter getWriter() {
        return mWriter;
    }
}
