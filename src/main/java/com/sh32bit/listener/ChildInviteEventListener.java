package com.sh32bit.listener;

import com.sh32bit.event.ChildInvitedEvent;
import com.sh32bit.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChildInviteEventListener {
    private final EmailService emailService;

    @EventListener
    @Async
    public void onChildInvited(ChildInvitedEvent event) throws MessagingException {
        String activationLink =  "http://localhost:8080/api/v1/childs/parent/link?token=" + event.getToken();
        log.info("Child Invited: {}", activationLink);

        emailService.send(
                event.getChild().getEmail(),
                "Access for your parent",
                "<p>Hello, please access for your parent: <a href=\"" + activationLink + "\">Activate</a></p>"
        );
    }
}
