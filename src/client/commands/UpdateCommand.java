package client.commands;

import java.time.ZonedDateTime;
import java.util.Scanner;

import client.util.Interactor;
import client.RequestSender;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.model.StudyGroup;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

/**
 * 'update' command. Updates the information about selected studyGroup.
 */
public class UpdateCommand extends Command {

    public UpdateCommand(RequestSender requestSender) {
        super(requestSender);
    }

    public UpdateCommand(RequestSender requestSender, Scanner scanner) {
        super(requestSender, scanner);
    }

    /**
     * Executes the command.
     * @return Command execute status.
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongAmountOfArgumentsException();

            Integer id = Integer.parseInt(argument);
            StudyGroup studyGroup = new StudyGroup(
                    id,
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
        } catch (NumberFormatException exception) {
            Interactor.printError("ID должен быть представлен числом!");
        } catch (IncorrectInputInScriptException exception) {
            Interactor.printError("Не удалось выполнить скрипт.");
        }
    }

    @Override
    public CommandResult executeWithObjectArgument(Object... objects) {
        StudyGroup studyGroup = StudyGroup.fromObjects(objects);
        studyGroup.setId((Integer) objects[objects.length - 1]);
        Request<StudyGroup> request = new Request<>(getName(), studyGroup);
        return requestSender.sendRequest(request);
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции по ID";
    }
}
