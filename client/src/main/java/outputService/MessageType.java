package outputService;

import org.fusesource.jansi.Ansi;

public enum MessageType {
    SUCCESS(Ansi.Color.GREEN),
    INFO(Ansi.Color.DEFAULT),
    WARNING(Ansi.Color.YELLOW),
    ERROR(Ansi.Color.RED);

    private final Ansi.Color color;

    MessageType(Ansi.Color color) {
        this.color = color;
    }

    public Ansi.Color getColor() {
        return color;
    }
}
