package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;

import java.sql.SQLException;

import static alantheknight.lab6.server.Main.collectionManager;

/**
 * Command for updating the element with the specified id.
 */
public class RemoveKey extends ServerCommand {
    public RemoveKey() {
        super(CommandType.REMOVE_KEY);
    }

    @Override
    public void validateRequest(Request request) throws RequestValidationException {
        if (request.doesNotHavePayload()) throw new RequestValidationException("Отсутствует тело запроса");
        if (request.getPayload().key() == null) throw new RequestValidationException("Отсутствует ключ");
    }

    @Override
    public Response<Void> apply(Request request) throws Exception {
        var key = request.getPayload().key();
        var removed = collectionManager.removeWorkerById(key, getVerifiedUserId());
        if (removed == 0)
            return new Response<>(getCommandType(), "Элемент с id " + key + " не найден", false);
        return new Response<>(getCommandType(), "Элемент с id " + key + " успешно удалён");
    }
}
