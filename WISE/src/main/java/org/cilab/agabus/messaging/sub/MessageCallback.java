package org.cilab.agabus.messaging.sub;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

public abstract class MessageCallback {
    public abstract void messageArrived(String data);

    public abstract void connectionLost(Throwable cause);

    public abstract void deliveryComplete(IMqttDeliveryToken token);
}