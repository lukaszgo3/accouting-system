package pl.coderstrust.model;

public interface EmailSendable {

  void sendEmail(String emailAddress, String subject, String content);
}
