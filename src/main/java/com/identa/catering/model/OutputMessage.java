package com.identa.catering.model;

public class OutputMessage {

    private String from;
    private String text;

    public OutputMessage(final String from, final String text) {

        this.from = from;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return from;
    }
}
