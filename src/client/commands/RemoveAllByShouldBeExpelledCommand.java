package client.commands;


import client.util.Interactor;
import common.exceptions.WrongAmountOfArgumentsException;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

/**
 * 'remove_all_by_should_be_expelled' command. Removes elements with sholdBeExpelled value user entered.
 */
public class RemoveAllByShouldBeExpelledCommand extends Command{

    /**
     * Executes the command.
     * @return Command execute status.
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongAmountOfArgumentsException();
            Long shouldBeExpelled = Long.parseLong(argument);

            Request<Long> request = new Request<>(getName(), shouldBeExpelled);
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
    public String getName() {
        return "remove_all_by_should_be_expelled";
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, значение поля shouldBeExpelled которого эквивалентно заданному";
    }
}
