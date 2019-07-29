package com.company;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

import static spark.Spark.*;

public class SmsApp {

    private static HashMap<String, Long> players = new HashMap<>();

    public static void main(String[] args) {
        get("/", (req, res) -> "Hello Web");

        post("/sms", (req, res) -> {
            String command = "";
            String number = req.queryParams("From");
            long id = -1;

            if (players.get(number) != null) {
                id = players.get(number);
            }
            if (id > 0) {
                command = req.queryParams("Body").toLowerCase();

                if (!(command.startsWith("check") || command.startsWith("reset")
                        || command.startsWith("help")
                        || command.startsWith("status") || command.startsWith("move"))) {
                    command = "act_" + command;
                }
                if (command.startsWith("help")) {
                    command = "help";
                }
                command = command.replace(" ", "_");
                if (!(command.startsWith("reset") || command.startsWith("help"))) {
                    command = id + "_" + command;
                }
            } else {
                command = "play_Player";
            }
            String result = readTextFromUrl("http://localhost:8080/" + command.trim()).trim();

            if (id < 0) {
                players.put(number, Long.parseLong(result));
                result = "Welcome to Zork via SMS!";
            }

            if (command.equals("reset")) {
                players.clear();
            }

            res.type("application/xml");
            Body body = new Body
                    .Builder(result)
                    .build();
            Message sms = new Message
                    .Builder()
                    .body(body)
                    .build();
            MessagingResponse twiml = new MessagingResponse
                    .Builder()
                    .message(sms)
                    .build();
            return twiml.toXml();
        });
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readTextFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String text = readAll(rd);
            return text;
        } finally {
            is.close();
        }
    }
}

