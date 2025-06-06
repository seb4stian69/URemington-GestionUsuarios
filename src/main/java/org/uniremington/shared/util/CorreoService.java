package org.uniremington.shared.util;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CorreoService {

    Mailer mailer;

    @Inject
    public CorreoService(Mailer mailer){
        this.mailer = mailer;
    }

    public void enviarCorreo(String username, String password, String toEmail) {

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

        mailer.send(
            Mail.withHtml(
                toEmail, // Reemplaza por el destinatario real
                "Clave de acceso recuperada",
                html
            )
        );
    }

}
