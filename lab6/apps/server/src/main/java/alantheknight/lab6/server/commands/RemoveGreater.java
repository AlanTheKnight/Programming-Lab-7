package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;

import java.sql.SQLException;

import static alantheknight.lab6.server.Main.collectionManager;

/**
 * Command for removing all elements greater than the given one.
 */
public class RemoveGreater extends ServerCommand {
    public RemoveGreater() {
        super(CommandType.REMOVE_GREATER);
    }

    @Override
    public void validateRequest(Request request) throws RequestValidationException {
        if (request.doesNotHavePayload()) throw new RequestValidationException("Отсутствует тело запроса");
        if (request.getPayload().endDate() == null) throw new RequestValidationException("Отсутствует дата окончания");
    }

    @Override
    public Response<Void> apply(Request request) throws Exception {
        var endDate = request.getPayload().endDate();
        int removed = collectionManager.removeGreaterEndDate(endDate, getVerifiedUserId());
        if (removed == 0)
            return new Response<>(getCommandType(), "Элементы с датой окончания больше " + endDate + " не найдены", false);
        return new Response<>(getCommandType(), "Удалено " + removed + " элементов с датой окончания больше " + endDate);
    }
}
