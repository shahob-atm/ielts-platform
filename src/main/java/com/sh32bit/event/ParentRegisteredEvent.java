package com.sh32bit.event;

import com.sh32bit.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ParentRegisteredEvent extends ApplicationEvent {
    private final User user;
    private final String activationLink;

    public ParentRegisteredEvent(Object source, User user, String activationLink) {
        super(source);
        this.user = user;
        this.activationLink = activationLink;
    }
}
