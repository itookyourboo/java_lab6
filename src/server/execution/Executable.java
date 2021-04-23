package server.execution;

import common.net.CommandResult;
import common.net.Request;

public interface Executable {
    CommandResult execute(Request<?> request);
}
