package com.example.seyedmoin.socketprograming;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;
    Socket socket;

    String Massage;
    DataOutputStream dOut;

    Client(String addr, int port, TextView textResponse, String msg) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        Massage = msg;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);

            if (socket.isConnected())
                Log.e("Client", "C: Connected");

            dOut = new DataOutputStream(socket.getOutputStream());

            // Send first message
            //dOut.writeByte(1);
            dOut.writeUTF(Massage);
            dOut.flush(); // Send off the data


            // Moin Saadati's Comment : Other Type For Send Message To Server
            // 2/4/17 2:43 AM

           /* // Send the second message
            dOut.writeByte(2);
            dOut.writeUTF(" B");
            dOut.flush(); // Send off the data

            // Send the third message
            dOut.writeByte(3);
            dOut.writeUTF(" C");
            dOut.writeUTF(" D");
            dOut.flush(); // Send off the data

            // Send the exit message
            dOut.writeByte(-1);
            dOut.flush();*/

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();


            // notice: inputStream.read() will block if no data return
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                Log.e("While", "TRUE");
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    dOut.close();
                    socket.close();
                    Log.e("Client", "close");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        publishProgress(response);
        super.onPostExecute(result);
    }

    private void publishProgress(String response) {
        textResponse.setText(response);
    }

}