package com.unipi.reactor.p10Sink;

import com.unipi.reactor.util.Util;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;

public class Lec07AssignmentSlackMessenger {

    public static void main(String[] args) {

        SlackRoom slackRoom = new SlackRoom("Reactor");
        SlackMember mick = new SlackMember("Mick");
        SlackMember jane = new SlackMember("Jane");
        SlackMember tom = new SlackMember("Tom");

        slackRoom.joinRoom(mick);
        slackRoom.joinRoom(jane);

        mick.says("Hi all..");
        Util.sleepSeconds(4);

        jane.says("Hey!");
        Util.sleepSeconds(2);
        mick.says("I just wanted to say hi..");
        Util.sleepSeconds(4);

        slackRoom.joinRoom(tom);
        tom.says("Hey guys.. glad to be here!");

    }
}

class SlackRoom {

    private final String name;
    private Sinks.Many<SlackMessage> sink;
    private Flux<SlackMessage> flux;

    public SlackRoom(String name) {
        this.name = name;
        this.sink = Sinks.many().replay().all();
        this.flux = this.sink.asFlux();
    }

    public void joinRoom(SlackMember slackMember) {
        System.out.println(slackMember.getName() + " -- joined -- " + this.name);
        this.subscribe(slackMember);
        slackMember.setMessageConsumer(message -> this.postMessage(message, slackMember));
    }

    private void subscribe(SlackMember slackMember) {
        this.flux
                // Filter the member who actually sends the message in order to not send the message to himself
                .filter(slackMessage -> !slackMessage.getSender().equals(slackMember.getName()))
                // Set the name of the receiver
                .doOnNext(slackMessage -> slackMessage.setReceiver(slackMember.getName()))
                .map(SlackMessage::toString)
                .subscribe(slackMember::receives);
    }

    private void postMessage(String message, SlackMember slackMember) {
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setSender(slackMember.getName());
        slackMessage.setMessage(message);
        this.sink.tryEmitNext(slackMessage);
    }
}

class SlackMember {

    private final String name;
    private Consumer<String> messageConsumer;

    public SlackMember(String name) {
        this.name = name;
    }

    void setMessageConsumer(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    String getName() {
        return name;
    }

    void receives(String message) {
        System.out.println(message);
    }

    public void says(String message) {
        this.messageConsumer.accept(message);
    }
}

@Data
class SlackMessage {

    private static final String FORMAT = "[%s -> %s] : %s";

    private String sender;
    private String receiver;
    private String message;

    @Override
    public String toString() {
        return String.format(FORMAT, this.sender, this.receiver, this.getMessage());
    }
}
