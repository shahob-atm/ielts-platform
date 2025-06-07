package com.sh32bit.listener;

import com.sh32bit.entity.User;
import com.sh32bit.event.UserInvitedEvent;
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
public class UserInvitationListener {
    private final EmailService emailService;

    @Async
    @EventListener
    public void handleUserInvited(UserInvitedEvent event) throws MessagingException {
        User user = event.getUser();
        String activationLink = event.getActivationLink();

        emailService.send(
                user.getEmail(),
                "Activate your account",
                "<p>Hello, please activate your account: <a href=\"" + activationLink + "\">Activate</a></p>"
        );
        log.info("Token sent to: {}", user.getEmail());
    }
}
