package client.commands;


import client.util.Interactor;
import client.RequestSender;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.model.Location;
import common.model.Person;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.util.Scanner;

/**
 * 'count_by_group_admin_command' command. Prints amount of groups with the same admin
 */
public class CountByGroupAdminCommand extends Command{

    public CountByGroupAdminCommand(RequestSender requestSender) {
        super(requestSender);
    }

    public CountByGroupAdminCommand(RequestSender requestSender, Scanner scanner) {
        super(requestSender, scanner);
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public void execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfArgumentsException();
            Person admin = new Person(
                    Interactor.askAdminName(scanner),
                    Interactor.askWeight(scanner),
                    Interactor.askPassportID(scanner),
                    Interactor.askLocation(scanner)
            );

            Request<Person> request = new Request<>(getName(), admin);
            CommandResult result = requestSender.sendRequest(request);
            if (result.status == ResultStatus.OK)
                Interactor.println(result.message);
            else
                Interactor.printError(result.message);
        } catch (WrongAmountOfArgumentsException exception) {
            Interactor.println("Использование: '" + getName() + "'");
        } catch (IncorrectInputInScriptException exception) {
            Interactor.printError("Не удалось выполнить скрипт.");
        }
    }

    @Override
    public CommandResult executeWithObjectArgument(Object... objects) {
        Person admin = new Person(
                (String) objects[0],            // AdminName
                (long) objects[1],              // AdminWeight
                (String) objects[2],            // PassportID
                new Location(
                        (int) objects[3],      // X
                        (int) objects[4],      // Y
                        (String) objects[5]    // LocationName
                )
        );

        Request<Person> request = new Request<>(getName(), admin);
        return requestSender.sendRequest(request);
    }

    @Override
    public String getName() {
        return "count_by_group_admin";
    }

    @Override
    public String getDescription() {
        return "вывести количество элементов, значение поля groupAdmin которых равно заданному";
    }
}
