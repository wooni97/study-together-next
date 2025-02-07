package dev.flab.studytogether.infra.mail;

public interface MailClient {
    void sendMail(String subject, String content, String receiverEmailAddress);
}
