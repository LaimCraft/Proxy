package ru.laimcraft.proxy.api.message;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import java.io.Serializable;

public class Message implements Serializable {

    byte[] bytes;

    public Message(byte[] bytes) {
        this.bytes = bytes;
    }

    public ByteArrayDataInput getByteArrayDataInput() {
        return ByteStreams.newDataInput(bytes);
    }

}
