package com.sh32bit.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void send(String to, String subject, String htmlText) throws MessagingException;
}
