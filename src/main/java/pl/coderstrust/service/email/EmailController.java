package pl.coderstrust.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import pl.coderstrust.model.EmailTemplateNames;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;

@Controller
public class EmailController {

  private final EmailService emailService;
  private final TemplateEngine templateEngine;
  private final InvoiceService invoiceService;

  @Autowired
  public EmailController(EmailService emailService, TemplateEngine templateEngine,
      InvoiceService invoiceService) {
    this.emailService = emailService;
    this.templateEngine = templateEngine;
    this.invoiceService = invoiceService;
  }

  @RequestMapping("email/{id}")
  public String send(@PathVariable("id") Long invoiceId,
      @RequestParam(name = "email") String email) {
    Invoice invoice = invoiceService.findEntry(invoiceId);
    EmailTemplate template = new EmailTemplate(invoice);

    String body = templateEngine.process(EmailTemplateNames.TEMPLATE, template.Template());
    emailService.sendEmail(email, EmailTemplateNames.HEADER_INFO, body);
    return EmailTemplateNames.INDEX;
  }
}