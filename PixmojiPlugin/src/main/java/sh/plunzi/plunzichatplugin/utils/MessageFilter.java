package sh.plunzi.plunzichatplugin.utils;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class MessageFilter implements Filter {

    @Override
    public boolean isLoggable(LogRecord record) {
        String message = record.getMessage();

        return !(
                message.contains("issued server command: /msg ") ||
                message.contains("issued server command: /w ") ||
                message.contains("issued server command: /tell ") ||
                message.contains("issued server command: /dm ") ||
                message.contains("issued server command: /t ") ||
                message.contains("issued server command: /r ")
        );

    }
}
