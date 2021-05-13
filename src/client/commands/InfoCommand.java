package client.commands;

import client.util.Interactor;
import client.RequestSender;
import common.exceptions.WrongAmountOfArgumentsException;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

/**
 * 'info' command. Prints information about the collection.
 */
public class InfoCommand extends Command {

    public InfoCommand(RequestSender requestSender) {
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

            Request<String> request = new Request<>(getName(), null);
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
        Request<String> request = new Request<>(getName(), null);
        return requestSender.sendRequest(request);
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "вывести информацию о коллекции";
    }
}
