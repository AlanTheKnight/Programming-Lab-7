package alantheknight.lab6.client.commands;

import alantheknight.lab6.common.commands.CommandType;

public class Register extends Login {
    public Register() {
        super(CommandType.REGISTER, "Создать новый аккаунт", "register");
    }
}
