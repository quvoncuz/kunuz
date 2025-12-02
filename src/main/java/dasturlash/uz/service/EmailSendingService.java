package dasturlash.uz.service;

import dasturlash.uz.dto.EmailDTO;
import dasturlash.uz.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingService {
    @Value("${spring.mail.username}")
    private String fromAccount;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService  emailService;

    public void sendRegistrationEmail(String toAccount) {
        int smsCode = RandomUtil.fiveDigit();
        String body = "Your confirmation code: " + smsCode;
        // send
        sendSimpleMessage("Registration complete", body, toAccount);
        // save to db
        // emailHistoryService.create(body, smsCode, toAccount);
    }

    public void sendRegistrationStyledEmail(String name, String toAccount) {
        String smsCode = RandomUtil.fiveDigit()+"";
        String body = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <title>Verification Code</title>\n" +
                "    <style>\n" +
                "        /* General resets */\n" +
                "        body, table, td, a { -webkit-text-size-adjust:100%%; -ms-text-size-adjust:100%%; }\n" +
                "        table, td { mso-table-lspace:0pt; mso-table-rspace:0pt; }\n" +
                "        img { -ms-interpolation-mode:bicubic; }\n" +
                "\n" +
                "        /* Container */\n" +
                "        body { margin:0; padding:0; background-color:#f4f6f8; font-family: 'Helvetica Neue', Arial, sans-serif; }\n" +
                "        .email-wrapper { width:100%%; background-color:#f4f6f8; padding:20px 0; }\n" +
                "        .email-content { width:100%%; max-width:600px; margin:0 auto; background:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 6px 18px rgba(20,30,50,0.08); }\n" +
                "\n" +
                "        /* Header */\n" +
                "        .email-header { padding:24px; text-align:center; background: linear-gradient(90deg, #4f46e5 0%%, #06b6d4 100%%); color:#ffffff; }\n" +
                "        .logo { display:block; margin:0 auto 12px; }\n" +
                "        .brand { font-weight:700; font-size:18px; letter-spacing:0.2px; }\n" +
                "\n" +
                "        /* Body */\n" +
                "        .email-body { padding:28px 32px; color:#0f172a; }\n" +
                "        .greeting { font-size:16px; margin:0 0 12px; }\n" +
                "        .lead { font-size:14px; color:#334155; margin-bottom:22px; line-height:1.5; }\n" +
                "\n" +
                "        /* Code box */\n" +
                "        .code-box { background:#f8fafc; border:1px dashed #e2e8f0; padding:18px; text-align:center; border-radius:8px; margin:12px 0 18px; }\n" +
                "        .code { font-family: 'Courier New', Courier, monospace; font-size:28px; letter-spacing:6px; font-weight:700; color:#0f172a; }\n" +
                "        .code-small { font-size:14px; color:#475569; margin-top:6px; }\n" +
                "\n" +
                "        /* CTA */\n" +
                "        .btn { display:inline-block; padding:12px 20px; border-radius:8px; background:#4f46e5; color:#fff; text-decoration:none; font-weight:600; }\n" +
                "        .support { font-size:12px; color:#94a3b8; margin-top:18px; }\n" +
                "\n" +
                "        /* Footer */\n" +
                "        .email-footer { padding:18px 32px; font-size:12px; color:#98a2b3; text-align:center; }\n" +
                "\n" +
                "        /* Mobile */\n" +
                "        @media screen and (max-width:480px) {\n" +
                "            .email-body { padding:18px 18px; }\n" +
                "            .email-header { padding:18px; }\n" +
                "            .code { font-size:24px; letter-spacing:4px; }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<!-- Preheader text : visible in inbox preview but hidden in email body -->\n" +
                "<span style=\"display:none; font-size:1px; color:#f4f6f8; line-height:1px; max-height:0px; max-width:0px; opacity:0; overflow:hidden;\">Your verification code inside — valid for 3 minutes.</span>\n" +
                "\n" +
                "<table class=\"email-wrapper\" width=\"100%%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "    <tr>\n" +
                "        <td align=\"center\">\n" +
                "            <table class=\"email-content\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\">\n" +
                "\n" +
                "                <!-- Header -->\n" +
                "                <tr>\n" +
                "                    <td class=\"email-header\">\n" +
                "                        <!-- Logo placeholder -->\n" +
                "                        <img src=\"https://via.placeholder.com/80x80.png?text=Logo\" alt=\"Logo\" width=\"64\" height=\"64\" style=\"border-radius:12px; display:block; margin:0 auto 8px;\" />\n" +
                "                        <div class=\"brand\">Your Company</div>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "\n" +
                "                <!-- Body -->\n" +
                "                <tr>\n" +
                "                    <td class=\"email-body\">\n" +
                "                        <p class=\"greeting\">Salom, %s \uD83D\uDC4B</p>\n" +
                "                        <p class=\"lead\">Sizning kirish / tasdiqlash kodingiz quyida berilgan. Ushbu kod 10 daqiqa davomida amal qiladi. Agar siz bu so'rovni amalga oshirmagan bo'lsangiz, iltimos, bu xabarni e'tiborsiz qoldiring.</p>\n" +
                "\n" +
                "                        <div class=\"code-box\">\n" +
                "                            <div class=\"code\">%d</div>\n" +
                "                            <div class=\"code-small\">Enter this code in the app or click the button below.</div>\n" +
                "                        </div>\n" +
                "\n" +
                "                        <p style=\"text-align:center; margin:16px 0 0;\">\n" +
                "                            <a class=\"btn\" href=\"{{ACTION_URL}}\">Verify your account</a>\n" +
                "                        </p>\n" +
                "\n" +
                "                        <p class=\"support\">Agar tugma ishlamasa, quyidagi havolani brauzeringizga joylashtiring:<br /><a href=\"{{ACTION_URL}}\" style=\"color:#0f172a; text-decoration:underline;\">{{ACTION_URL}}</a></p>\n" +
                "\n" +
                "                        <p style=\"margin-top:18px; font-size:13px; color:#6b7280;\">Rahmat,<br/>Your Company jamoasi</p>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "\n" +
                "                <!-- Footer -->\n" +
                "                <tr>\n" +
                "                    <td class=\"email-footer\">\n" +
                "                        You received this email because a request was made for account verification. If you didn't request this, you can safely ignore it.\n" +
                "                        <div style=\"margin-top:8px;\">&copy; <span id=\"year\">2025</span> Your Company. All rights reserved.</div>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "\n" +
                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        body = String.format(body, name, smsCode);
        // send
        sendMimeMessage("Registration complete", body, toAccount);
        // save to db
        emailService.create(new EmailDTO(body, smsCode, toAccount));
    }

    private String sendMimeMessage(String subject, String body, String toAccount) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(msg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "Mail was send";
    }



    public String sendSimpleMessage(String subject, String content, String toAccount) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(toAccount);
        msg.setSubject(subject);
        msg.setText(content);
        javaMailSender.send(msg);

        return "Mail was send";
    }

}
