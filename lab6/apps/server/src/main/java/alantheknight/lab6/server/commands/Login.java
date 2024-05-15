package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;

/**
 * Command for adding an element to the collection.
 */
public class Login extends ServerCommand {
    public Login() {
        super(CommandType.LOGIN);
    }

    @Override
    public Response<Void> apply(Request request) {
        return new Response<>(getCommandType(), "Вы успешно вошли в аккаунт!");
    }
}
