package com.sh32bit.event;

import com.sh32bit.entity.User;
import lombok.Getter;

@Getter
public class ChildInvitedEvent {
    private final User parent;
    private final User child;
    private final String token;

    public ChildInvitedEvent(User parent, User child, String token) {
        this.parent = parent;
        this.child = child;
        this.token = token;
    }
}
