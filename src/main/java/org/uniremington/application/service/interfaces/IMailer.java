package org.uniremington.application.service.interfaces;

public interface IMailer {
    void sendReset(String template, String message, String addreses, String username, String password);
}
