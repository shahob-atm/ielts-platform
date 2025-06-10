package com.sh32bit.service.impl;

import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.LinkToken;
import com.sh32bit.entity.User;
import com.sh32bit.event.ChildInvitedEvent;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.repository.LinkTokenRepository;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.service.ParentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final UserRepository userRepository;
    private final LinkTokenRepository linkTokenRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public MessageResponse inviteChild(String parentEmail, String childEmail) {
        User child = userRepository.findByEmail(childEmail)
                .orElseThrow(() -> new NotFoundException("Child user not found: " + childEmail));

        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new NotFoundException("Parent user not found: " + parentEmail));

        String token = UUID.randomUUID().toString();

        LinkToken linkToken = LinkToken.builder()
                .token(token)
                .parentId(parent.getId())
                .childId(child.getId())
                .createdAt(LocalDateTime.now())
                .build();

        linkTokenRepository.save(linkToken);
        log.info("Child link token: {}", linkToken);

        eventPublisher.publishEvent(new ChildInvitedEvent(parent, child, token));
        log.info("Invitation sent to child {}", child);

        return new MessageResponse("Invitation sent to child");
    }
}
