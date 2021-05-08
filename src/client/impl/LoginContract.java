package client.impl;

public interface LoginContract {
    void onLoginClicked(String username, String password);
    void onRegisterClicked(String username, String password);
}
