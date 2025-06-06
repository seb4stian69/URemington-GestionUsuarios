package org.uniremington.domain.repository;

public interface MailerRepository {
    void send(String template, String message, String addreses);
}
