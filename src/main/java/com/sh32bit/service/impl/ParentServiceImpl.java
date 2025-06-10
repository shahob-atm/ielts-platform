package com.sh32bit.service.impl;

import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.dto.response.StudentResponse;
import com.sh32bit.entity.LinkToken;
import com.sh32bit.entity.ParentProfile;
import com.sh32bit.entity.User;
import com.sh32bit.event.ChildInvitedEvent;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.mapper.StudentMapper;
import com.sh32bit.repository.LinkTokenRepository;
import com.sh32bit.repository.ParentProfileRepository;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.service.ParentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final UserRepository userRepository;
    private final LinkTokenRepository linkTokenRepository;
    private final ParentProfileRepository parentProfileRepository;
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

    @Override
    public List<StudentResponse> getChildrenOfParent(String email) {
        User parent = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Parent user not found: " + email));

        ParentProfile parentProfile = parentProfileRepository.findByUserId(parent.getId())
                .orElseThrow(() -> new NotFoundException("Parent profile not found: " + parent.getId()));

        return parentProfile.getChildren().stream().map(StudentMapper::toDTO).collect(Collectors.toList());
    }
}
