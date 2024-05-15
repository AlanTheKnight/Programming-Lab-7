package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;

import java.sql.SQLException;

import static alantheknight.lab6.server.Main.collectionManager;

/**
 * Command for removing all elements with keys greater than the specified one.
 */
public class RemoveGreaterKey extends ServerCommand {
    public RemoveGreaterKey() {
        super(CommandType.REMOVE_GREATER_KEY);
    }

    @Override
    public void validateRequest(Request request) throws RequestValidationException {
        if (request.doesNotHavePayload()) throw new RequestValidationException("Отсутствует тело запроса");
        if (request.getPayload().key() == null) throw new RequestValidationException("Отсутствует ключ");
    }

    @Override
    public Response<Void> apply(Request request) throws Exception {
        var key = request.getPayload().key();
        int removed = collectionManager.removeGreaterKey(key, getVerifiedUserId());
        return new Response<>(getCommandType(), "Удалено " + removed + " элементов с ключом больше " + key);
    }
}
