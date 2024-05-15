package alantheknight.lab6.server.managers;

import alantheknight.lab6.common.commands.CommandManager;
import alantheknight.lab6.server.commands.*;

import java.util.List;

public class ServerCommandManager extends CommandManager<ServerCommand> {
    public ServerCommandManager() {
        super();
    }

    public void initCommands() {
        var commands = List.of(
                Clear.class,
                FilterByEndDate.class,
                Info.class,
                Insert.class,
                Login.class,
                PrintAscending.class,
                Register.class,
                RemoveAnyByEndDate.class,
                RemoveGreater.class,
                RemoveGreaterKey.class,
                RemoveKey.class,
                Show.class,
                Update.class
        );

        ServerCommand.bulkRegister(commands);
    }
}
