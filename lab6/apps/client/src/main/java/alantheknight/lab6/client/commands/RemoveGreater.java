package alantheknight.lab6.client.commands;

import alantheknight.lab6.common.commands.CommandType;

public class RemoveGreater extends RemoveAnyByEndDate {
    public RemoveGreater() {
        super(CommandType.REMOVE_GREATER, "Удалить элементы, превышающие заданный", "remove_greater yyyy-mm-dd");
    }
}
