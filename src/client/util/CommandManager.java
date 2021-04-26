package client.util;

import client.RequestSender;
import client.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandManager {
    /**
     * Все доступные в программе команды
     */
    protected HashMap<String, Command> commands = new HashMap<String, Command>();
    private Scanner scanner;

    public CommandManager(RequestSender con, Scanner scanner){
        registerCommand(new AddCommand(con, scanner));
        registerCommand(new AddIfMinCommand(con, scanner));
        registerCommand(new ClearCommand());
        registerCommand(new CountByGroupAdminCommand(con, scanner));
        registerCommand(new ExecuteScriptCommand(this, con));
        registerCommand(new ExitCommand());
        registerCommand(new FilterGreaterThanExpelledStudentsCommand(con));
        registerCommand(new HelpCommand(this));
        registerCommand(new InfoCommand(con));
        registerCommand(new RemoveByIdCommand(con));
        registerCommand(new RemoveGreaterCommand(con, scanner));
        registerCommand(new RemoveLowerCommand(con, scanner));
        //registerCommand(new SaveCommand(con));
        registerCommand(new ShowCommand(con));
        registerCommand(new UpdateCommand(con, scanner));

        this.scanner = scanner;
    }

    public void parseAndExecute(String input) throws Exception {
        String[] args = input.trim().split(" ");
        if (args.length == 0)
            Interactor.println("Введённая строка пуста.");

        String command = args[0];
        if (!commands.containsKey(command))
            throw new IndexOutOfBoundsException(String.format("Команды \"%s\" нет.", command));

        try{
            commands.get(command).execute(input.replace(command, "").trim());
        } catch (Exception e){
            e.printStackTrace();
            Interactor.printError("Произошла ошибка: " + e.getMessage());
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