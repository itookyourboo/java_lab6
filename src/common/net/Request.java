package common.net;

import common.model.User;

import java.io.Serializable;

public class Request<T>  implements Serializable {
    public final String command;
    public User user;
    public final T entity;

    public Request(String command, T entity) {
        this.command = command;
        this.entity = entity;
    }
}
