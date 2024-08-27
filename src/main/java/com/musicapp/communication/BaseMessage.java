package com.musicapp.communication;

/**
 * Eine einfache Implementierung der Message-Schnittstelle.
 */
public class BaseMessage implements Message {
    private final String content;

    public BaseMessage(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return  content;
    }
}
