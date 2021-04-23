package client.commands;

import client.RequestSender;
import java.util.Scanner;

/**
 * Command 'add_if_min'. Adds a new element to collection if it's less than the minimal one.
 */
public class AddIfMinCommand extends AddCommand {

    public AddIfMinCommand(RequestSender requestSender, Scanner scanner) {
        super(requestSender, scanner);
    }

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент, если его значение меньше, чем у наименьшего";
    }
}
