/*
 * This file is part of Tprot.
 *
 * Tprot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Tprot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Tprot.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jcabi.email;

import com.jcabi.email.enclosure.EnHTML;
import com.jcabi.email.stamp.StRecipient;
import com.jcabi.email.stamp.StSender;
import com.jcabi.email.stamp.StSubject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

public class PostmanTest {
    /**
     * Test for issue #8.
     */
    @Test
    public void utf8Handling() throws MessagingException, IOException {
        final Transport transport = Mockito.mock(Transport.class);
        final Wire wire = Mockito.mock(Wire.class);
        Mockito.when(wire.connect()).thenReturn(transport);
        final Postman.Default postman = new Postman.Default(wire);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invoc) throws Throwable {
                Assert.assertTrue(invoc.getArguments()[0] instanceof MimeMessage);
                final MimeMessage msg = (MimeMessage) invoc.getArguments()[0];
                Assert.assertTrue(msg.getContent() instanceof Multipart);
                final Multipart multipart = (Multipart) msg.getContent();
                Assert.assertEquals(1, multipart.getCount());
                Assert.assertTrue(multipart.getBodyPart(0) instanceof MimeBodyPart);
                final MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(0);
                Assert.assertEquals("UTF-8", bodyPart.getEncoding());
                Assert.assertEquals("<html><body>привет</body></html>", bodyPart.getContent());
                return null;
            }
        }).when(transport).sendMessage(Mockito.isA(Message.class),
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
