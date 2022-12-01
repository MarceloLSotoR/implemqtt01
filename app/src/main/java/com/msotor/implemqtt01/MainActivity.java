package com.msotor.implemqtt01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MainActivity extends AppCompatActivity {
    private static final String TAG ="Implemqtt01";
    private MqttAndroidClient mqttAndroidClient;
    private MqttCallbackExtended mqttCallbackExtended;
    private IMqttActionListener iMqttActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(),"tcp://10.110.64.67:1883","appAnroidMSR_ID");
        mqttCallbackExtended = new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Toast.makeText(getApplicationContext(),"Broker conectado!", Toast.LENGTH_LONG).show();
                try {
                    mqttAndroidClient.subscribe("Topic01",0);
                    Toast.makeText(getApplicationContext(),"Subscrito a Topic01",Toast.LENGTH_LONG).show();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                Log.i(TAG,message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        };

        mqttAndroidClient.setCallback(mqttCallbackExtended);

        iMqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i(TAG,"Conectado a:" + mqttAndroidClient.getServerURI());
                DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                disconnectedBufferOptions.setBufferEnabled(true);
                disconnectedBufferOptions.setBufferSize(100);
                disconnectedBufferOptions.setPersistBuffer(false);
                disconnectedBufferOptions.setDeleteOldestMessages(false);
                mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i(TAG,"Falla al conectar a:" + mqttAndroidClient.getServerURI());
                exception.printStackTrace();
            }
        };

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName("marcelo");
        mqttConnectOptions.setPassword("1235".toCharArray());

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, iMqttActionListener);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}