package commands;



import setup.ConfigCommandsDatabase;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager extends ArrayList<Command> {

    public Command getCommand(String alias) {
        return stream().filter(cmd -> Arrays.stream(cmd.aliases).anyMatch(s ->
                s.equalsIgnoreCase(alias))).findAny().orElse(null);
    }

    @Override
    public boolean add(Command command) {
        //check for command as column name, add it and default false if not there

        if (!ConfigCommandsDatabase.checkCommandColumn(command.getClass().getSimpleName())) {
            ConfigCommandsDatabase.addCommandColumn(command.getClass().getSimpleName());
        }

        return super.add(command);
    }
}