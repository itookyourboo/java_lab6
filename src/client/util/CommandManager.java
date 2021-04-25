package client.util;

import client.RequestSender;
import client.commands.*;
import common.net.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandManager {
    /**
     * Все доступные в программе команды
     */
    protected HashMap<String, Command> commands = new HashMap<String, Command>();
    private Scanner scanner;
    private RequestSender requestSender;

    public CommandManager(RequestSender requestSender, Scanner scanner){
        registerCommand(new AddCommand(requestSender, scanner));
        registerCommand(new AddIfMinCommand(requestSender, scanner));
        registerCommand(new ClearCommand());
        registerCommand(new CountByGroupAdminCommand(requestSender, scanner));
        registerCommand(new ExecuteScriptCommand(this, requestSender));
        registerCommand(new ExitCommand());
        registerCommand(new FilterGreaterThanExpelledStudentsCommand(requestSender));
        registerCommand(new HelpCommand(this));
        registerCommand(new InfoCommand(requestSender));
        registerCommand(new LoginCommand(requestSender, scanner));
        registerCommand(new RegisterCommand(requestSender, scanner));
        registerCommand(new RemoveByIdCommand(requestSender));
        registerCommand(new RemoveGreaterCommand(requestSender, scanner));
        registerCommand(new RemoveLowerCommand(requestSender, scanner));
        registerCommand(new ShowCommand(requestSender));
        registerCommand(new UpdateCommand(requestSender, scanner));

        this.requestSender = requestSender;
        this.scanner = scanner;
    }

    public void parseAndExecute(String input) throws Exception {
        String[] args = input.trim().split(" ");
        if (args.length == 0)
            Interactor.println("Введённая строка пуста.");

        String commandString = args[0];
        if (!commands.containsKey(commandString))
            throw new IndexOutOfBoundsException(String.format("Команды \"%s\" нет.", commandString));

        try{
            Command command = commands.get(commandString);
            if (requestSender.getUser() != null || command.canExecuteIfNotAuthed())
                commands.get(commandString).execute(input.replace(commandString, "").trim());
            else
                Interactor.printError("Команда доступна только авторизованным пользователям.");
        } catch (Exception e){
            Interactor.printError(e.getMessage());
        }
    }

    void registerCommand(Command newCommand){
        if(commands.containsKey(newCommand.getName())){
            throw new IllegalArgumentException("Команда с таким именем уже существует!");
        }

        commands.put(newCommand.getName(), newCommand);
    }

    public String getCommandsNameWithDescription(){
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Command> e : commands.entrySet()) {
            Command command = e.getValue();
            result.append(command.getName())
                    .append(" - ")
                    .append(command.getDescription())
                    .append("\n");
        }
        return result.toString();
    }

    public void runScript(CommandManager cm) {
        String input;
        Interactor.fileMode = true;
        do {
            if (!cm.scanner.hasNextLine()) return;
            input = cm.scanner.nextLine();
            try {
                cm.parseAndExecute(input);
            } catch (Exception e) {
                Interactor.printError(e.getMessage());
            }
            Interactor.println("");
        } while (!input.equals("exit"));
        Interactor.fileMode = false;
    }
}