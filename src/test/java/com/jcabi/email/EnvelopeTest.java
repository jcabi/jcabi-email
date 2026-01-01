/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email;

import com.google.common.base.Joiner;
import com.jcabi.email.enclosure.EnHtml;
import com.jcabi.email.enclosure.EnPlain;
import com.jcabi.email.stamp.StRecipient;
import com.jcabi.email.stamp.StSender;
import com.jcabi.email.stamp.StSubject;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link Envelope}.
 *
 * @since 1.4
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class EnvelopeTest {

    /**
     * Envelope.Constant can cache.
     * @throws Exception If fails
     */
    @Test
    void cachesPreviousCall() throws Exception {
        final Envelope origin = Mockito.mock(Envelope.class);
        Mockito.doReturn(
            new MimeMessage(Session.getDefaultInstance(new Properties()))
        ).when(origin).unwrap();
        final Envelope envelope = new Envelope.Constant(origin);
        envelope.unwrap();
        envelope.unwrap();
        Mockito.verify(origin, Mockito.times(1)).unwrap();
    }

    /**
     * Envelope should handle non-Latin characters.
     * @throws Exception Thrown in case of problem of writing a message to
     *  string.
     */
    @Test
    void handlesUnicodeCorrectly() throws Exception {
        final Envelope env = new Envelope.Mime()
            .with(new StSender("from <test-from@jcabi.com>"))
            .with(new StRecipient("to", "test-to@jcabi.com"))
            .with(new StSubject("test subject: test me"))
            .with(new EnHtml("<html><body>привет</body></html>"));
        final ByteArrayOutputStream stream =
            new ByteArrayOutputStream();
        env.unwrap().writeTo(stream);
        final String msgtxt = stream.toString();
        MatcherAssert.assertThat(
            msgtxt,
            Matchers.allOf(
                Matchers.containsString(
                    "Content-Type: text/html;charset=\"UTF-8\""
                ),
                Matchers.containsString(
                    "Content-Transfer-Encoding: quoted-printable"
                ),
                Matchers.containsString(
                    Joiner.on("").join(
                        "<html><body>",
                        "=D0=BF=D1=80=D0=B8=D0=B2=D0=B5=D1=82</body></html>"
                    )
                )
            )
        );
    }

    /**
     * Envelope.MIME can wrap another envelope.
     * @throws Exception If fails
     */
    @Test
    void wrapsAnotherEnvelope() throws Exception {
        final Envelope origin = new Envelope.Mime().with(
            new StSender("jack@example.com")
        );
        final Message message = new Envelope.Mime(origin).with(
            new StRecipient("paul@example.com")
        ).unwrap();
        MatcherAssert.assertThat(
            message.getAllRecipients().length,
            Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            message.getFrom().length,
            Matchers.equalTo(1)
        );
    }

    /**
     * Envelope.MIME can wrap another envelope with enclosures.
     * @throws Exception If fails
     */
    @Test
    void wrapsAnotherEnvelopeWithEnclosures() throws Exception {
        final Envelope origin = new Envelope.Mime().with(
            new EnPlain("first enclosure")
        );
        final Message message = new Envelope.Mime(origin).with(
            new EnPlain("second enclosure")
        ).unwrap();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Multipart.class.cast(message.getContent()).writeTo(baos);
        MatcherAssert.assertThat(
            baos.toString(),
            Matchers.allOf(
                Matchers.containsString("first"),
                Matchers.containsString("second")
            )
        );
    }
}
