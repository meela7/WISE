package org.cilab.agabus.messaging.pub;

public class PublisherFactory {
    public TopicPublisher getTopicPublisher() {
        return new TopicPublisherImpl();
    }
}