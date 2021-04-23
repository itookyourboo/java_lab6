package client.commands;

import client.util.CommandManager;
import client.util.Interactor;
import client.RequestSender;
import common.exceptions.NoAccessToFileException;
import common.exceptions.ScriptRecursionException;
import common.exceptions.WrongAmountOfArgumentsException;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 'execute_script' command. Executes scripts from a file.
 */
public class ExecuteScriptCommand extends Command {

    private CommandManager commandManager;

    private static Set<String> executingScripts = new HashSet<>();

    public ExecuteScriptCommand(CommandManager commandManager, RequestSender requestSender) {
        super(requestSender);
        this.commandManager = commandManager;
    }

    /**
     * Executes the script.
     * @return Command execute status.
     */
    @Override
    public void execute(String argument) {
        try {
            if (argument.isEmpty()) throw new WrongAmountOfArgumentsException();
            File script = new File(argument);
            if (!script.exists()) throw new NoSuchFileException("Файла не существует");
            if (!script.canRead()) throw new NoAccessToFileException();
            if (executingScripts.contains(argument)) throw new ScriptRecursionException();

            Scanner fileScanner = new Scanner(new BufferedInputStream(new FileInputStream(script)));
            Interactor.println("Выполняю скрипт '" + argument + "'...");
            executingScripts.add(argument);
            commandManager.runScript(new CommandManager(requestSender, fileScanner));
            executingScripts.remove(argument);
            Interactor.println("Выполнение скрипта '" + argument + "' завершено");
        } catch (WrongAmountOfArgumentsException exception) {
            Interactor.println("Использование: '" + getName() + "'");
        } catch (NoSuchFileException | FileNotFoundException exception) {
            Interactor.println(exception.getMessage());
        } catch (ScriptRecursionException e) {
            Interactor.println("Этот скрипт уже исполняется!");
        } catch (NoAccessToFileException e) {
            Interactor.println("Нет прав на чтение файла");
        }
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "исполнить скрипт из указанного файла";
    }
}
