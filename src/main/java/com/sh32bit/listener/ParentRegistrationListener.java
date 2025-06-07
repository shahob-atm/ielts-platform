package com.sh32bit.listener;

import com.sh32bit.entity.User;
import com.sh32bit.event.ParentRegisteredEvent;
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
public class ParentRegistrationListener {
    private final EmailService emailService;

    @Async
    @EventListener
    public void handleParentRegistered(ParentRegisteredEvent event) throws MessagingException {
        User user = event.getUser();
        String activationLink = event.getActivationLink();

        emailService.send(
                user.getEmail(),
                "Activate your parent account",
                "<p>Hello, please activate your account: <a href=\"" + activationLink + "\">Activate</a></p>"
        );
        log.info("Token sent to: {}", user.getEmail());
    }
}
