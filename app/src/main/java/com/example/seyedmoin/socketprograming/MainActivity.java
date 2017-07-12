package com.example.seyedmoin.socketprograming;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // For Client Implementation
    TextView response;
    EditText editTextAddress, editTextPort, editTextmsg;
    Button buttonConnect;

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
        response = (TextView) findViewById(R.id.responseTextView);
        editTextAddress.setText("192.168.4.1");
        editTextPort.setText("1300");

        /*// Get Door Status
        new Handler().postDelayed(new Runnable() {
            public void run() {
                DefineSocketSend(editTextmsg.getText().toString());
                //Toast.makeText(getBaseContext(), "Toast", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(this, 2000); //now is every
            }
        }, 2000); //Every*/


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

    @Override
    protected void onPause() {
        super.onPause();
        server.onDestroy();
    }

    private void DefineSocketSend(String Msg) {

        Client myClient = new Client(editTextAddress.getText().toString()
                , Integer.parseInt(editTextPort.getText().toString())
                , new AsyncResult() {
            @Override
            public void onResponse(String result) {
                //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                response.setText("");
                response.setText("R : " + result);
            }
        });
        myClient.execute(Msg);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.connectButton:
                DefineSocketSend(editTextmsg.getText().toString());
                break;
            case R.id.btn_door:
                DefineSocketSend("$door$");
                break;
            case R.id.btn_door_status:
                DefineSocketSend("$door-status$");
                break;
            case R.id.btn_get_setting:
                DefineSocketSend("$setting-get$");
                break;
            case R.id.btn_get_remotes:
                DefineSocketSend("$setting-remotes$");
                break;
            case R.id.btn_reset_setting:
                DefineSocketSend("$setting-reset$");
                break;
            case R.id.btn_send_setting:
                String opt = "01-02-03-04-05-06-07-08-09-10-11-12-13-14-15";
                DefineSocketSend("$settingg-" + opt + "$");
                break;
            case R.id.btn_delete_remotes:
                DefineSocketSend("$setting-remove-all$");
                break;
        }
    }
}