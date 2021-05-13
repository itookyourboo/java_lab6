package client.commands;


import client.util.Interactor;
import common.net.CommandResult;

/**
 * 'exit' command. Closes the program.
 */
public class ExitCommand extends Command {

    /**
     * Executes the command.
     * @return Command execute status.
     */
    @Override
    public void execute(String argument) {
        Interactor.println("Выход.");
    }

    @Override
    public CommandResult executeWithObjectArgument(Object... objects) {
        return null;
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }
}
