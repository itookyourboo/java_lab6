package client.commands;

import client.RequestSender;

public abstract class Command {
    protected RequestSender requestSender;

    public Command() {}
    public Command(RequestSender requestSender) {
        this.requestSender = requestSender;
    }

    public abstract String getName();
    public abstract String getDescription();
    public abstract void execute(String stringArgument);
}
