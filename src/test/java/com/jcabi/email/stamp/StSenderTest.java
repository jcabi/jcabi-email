/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
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
 * Test case for {@link com.jcabi.email.stamp.StSender}.
 *
 * @since 1.3.1
 */
final class StSenderTest {

    /**
     * StSender can add a sender to the message.
     * @throws Exception If fails
     */
    @Test
    void addsSenderToMessage() throws Exception {
        final Message msg = new MimeMessage(
            Session.getDefaultInstance(new Properties())
        );
        new StSender("Jeff Петровский", "jeff@gmail.com").attach(msg);
        MatcherAssert.assertThat(
            new InternetAddress(
                msg.getFrom()[0].toString()
            ).getPersonal(),
            Matchers.containsString("Петровский")
        );
    }

    /**
     * StSender can add a sender with custom encoding to the message.
     * @throws Exception If fails
     */
    @Test
    void addsSenderToMessageWithCustomCharset() throws Exception {
        final String charset = "KOI8-R";
        final Message msg = new MimeMessage(
            Session.getDefaultInstance(new Properties())
        );
        final String name = "Ivan Иванов";
        final String family = "Иванов";
        new StSender(name, "ivan@gmail.com", charset).attach(msg);
        MatcherAssert.assertThat(
            new InternetAddress(
                msg.getFrom()[0].toString()
            ).getPersonal(),
            Matchers.containsString(family)
        );
        MatcherAssert.assertThat(
            msg.getFrom()[0].toString(),
            Matchers.not(Matchers.containsString(family))
        );
        MatcherAssert.assertThat(
            msg.getFrom()[0].toString(),
            Matchers.not(Matchers.containsString("UTF"))
        );
        MatcherAssert.assertThat(
            msg.getFrom()[0].toString(),
            Matchers.containsString(charset)
        );
        MatcherAssert.assertThat(
            msg.getFrom()[0].toString(),
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
