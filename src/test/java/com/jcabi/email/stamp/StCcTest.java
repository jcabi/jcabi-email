/*
 * Copyright (c) 2014-2023, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
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
