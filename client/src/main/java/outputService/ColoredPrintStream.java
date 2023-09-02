package outputService;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.OutputStream;
import java.io.PrintStream;

public class ColoredPrintStream extends PrintStream {
    public ColoredPrintStream(OutputStream out) {
        super(out);
        AnsiConsole.systemInstall();
    }

    public ColoredPrintStream(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
        AnsiConsole.systemInstall();
    }

    public void println(MessageType messageType, String message) {
        Ansi color = Ansi.ansi().fg(messageType.getColor());
        String formattedMessage = color.a(message).reset().toString();
        println(formattedMessage);
    }

    @Override
    public void close() {
        super.close();
        AnsiConsole.systemUninstall();
    }
}