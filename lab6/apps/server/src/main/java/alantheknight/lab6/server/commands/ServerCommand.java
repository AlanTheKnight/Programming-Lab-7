package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.BaseCommand;
import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;
import alantheknight.lab6.server.managers.DBManager;
import org.apache.logging.log4j.Level;

import java.sql.SQLException;
import java.util.List;

import static alantheknight.lab6.server.Main.*;


public abstract class ServerCommand extends BaseCommand {
    private Integer verifiedUserId = null;

    public ServerCommand(CommandType commandType) {
        super(commandType);
    }

    public static void bulkRegister(List<Class<? extends ServerCommand>> commands) {
        for (Class<? extends ServerCommand> command : commands) {
            try {
                commandManager.register(command.getConstructor().newInstance());
            } catch (Exception e) {
                logger.log(Level.ERROR, "Failed to register command: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public Integer getVerifiedUserId() {
        return verifiedUserId;
    }

    private void validate(Request request) throws RequestValidationException {
        if (request.getCommand() != getCommandType()) {
            throw new RequestValidationException("Неверный тип команды");
        }

        if (!request.getPayload().hasCredentials()) {
            throw new RequestValidationException("Требуются данные для авторизации");
        }

        if (getCommandType() != CommandType.REGISTER) {
            try {
                verifiedUserId = dbManager.verifyCredentials(request.getPayload().credentials());
            } catch (SQLException | DBManager.InvalidCredentialsException e) {
                throw new RequestValidationException("Ошибка при проверке учетных данных: " + e.getMessage());
            }
        }

        validateRequest(request);
    }

    public void validateRequest(Request request) throws RequestValidationException {
    }

    public abstract Response<?> apply(Request request) throws Exception;

    public Response<?> execute(Request request) {
        try {
            validate(request);
            return apply(request);
        } catch (RequestValidationException e) {
            return new Response<>(getCommandType(), e.getMessage(), false);
        } catch (Exception e) {
            return new Response<>(getCommandType(), "Ошибка при выполнении команды: " + e.getMessage(), false);
        }
    }

    public static class RequestValidationException extends Exception {
        public RequestValidationException(String message) {
            super(message);
        }
    }
}
