package org.uniremington.infrastructure.repository;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.uniremington.domain.repository.MailerRepository;


@ApplicationScoped
public class QuarkusMailerRepository implements MailerRepository {

    Mailer mailer;

    @Inject
    QuarkusMailerRepository(Mailer mailer){
        this.mailer = mailer;
    }

    @Override
    public void send(String template, String message, String addreses) {
        this.mailer.send( Mail.withHtml(addreses, message, template) );
    }

}
