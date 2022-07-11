package com.meem.plugins.tcpcomm;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {

    public static final String TAG = "Node Server";
    public static final String SERVER_IP = "192.168.4.1"; //server IP address
    public static final int SERVER_PORT = 80;
    private String serverMessage;
    // sends message received notifications
    private OnMessageReceived messageListener = null;
    // while this is true, the server will continue running
    private boolean run = false;
    private PrintWriter bufferOut;
    private BufferedReader bufferIn;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        messageListener = listener;
    }

    public void sendMessage(final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (bufferOut != null) {
                    bufferOut.write(message);
                    bufferOut.flush();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void stopClient() {

        run = false;

        if (bufferOut != null) {
            bufferOut.flush();
            bufferOut.close();
        }

        messageListener = null;
        bufferIn = null;
        bufferOut = null;
        serverMessage = null;
    }

    public void run() {

        run = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.d("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVER_PORT);

            try {
                bufferOut = new PrintWriter(socket.getOutputStream());
                bufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                int charsRead = 0;
                char[] buffer = new char[1024];

                while (run) {
                    charsRead = bufferIn.read(buffer);
                    if (charsRead > -1)  {
                        serverMessage = new String(buffer).substring(0, charsRead);
                    }
                    if (serverMessage != null && messageListener != null) {
                        messageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
                }
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }

    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}