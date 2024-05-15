package alantheknight.lab6.client.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.RequestPayload;
import alantheknight.lab6.common.network.Response;

import static alantheknight.lab6.client.Main.authManager;
import static alantheknight.lab6.client.Main.stdConsole;

public class Clear extends ClientCommand<Void> {
    public Clear() {
        super(CommandType.CLEAR, "Очистить коллекцию", "clear");
    }

    @Override
    public boolean apply(String[] arguments) throws CommandExecutionException {
        Request request = new Request(getCommandType(), new RequestPayload(authManager.getCredentials()));
        Response<Void> response = sendRequestAndHandleResponse(request);
        stdConsole.printSuccess(response.getMessage());
        return true;
    }
}
