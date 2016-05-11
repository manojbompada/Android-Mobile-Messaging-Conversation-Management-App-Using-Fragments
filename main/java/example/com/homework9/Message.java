/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    Message.java
 */

package example.com.homework9;

/**
 * Created by Manoj on 4/15/2016.
 */
public class Message {
    private String timestamp,deletedBy=" ";
    private boolean message_read;
    private String message_text,receiver,sender;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Messages{" +
                "timestamp='" + timestamp + '\'' +
                ", message_read=" + message_read +
                ", message_text='" + message_text + '\'' +
                ", receiver='" + receiver + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }

    public String getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isMessage_read() {
        return message_read;
    }

    public void setMessage_read(boolean message_read) {
        this.message_read = message_read;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Message(String timestamp, String deletedBy, boolean message_read, String message_text, String sender, String receiver) {
        this.timestamp = timestamp;
        this.deletedBy = deletedBy;
        this.message_read = message_read;
        this.message_text = message_text;
        this.sender = sender;
        this.receiver = receiver;
    }
}