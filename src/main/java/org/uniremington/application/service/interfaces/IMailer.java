package org.uniremington.application.service.interfaces;

public interface IMailer {
    void sendReset(String addreses, String username, String password);
}
