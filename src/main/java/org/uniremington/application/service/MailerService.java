package org.uniremington.application.service;

import jakarta.inject.Inject;
import org.uniremington.application.service.interfaces.IMailer;
import org.uniremington.domain.repository.MailerRepository;

public class MailerService implements IMailer {

    MailerRepository repository;

    @Inject MailerService(MailerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sendReset(String template, String message, String addreses, String username, String password) {

        String html = "<html>\n" +
                "  <body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;\">\n" +
                "    <div style=\"background-color: #ffffff; padding: 20px; border-radius: 5px;\">\n" +
                "      <h2 style=\"color: #333;\">¡Hola, " + username + "!</h2>\n" +
                "      <p>Hemos recibido tu solicitud de reseteo de clave.</p>\n" +
                "      <p><strong>Tu contraseña ha sido cambiada con éxito: " + password + ".</strong></p>\n" +
                "      <p style=\"margin-top: 30px;\">Saludos,<br/>Equipo de Soporte</p>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";

        repository.send(html, message, addreses);

    }

}
