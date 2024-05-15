package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;

import static alantheknight.lab6.server.Main.collectionManager;


public class Insert extends ServerCommand {
    public Insert() {
        super(CommandType.INSERT);
    }

    @Override
    public void validateRequest(Request request) throws RequestValidationException {
        var payloadElement = request.getPayload().element();
        if (payloadElement == null) throw new RequestValidationException("Отсутствует элемент");
        if (!payloadElement.validate()) throw new RequestValidationException("Элемент не прошел валидацию");
    }

    @Override
    public Response<Void> apply(Request request) throws Exception {
        var payload = request.getPayload();
        collectionManager.insertWorker(payload.element(), getVerifiedUserId());
        return new Response<>(getCommandType(), "Элемент с id " + payload.element().getId() + " успешно добавлен");
    }
}
