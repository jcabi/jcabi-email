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
import com.jcabi.email.stamp.StRecipient;
import com.jcabi.email.stamp.StSender;
import com.jcabi.email.stamp.StSubject;
import java.io.ByteArrayOutputStream;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test class for Postman class.
 */
public final class PostmanTest {
    /**
     * Test for issue #8 (unicode/UTF-8 is broken).
     * @throws Exception Thrown in case of problem of writing a message to
     *  string.
     */
    @Test
    public final void utf8Handling() throws Exception {
        final Transport transport = Mockito.mock(Transport.class);
        final Wire wire = Mockito.mock(Wire.class);
        Mockito.when(wire.connect()).thenReturn(transport);
        final Postman.Default postman = new Postman.Default(wire);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invoc) throws Throwable {
                final MimeMessage msg = (MimeMessage) invoc.getArguments()[0];
                final ByteArrayOutputStream stream =
                    new ByteArrayOutputStream();
                msg.writeTo(stream);
                final String msgtxt = stream.toString();
                Assert.assertTrue(msgtxt.contains(
                    "Content-Type: text/html;charset=\"utf-8\""));
                Assert.assertTrue(msgtxt.contains(
                    "Content-Transfer-Encoding: quoted-printable"));
                Assert.assertTrue(msgtxt.contains(
                    Joiner.on("").join("<html><body>",
                        "=D0=BF=D1=80=D0=B8=D0=B2=D0=B5=D1=82</body></html>")));
                return null;
            }
        }).when(transport).sendMessage(
            Mockito.isA(Message.class),
            Mockito.any(Address[].class)
        );
        final Envelope.MIME env = new Envelope.MIME()
                .with(new StSender("from <test-from@jcabi.com>"))
                .with(new StRecipient("to", "test-to@jcabi.com"))
                .with(new StSubject("test subject: test me"))
                .with(new EnHTML("<html><body>привет</body></html>"));
        postman.send(env);
    }
}
