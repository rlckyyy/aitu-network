package aitu.network.aitunetwork.model.enums;

public enum MessageType {
    MESSAGE_TEXT(false),
    MESSAGE_AUDIO(true),
    MESSAGE_VIDEO(true),
    JOIN(false),
    LEAVE(false);

    public final boolean isFileMessageType;

    MessageType(boolean isFileMessageType) {
        this.isFileMessageType = isFileMessageType;
    }
}