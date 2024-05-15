package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;

import java.sql.SQLException;

import static alantheknight.lab6.server.Main.collectionManager;

public class RemoveAnyByEndDate extends ServerCommand {
    public RemoveAnyByEndDate() {
        super(CommandType.REMOVE_ANY_BY_END_DATE);
    }

    @Override
    public void validateRequest(Request request) throws RequestValidationException {
        if (request.doesNotHavePayload()) throw new RequestValidationException("Отсутствует тело запроса");
        if (request.getPayload().endDate() == null) throw new RequestValidationException("Отсутствует дата окончания");
    }

    @Override
    public Response<Void> apply(Request request) throws Exception {
        var endDate = request.getPayload().endDate();
        int removed = collectionManager.removeWorkerByEndDate(endDate, getVerifiedUserId());
        if (removed == 0)
            return new Response<>(getCommandType(), "Элемент с датой окончания " + endDate + " не найден", false);
        return new Response<>(getCommandType(), "Элемент с датой окончания " + endDate + " успешно удалён");
    }
}
