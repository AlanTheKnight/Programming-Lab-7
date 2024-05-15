package alantheknight.lab6.server.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.models.Worker;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.Response;

import java.util.ArrayList;

import static alantheknight.lab6.server.Main.collectionManager;


public class PrintAscending extends ServerCommand {
    public PrintAscending() {
        super(CommandType.PRINT_ASCENDING);
    }

    @Override
    public Response<ArrayList<Worker>> apply(Request request) {
        ArrayList<Worker> workers = new ArrayList<>(collectionManager.getCollection());
        return new Response<>(getCommandType(), "Список элементов коллекции:", workers);
    }
}
