package alantheknight.lab6.client.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.fields.handlers.InputHandler;
import alantheknight.lab6.common.network.Credentials;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.RequestPayload;

import static alantheknight.lab6.client.Main.authManager;
import static alantheknight.lab6.client.Main.stdConsole;

public class Login extends ClientCommand<Void> {
    public Login() {
        super(CommandType.LOGIN, "Войти в аккаунт", "login");
    }

    public Login(CommandType commandType, String description, String commandFormat) {
        super(commandType, description, commandFormat);
    }

    @Override
    public boolean apply(String[] arguments) throws CommandExecutionException, InputHandler.InputException {
        authManager.inputCredentials();
        Credentials credentials = authManager.getCredentials();
        Request request = new Request(getCommandType(), new RequestPayload(credentials));
        authManager.clearCredentials();
        var response = sendRequestAndHandleResponse(request);
        authManager.setCredentials(credentials);
        stdConsole.printSuccess(response.getMessage());
        return true;
    }
}
