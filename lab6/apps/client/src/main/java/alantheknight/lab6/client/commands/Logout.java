package alantheknight.lab6.client.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.fields.handlers.InputHandler;

import static alantheknight.lab6.client.Main.authManager;
import static alantheknight.lab6.client.Main.stdConsole;

public class Logout extends ClientCommand<Void> {
    public Logout() {
        super(CommandType.LOGOUT, "Выйти из аккаунта", "logout");
    }

    @Override
    public boolean apply(String[] arguments) throws CommandExecutionException, InputHandler.InputException {
        if (!authManager.isAuthorized()) throw new CommandExecutionException("Вы не авторизованы");
        authManager.clearCredentials();
        stdConsole.printSuccess("Вы вышли из аккаунта");
        return true;
    }
}
