/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.stamp;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link StCc}.
 *
 * @since 1.3.1
 */
final class StCcTest {

    /**
     * StCC can add a CC recipient to the message.
     * @throws Exception If fails
     */
    @Test
    void addsCopyToMessage() throws Exception {
        final Message msg = new MimeMessage(
            Session.getDefaultInstance(new Properties())
        );
        new StCc("Jeff Петровский", "jeff@gmail.com").attach(msg);
        new StCc("Jeff Again", "jeff+again@gmail.com").attach(msg);
        MatcherAssert.assertThat(
            new InternetAddress(
                msg.getRecipients(Message.RecipientType.CC)[0].toString()
            ).getPersonal(),
            Matchers.containsString("Петровский")
        );
        MatcherAssert.assertThat(
            new InternetAddress(
                msg.getRecipients(Message.RecipientType.CC)[1].toString()
            ).getPersonal(),
            Matchers.containsString("Again")
        );
    }

    /**
     * StCC can add a CC recipient with custom encoding to the message.
     * @throws Exception If fails
     */
    @Test
    void addsCopyToMessageWithCustomCharset() throws Exception {
        final String charset = "KOI8-R";
        final Message msg = new MimeMessage(
            Session.getDefaultInstance(new Properties())
        );
        final String name = "Ivan Иванов";
        final String family = "Иванов";
        new StCc(name, "ivan@gmail.com", charset).attach(msg);
        MatcherAssert.assertThat(
            new InternetAddress(
                msg.getRecipients(Message.RecipientType.CC)[0].toString()
            ).getPersonal(),
            Matchers.containsString(family)
        );
        MatcherAssert.assertThat(
            msg.getRecipients(Message.RecipientType.CC)[0].toString(),
            Matchers.not(Matchers.containsString(family))
        );
        MatcherAssert.assertThat(
            msg.getRecipients(Message.RecipientType.CC)[0].toString(),
            Matchers.not(Matchers.containsString("UTF"))
        );
        MatcherAssert.assertThat(
            msg.getRecipients(Message.RecipientType.CC)[0].toString(),
            Matchers.containsString(charset)
        );
        MatcherAssert.assertThat(
            msg.getRecipients(Message.RecipientType.CC)[0].toString(),
            Matchers.containsString(encodeText(name, charset))
        );
    }

    /**
     * Encode text into MIME with encoding.
     * @param text Text
     * @param charset Text charset
     * @return Encoded text
     * @throws UnsupportedEncodingException if fail
     */
    private static String encodeText(
        final String text,
        final String charset
    ) throws UnsupportedEncodingException {
        final String encoded = MimeUtility.encodeWord(text, charset, null);
        final int last = encoded.lastIndexOf('?');
        final int prev = encoded.lastIndexOf('?', last - 1);
        return encoded.substring(prev + 1, last);
    }

}
