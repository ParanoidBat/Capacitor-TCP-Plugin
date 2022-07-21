package com.meem.plugins.tcpcomm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class CommActivity extends AppCompatActivity {
    private Button btn_set_ssid, btn_set_password, btn_save;
    private RelativeLayout rl_password;
    private TextInputLayout et_ssid, et_password;
    private TcpClient tcpClient;
    private CommIDs commIDs;
    private byte counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);

        btn_set_ssid = findViewById(R.id.btn_setup_ssid);
        btn_set_password = findViewById(R.id.btn_setup_password);
        btn_save = findViewById(R.id.btn_setup_save);

        et_ssid = findViewById(R.id.et_setup_ssid);
        et_password = findViewById(R.id.et_setup_password);

        rl_password = findViewById(R.id.rl_setup_password);

        btn_set_ssid.setOnClickListener(view -> {
            String msg = String.valueOf(commIDs.header) +
                    String.valueOf(commIDs.network) +
                    et_ssid.getEditText().getText().toString() +
                    "/";
            sendTcpMessage(msg);
        });

        btn_set_password.setOnClickListener(view -> {
            String msg = String.valueOf(commIDs.header) +
                    String.valueOf(commIDs.password) +
                    et_password.getEditText().getText().toString() +
                    "/";
            sendTcpMessage(msg);
        });

        // get id from user preferences
        btn_save.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences("CapacitorStorage", MODE_PRIVATE);
            String orgID = preferences.getString(getString(R.string.org_id), "ehh");

            if (!orgID.equalsIgnoreCase("ehh")) {
                String msg = String.valueOf(commIDs.header) +
                        String.valueOf(commIDs.finish) + orgID + "/";
                sendTcpMessage(msg);
            }
            else {
                sendTcpMessage(String.valueOf(commIDs.failure));
            }
        });

        commIDs = new CommIDs();
        new ConnectTask().execute("");
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            tcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            tcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            if (Byte.valueOf(values[0]) == commIDs.acknowledge) {
                switch (counter) {
                    case 0:
                        rl_password.setVisibility(View.VISIBLE);
                        counter++;
                        break;
                    case 1:
                        btn_save.setVisibility(View.VISIBLE);
                        counter++;
                        break;
                    case 2:
                        if (tcpClient != null) tcpClient.stopClient();
                        finish();
                        break;
                }
            } else if (Byte.valueOf(values[0]) == commIDs.failure) {
                Toast.makeText(CommActivity.this, "Error: Try Again.", Toast.LENGTH_SHORT).show();
            } else if (Byte.valueOf(values[0]) == commIDs.emptyOrCorrupted) {
                Toast.makeText(CommActivity.this, "Some Error Occurred.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendTcpMessage(String msg){
        if (tcpClient != null) tcpClient.sendMessage(msg);
        else Log.d("comm activity", "Device not connected");
    }
}