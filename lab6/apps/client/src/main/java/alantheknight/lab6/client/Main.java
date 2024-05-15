package alantheknight.lab6.client;

import alantheknight.lab6.client.commands.*;
import alantheknight.lab6.common.utils.ConfigReader;
import alantheknight.lab6.common.utils.Console;
import alantheknight.lab6.common.utils.StandardConsole;

import java.util.List;

public class Main {
    public static final Console stdConsole = new StandardConsole();
    public static final ClientCommandManager commandManager = new ClientCommandManager();
    public static final AuthManager authManager = new AuthManager();
    public static final CommandRunner commandRunner = new CommandRunner();

    public static void main(String[] args) {
        var commands = List.of(
                Clear.class,
                ExecuteScript.class,
                Exit.class,
                FilterByEndDate.class,
                Help.class,
                History.class,
                Info.class,
                Insert.class,
                Login.class,
                Logout.class,
                PrintAscending.class,
                Register.class,
                RemoveAnyByEndDate.class,
                RemoveGreater.class,
                RemoveGreaterKey.class,
                RemoveKey.class,
                Show.class,
                Update.class
        );

        ClientCommand.bulkRegister(commands);

        commandRunner.setMaxRecursionLevel(ConfigReader.getConfig().clientConfig().maxRecursionLevel());
        commandRunner.run();
    }
}
