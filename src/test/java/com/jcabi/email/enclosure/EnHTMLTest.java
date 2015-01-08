package com.jcabi.email.enclosure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import org.junit.Assert;
import org.junit.Test;

public class EnHTMLTest {
    @Test
    public void partSetsEncoding() throws MessagingException, IOException {
        final EnHTML enHTML = new EnHTML("<html><body>привет</body></html>");

        final MimeBodyPart part = enHTML.part();

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        part.writeTo(stream);
        final String parttxt = stream.toString();
        Assert.assertEquals("Content-Type: text/html;charset=\"utf-8\"\n" +
            "\n" +
            "<html><body>привет</body></html>", parttxt);
    }
}
