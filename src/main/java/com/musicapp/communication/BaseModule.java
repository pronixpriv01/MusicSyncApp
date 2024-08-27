package com.musicapp.communication;

/**
 * Eine Basisklasse für Module, die sendMessage und receiveMessage Methoden implementiert.
 */
public abstract class BaseModule implements Module{
    @Override
    public void sendMessage(Module target, Message message) {
        target.receiveMessage(message);
    }

    @Override
    public abstract void receiveMessage(Message message);
}
