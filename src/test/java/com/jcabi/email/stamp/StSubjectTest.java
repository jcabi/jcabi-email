/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.stamp;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link com.jcabi.email.stamp.StSubject}.
 *
 * @since 1.3.1
 */
final class StSubjectTest {

    /**
     * StSubject can add a subject to the message.
     * @throws Exception If fails
     */
    @Test
    void addsSubjectToMessage() throws Exception {
        final Message msg = new MimeMessage(
            Session.getDefaultInstance(new Properties())
        );
        new StSubject("how are you, друг?").attach(msg);
        MatcherAssert.assertThat(
            msg.getSubject(),
            Matchers.containsString("друг")
        );
    }

    /**
     * StSubject can add a subject to the message with custom encoding.
     * @throws Exception If fails
     */
    @Test
    void addsSubjectToMessageWithCustomEncoding() throws Exception {
        final String charset = "KOI8-R";
        final Message msg = new MimeMessage(
            Session.getDefaultInstance(new Properties())
        );
        new StSubject("how are you, приятель?", charset).attach(msg);
        final String substring = "приятель";
        MatcherAssert.assertThat(
            msg.getSubject(),
            Matchers.containsString(substring)
        );
        final String subject = "Subject";
        MatcherAssert.assertThat(
            msg.getHeader(subject)[0],
            Matchers.not(Matchers.containsString("UTF"))
        );
        MatcherAssert.assertThat(
            msg.getHeader(subject)[0],
            Matchers.containsString(charset)
        );
        MatcherAssert.assertThat(
            msg.getHeader(subject)[0],
            Matchers.containsString(encodeText(substring, charset))
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
        final String encoded = MimeUtility.encodeText(text, charset, "Q");
        final int last = encoded.lastIndexOf('?');
        final int prev = encoded.lastIndexOf('?', last - 1);
        return encoded.substring(prev + 1, last);
    }

}
