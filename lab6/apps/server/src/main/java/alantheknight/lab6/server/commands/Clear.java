package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;

import static alantheknight.lab6.server.Main.collectionManager;

/**
 * Command for clearing the collection.
 */
public class Clear extends ServerCommand {
    public Clear() {
        super(CommandType.CLEAR);
    }

    @Override
    public Response<Void> apply(Request request) throws Exception {
        collectionManager.clear();
        return new Response<>(getCommandType(), "Коллекция успешно очищена");
    }
}
