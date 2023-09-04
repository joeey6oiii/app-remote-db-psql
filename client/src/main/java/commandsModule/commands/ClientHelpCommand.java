package commandsModule.commands;

import outputService.ColoredPrintStream;
import outputService.OutputSource;

import java.util.Map;
import java.util.stream.Collectors;

public class ClientHelpCommand implements ClientCommand {
    private final Map<String, ClientCommand> commands;

    public ClientHelpCommand(Map<String, ClientCommand> commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "c_help";
    }

    @Override
    public String getDescription() {
        return "Prints a list of available client commands";
    }

    @Override
    public void execute() {
        int commandLength = commands.keySet().stream().mapToInt(String::length).max().orElse(0);
        String formatString = "%-" + (commandLength + 4) + "s%s\n";
        StringBuilder builder;
        builder = new StringBuilder(commands.entrySet().stream()
                .map(entry -> String.format(formatString, entry.getKey() + " ", entry.getValue().getDescription()))
                .collect(Collectors.joining()));

        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());
        cps.println(builder.substring(0, builder.length() - 1));
    }
}
