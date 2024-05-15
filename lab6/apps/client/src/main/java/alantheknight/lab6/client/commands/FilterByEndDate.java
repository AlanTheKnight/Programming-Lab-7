package alantheknight.lab6.client.commands;

import alantheknight.lab6.common.commands.CommandType;
import alantheknight.lab6.common.network.Request;
import alantheknight.lab6.common.network.RequestPayload;

import java.time.LocalDate;

import static alantheknight.lab6.client.Main.authManager;
import static alantheknight.lab6.client.Main.stdConsole;

public class FilterByEndDate extends Show {
    public FilterByEndDate() {
        super(CommandType.FILTER_BY_END_DATE, "Вывести элементы, значение поля endDate которых равно заданному",
                "filter_by_end_date yyyy-mm-dd");
    }

    @Override
    public boolean apply(String[] arguments) throws CommandExecutionException {
        String endDate = arguments[1];
        if (!endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            stdConsole.printError("Дата должна быть в формате yyyy-mm-dd");
            return false;
        }
        LocalDate date = LocalDate.parse(endDate);
        Request request = new Request(getCommandType(), new RequestPayload(date, authManager.getCredentials()));
        var response = sendRequestAndHandleResponse(request);
        stdConsole.println(response.getMessage());
        response.getPayload().forEach(worker -> stdConsole.println(worker.toString()));
        return true;
    }
}
