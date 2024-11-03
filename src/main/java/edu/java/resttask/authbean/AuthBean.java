package edu.java.resttask.authbean;

import edu.java.resttask.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthBean {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clear(){
        this.user = null;
    }
}
