package com.meem.plugins.tcpcomm;

import android.content.Intent;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TCP")

public class TCPPlugin extends Plugin {

    private TCP implementation;

    @Override
    public void load(){
        implementation = new TCP();
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void sendMessage(PluginCall call){
        JSObject ret = new JSObject();

        try{
            getActivity().startActivity(new Intent(getActivity().getBaseContext(), CommActivity.class));
        }
        catch (Exception e){
            ret.put("value", e.getMessage());
            call.resolve(ret);
        }

        ret.put("value", "true");
        call.resolve(ret);
    }
}
