package client.commands;

import client.util.Interactor;
import client.RequestSender;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.model.StudyGroup;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.time.ZonedDateTime;
import java.util.Scanner;

/**
 * 'add' command. Adds a new element to the collection.
 */
public class AddCommand extends Command {
    private Scanner scanner;

    public AddCommand(RequestSender requestSender, Scanner scanner) {
        super(requestSender);
        this.scanner = scanner;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }

    @Override
    public void execute(String stringArgument) {
        try {
            if (!stringArgument.isEmpty()) throw new WrongAmountOfArgumentsException();
            StudyGroup studyGroup = new StudyGroup(
                    Interactor.askGroupName(scanner),
                    Interactor.askCoordinates(scanner),
                    ZonedDateTime.now(),
                    Interactor.askStudentsCount(scanner),
                    Interactor.askExpelledStudents(scanner),
                    Interactor.askShouldBeExpelled(scanner),
                    Interactor.askFormOfEducation(scanner),
                    Interactor.askGroupAdmin(scanner)
            );

            Request<StudyGroup> request = new Request<>(getName(), studyGroup);
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
}
