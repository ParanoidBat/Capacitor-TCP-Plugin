package com.meem.plugins.tcpcomm;

import android.os.AsyncTask;
import android.util.Log;

public class TCP {
    private TcpClient tcpClient;

    public TCP(){

    }



    public void sendTcpMessage(String msg){
        if (tcpClient != null) tcpClient.sendMessage(msg);
    }

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

}