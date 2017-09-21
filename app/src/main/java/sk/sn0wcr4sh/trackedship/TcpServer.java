package sk.sn0wcr4sh.trackedship;

import android.location.Location;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sn0wcr4sh on 9/18/2017.
 *
 */

class TcpServer {
    interface Listener {
        void onClientConnected();
    }

    static final int PORT = 3456;

    private ServerSocket mServerSocket;
    private Thread mThread;
    private List<ConnectionContext> mSocketList;
    private Listener mListener;

    TcpServer(Listener listener) {
        mSocketList = new ArrayList<>();
        mListener = listener;
    }

    void start() {
        mThread = new Thread(new ServerThread());
        mThread.start();
    }

    void stop() {

        if (mThread != null)
            mThread.interrupt();

        try {
            mServerSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendLocationUpdate(Location location) {
        String message =
                "<loc_update>" +
                    "<lat>" + String.valueOf(location.getLatitude()) + "</lat>" +
                    "<lon>" + String.valueOf(location.getLongitude()) + "</lon>" +
                "</loc_update>";

        sendToAll(message);
    }

    private void sendToAll(String data) {
        // TODO remove connection on error

        for (ConnectionContext context : mSocketList) {
            try {
                BufferedWriter writer = context.getWriter();

                writer.write(data);
                writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ServerThread implements Runnable {

        @Override
        public void run() {
            Socket socket;
            try {
                mServerSocket = new ServerSocket(PORT);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    socket = mServerSocket.accept();
                    mSocketList.add(new ConnectionContext(socket));
                    mListener.onClientConnected();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
