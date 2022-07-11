package com.meem.plugins.tcpcomm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class CommActivity extends AppCompatActivity {
    private TcpClient tcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm);

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
            sendTcpMessage(values[0]);
            Log.d("recieved", values[0]);
            //response received from server
//            if (Byte.valueOf(values[0]) == commIDs.acknowledge) {
//                switch (counter) {
//                    case 0:
//                        rl_password.setVisibility(View.VISIBLE);
//                        counter++;
//                        break;
//                    case 1:
//                        btn_save.setVisibility(View.VISIBLE);
//                        counter++;
//                        break;
//                    case 2:
//                        if (tcpClient != null) tcpClient.stopClient();
//                        break;
//                }
//            } else if (Byte.valueOf(values[0]) == commIDs.failure) {
//                Toast.makeText(InitialSetupActivity.this, "Error: Try Again.", Toast.LENGTH_SHORT).show();
//            } else if (Byte.valueOf(values[0]) == commIDs.emptyOrCorrupted) {
//                Toast.makeText(InitialSetupActivity.this, "Some Error Occurred.", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private void sendTcpMessage(String msg){
        if (tcpClient != null) tcpClient.sendMessage(msg);
        else Log.d("comm activity", "Device not connected");
    }
}