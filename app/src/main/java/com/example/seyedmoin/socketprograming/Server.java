package com.example.seyedmoin.socketprograming;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Server {
    MainActivity activity;
    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 8080;

    public Server(MainActivity activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                Log.e("Server", "close");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                // create ServerSocket using specified port
                serverSocket = new ServerSocket(socketServerPORT);

                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    count++;
                    message += "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n";

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.msg.setText(message);
                        }
                    });

                    SocketServerReplyThread socketServerReplyThread =
                            new SocketServerReplyThread(socket, count);
                    socketServerReplyThread.run();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            //String msgReply = "Hello from Server, you are #" + cnt;
            String msgReply = "";


            try {
                outputStream = hostThreadSocket.getOutputStream();

                // zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

                DataInputStream dIn = new DataInputStream(hostThreadSocket.getInputStream());

                msgReply += dIn.readUTF();

                // // Moin Saadati's Comment : Other Type For Get Message From Client
                // 2/4/17 2:48 AM

//                boolean done = false;
//                while (!done) {
//                    byte messageType = dIn.readByte();
//
//                    switch (messageType) {
//                        case 1: // Type A
//                            msgReply += dIn.readUTF();
//                            break;
//                        case 2: // Type B
//                            msgReply += dIn.readUTF();
//                            break;
//                        case 3: // Type C
//                            msgReply += dIn.readUTF();
//                            msgReply += dIn.readUTF();
//                            break;
//                        default:
//                            done = true;
//                    }
//                }

                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();

                dIn.close();
                message += "replayed: " + msgReply + "\n";

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        activity.msg.setText(message);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });
        }

    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}