package server.execution;

import common.DataManager;
import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;

import java.util.HashMap;

public class ExecutionService {
    private HashMap<String, Executable> commands = new HashMap<>();
    private DataManager dataManager;

    public ExecutionService(DataManager dataManager) {
        this.dataManager = dataManager;
        initCommands();
    }

    private void initCommands() {
        commands.put("add", dataManager::add);
        commands.put("add_if_min", dataManager::addIfMin);
        commands.put("clear", dataManager::clear);
        commands.put("count_by_group_admin", dataManager::countByGroupAdmin);
        commands.put("info", dataManager::info);
        commands.put("filter_greater_than_expelled_students", dataManager::filterGreaterThanExpelledStudents);
        commands.put("remove_all_by_should_be_expelled", dataManager::removeAllByShouldBeExpelled);
        commands.put("remove_by_id", dataManager::removeById);
        commands.put("remove_greater", dataManager::removeGreater);
        commands.put("remove_lower", dataManager::removeLower);
        commands.put("update", dataManager::update);
        commands.put("show", dataManager::show);
    }

    public CommandResult executeCommand(Request<?> request) {
        if (!commands.containsKey(request.command))
            return new CommandResult(ResultStatus.ERROR, "Такой команды на сервере нет.");
        return commands.get(request.command).execute(request);
    }
}
