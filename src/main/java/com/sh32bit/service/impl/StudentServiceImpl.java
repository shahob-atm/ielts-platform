package com.sh32bit.service.impl;

import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.LinkToken;
import com.sh32bit.entity.ParentProfile;
import com.sh32bit.entity.User;
import com.sh32bit.exception.ConflictException;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.repository.LinkTokenRepository;
import com.sh32bit.repository.ParentProfileRepository;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final UserRepository userRepository;
    private final LinkTokenRepository linkTokenRepository;
    private final ParentProfileRepository parentProfileRepository;

    @Transactional
    public MessageResponse linkParent(String token, String childEmail) {
        LinkToken linkToken = linkTokenRepository.findByToken(token)
                .orElseThrow(() -> new ConflictException("Invalid token"));

        if (linkToken.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Token expired");
        }

        User child = userRepository.findByEmail(childEmail)
                .orElseThrow(() -> new NotFoundException(childEmail));

        if (!linkToken.getChildId().equals(child.getId())) {
            throw new ConflictException("Token does not match this user");
        }

        ParentProfile parentProfile = parentProfileRepository.findByUserId(linkToken.getParentId())
                .orElseThrow(() -> new NotFoundException("ParentProfile userId " + linkToken.getParentId()));

        if (parentProfile.getChildren().contains(child)) {
            throw new ConflictException("This child is already linked to the parent");
        }

        parentProfile.getChildren().add(child);
        parentProfileRepository.save(parentProfile);
        log.info("Student and Parent is connected");

        linkTokenRepository.delete(linkToken);
        log.info("linkToken deleted: {}", linkToken);

        return new MessageResponse("Parent linked successfully");
    }
}
