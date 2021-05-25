package client.commands;

import client.RequestSender;
import client.util.Interactor;
import common.exceptions.WrongAmountOfArgumentsException;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.util.Scanner;

/**
 *  'clear' command. Clears the collection.
 */
public class ClearCommand extends Command {

    public ClearCommand(RequestSender requestSender) {
        super(requestSender);
    }

    /**
     * Executes the command.
     * @return Command execute status.
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfArgumentsException();

            Request<?> request = new Request<String>(getName(), null);
            CommandResult result = requestSender.sendRequest(request);
            if (result.status == ResultStatus.OK)
                Interactor.println(result.message);
            else
                Interactor.printError(result.message);
        } catch (WrongAmountOfArgumentsException exception) {
            Interactor.println("Использование: '" + getName() + "'");
        }
    }

    @Override
    public CommandResult executeWithObjectArgument(Object... objects) {
        Request<?> request = new Request<String>(getName(), null);
        return requestSender.sendRequest(request);
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}
