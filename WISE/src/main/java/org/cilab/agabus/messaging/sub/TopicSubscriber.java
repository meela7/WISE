package org.cilab.agabus.messaging.sub;

public interface TopicSubscriber {
    public void setConnection(String brokerURL, String userId, String password, String topicId);

    public void setMessageCallback(MessageCallback callback);

    public void subscribe();

    public void unsubscribe();
}
