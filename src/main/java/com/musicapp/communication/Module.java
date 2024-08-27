package com.musicapp.communication;

/**
 * Interface zur Definition eines Moduls, das Nachrichten senden und empfangen kann.
 */
public interface Module {
    void sendMessage(Module target, Message message);
    void receiveMessage(Message message);
}
