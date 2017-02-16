package com.example.seyedmoin.socketprograming;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // For Client Implementation
    TextView response;
    EditText editTextAddress, editTextPort, editTextmsg;
    Button buttonConnect, buttonClear, buttonSend;

    // For Client Implementation
    Server server;
    TextView infoip, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For Client Implementation
        editTextAddress = (EditText) findViewById(R.id.addressEditText);
        editTextPort = (EditText) findViewById(R.id.portEditText);
        editTextmsg = (EditText) findViewById(R.id.msgEditText);
        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttonClear = (Button) findViewById(R.id.clearButton);
        response = (TextView) findViewById(R.id.responseTextView);

        buttonConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Client myClient = new Client(editTextAddress.getText()
                        .toString(), Integer.parseInt(editTextPort
                        .getText().toString()), response, editTextmsg.getText().toString());
                myClient.execute();

            }
        });


        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                response.setText("");
            }
        });

        // For Server Implementation

        infoip = (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);
        server = new Server(this);
        infoip.setText(server.getIpAddress() + ":" + server.getPort());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.onDestroy();
    }
}