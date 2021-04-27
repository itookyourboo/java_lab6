package server.handlers;

import common.net.CommandResult;
import common.net.Request;
import common.net.ResultStatus;
import server.execution.CommandExecutionService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class RequestHandler {

    private CommandExecutionService commandExecutionService;

    public RequestHandler(CommandExecutionService commandExecutionService) {
        this.commandExecutionService = commandExecutionService;
    }

    public CommandResult handleRequest(Request<?> request, ExecutorService handleRequestThread) {
        RequestExecutor requestExecutor = new RequestExecutor(request);
        Future<CommandResult> commandResultFuture = handleRequestThread.submit(requestExecutor);
        while (true) {
            if (commandResultFuture.isDone()) {
                try {
                    return commandResultFuture.get();
                } catch (InterruptedException | ExecutionException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private class RequestExecutor implements Callable<CommandResult> {

        private Request<?> request;

        public RequestExecutor(Request<?> request) {
            this.request = request;
        }

        @Override
        public CommandResult call() {
            CommandResult result = commandExecutionService.executeCommand(request);
            if (result.status == ResultStatus.OK)
                System.out.println("Команда выполнена успешно");
            else
                System.out.println("Команда выполнена неуспешно");
            return result;
        }
    }
}
