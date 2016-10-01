package me.jin.account;

/**
 * Created by nayoung on 2016. 9. 30..
 */
public class UserDuplicatedException extends RuntimeException {
    String username;

    public UserDuplicatedException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
