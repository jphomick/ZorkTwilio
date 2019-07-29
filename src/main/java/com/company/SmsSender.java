package com.company;

// Install the Java helper library from twilio.com/docs/libraries/java
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID =
            "AC3c042dd1ea34110fdc470a6b828ba3b0";
    public static final String AUTH_TOKEN =
            "10db01607db89e8076632a09a43656ae";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber("+13015251563"), // to
                        new PhoneNumber("+12407022846"), // from
                        "Hello, this message was sent from Twilio")
                .create();

        System.out.println(message.getSid());
    }
}

