package ai.api.sample;

import android.widget.ListView;


public class ChatMessage {
    public boolean left;
    public String message;
    public boolean imageShow;

    public ChatMessage(boolean left, String message,boolean imageShow) {
        super();
        this.left = left;
        this.message = message;
        this.imageShow=imageShow;
    }
}
