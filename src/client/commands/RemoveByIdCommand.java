package client.commands;

import client.util.Interactor;
import client.RequestSender;
import common.exceptions.WrongAmountOfArgumentsException;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

/**
 * 'remove_by_id' command. Removes the element by its ID.
 */
public class RemoveByIdCommand extends Command {
    public RemoveByIdCommand(RequestSender requestSender) {
        super(requestSender);
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по ID";
    }

    @Override
    public void execute(String stringArgument) {
        try {
            if (stringArgument.isEmpty()) throw new WrongAmountOfArgumentsException();
            Integer id = Integer.parseInt(stringArgument);

            Request<Integer> request = new Request<>(getName(), id);
            CommandResult result = requestSender.sendRequest(request);
            if (result.status == ResultStatus.OK)
                Interactor.println(result.message);
            else
                Interactor.printError(result.message);
        } catch (WrongAmountOfArgumentsException exception) {
            Interactor.println("Использование: '" + getName() + "'");
        } catch (NumberFormatException exception) {
            Interactor.printError("Ожидается число типа Integer.");
        }
    }

    @Override
    public CommandResult executeWithObjectArgument(Object... objects) {
        Integer id = (Integer) objects[0];
        Request<Integer> request = new Request<>(getName(), id);
        return requestSender.sendRequest(request);
    }
}
