package org.cilab.agabus.messaging.sub;

public class SubscriberFactory {
    public TopicSubscriber getTopicSubscriber() {
        return new TopicSubscriberImpl();
    }
}