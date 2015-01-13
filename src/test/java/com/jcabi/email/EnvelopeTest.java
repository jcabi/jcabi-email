/**
 * Copyright (c) 2014, jcabi.com
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
package com.jcabi.email;

import com.google.common.base.Joiner;
import com.jcabi.email.enclosure.EnHTML;
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
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link Envelope}.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 1.4
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class EnvelopeTest {

    /**
     * Envelope.Constant can cache.
     * @throws Exception If fails
     */
    @Test
    public void cachesPreviousCall() throws Exception {
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
     * Verifies that the encoding of the mail is set correctly and - as a
     *  consequence - non-Latin (e. g. Cyrillic) characters are displayed
     *  correctly in the received e-mail.
     * @throws Exception Thrown in case of problem of writing a message to
     *  string.
     */
    @Test
    public void handlesUnicodeCorrectly() throws Exception {
        final Envelope env = new Envelope.MIME()
            .with(new StSender("from <test-from@jcabi.com>"))
            .with(new StRecipient("to", "test-to@jcabi.com"))
            .with(new StSubject("test subject: test me"))
            .with(new EnHTML("<html><body>привет</body></html>"));
        final ByteArrayOutputStream stream =
            new ByteArrayOutputStream();
        env.unwrap().writeTo(stream);
        final String msgtxt = stream.toString();
        Assert.assertTrue(
            msgtxt.contains(
                "Content-Type: text/html;charset=\"utf-8\""
            )
        );
        Assert.assertTrue(
            msgtxt.contains(
                "Content-Transfer-Encoding: quoted-printable"
            )
        );
        Assert.assertTrue(
            msgtxt.contains(
                Joiner.on("").join(
                    "<html><body>",
                    "=D0=BF=D1=80=D0=B8=D0=B2=D0=B5=D1=82</body></html>"
                )
            )
        );
    }

    /**
     * Envelope.MIME can wrap another envelope.
     * @throws Exception If fails
     */
    @Test
    public void wrapsAnotherEnvelope() throws Exception {
        final Envelope origin = new Envelope.MIME().with(
            new StSender("jack@example.com")
        );
        final Message message = new Envelope.MIME(origin).with(
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
    public void wrapsAnotherEnvelopeWithEnclosures() throws Exception {
        final Envelope origin = new Envelope.MIME().with(
            new EnPlain("first enclosure")
        );
        final Message message = new Envelope.MIME(origin).with(
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
