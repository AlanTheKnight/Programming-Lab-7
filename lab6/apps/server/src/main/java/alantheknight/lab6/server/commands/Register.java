package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;

import static alantheknight.lab6.server.Main.dbManager;


public class Register extends ServerCommand {
    public Register() {
        super(CommandType.REGISTER);
    }

    @Override
    public Response<Void> apply(Request request) throws Exception {
        dbManager.createNewUser(request.getPayload().credentials());
        return new Response<>(getCommandType(), "Пользователь успешно зарегистрирован");
    }
}
