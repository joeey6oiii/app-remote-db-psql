package outputService;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.PrintStream;

public class StreamPrinter implements Printer {
    private final PrintStream output;

    public StreamPrinter(PrintStream output) {
        AnsiConsole.systemInstall();
        this.output = output;
    }

    public void println(MessageType messageType, String message) {
        String formattedMessage = this.formatMessage(messageType, message);
        output.println(formattedMessage);
    }

    private String formatMessage(MessageType messageType, String message) {
        Ansi color = Ansi.ansi().fg(messageType.getColor());
        return color.a(message).reset().toString();
    }
}
