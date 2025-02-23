/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.wire;

import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.jcabi.email.Envelope;
import com.jcabi.email.Postman;
import com.jcabi.email.Protocol;
import com.jcabi.email.Token;
import com.jcabi.email.enclosure.EnPlain;
import com.jcabi.email.stamp.StRecipient;
import com.jcabi.email.stamp.StSender;
import com.jcabi.email.stamp.StSubject;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.Security;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Smtps}.
 * @since 1.9
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class SmtpsTest {

    /**
     * SMTPS postman can email the server through SMTPS wire.
     * @throws Exception If fails
     */
    @Test
    @Disabled
    void sendsEmailToTheServerThroughSmtps() throws Exception {
        final String bind = "localhost";
        final int received = 1;
        final int port = SmtpsTest.port();
        final int timeout = 3000;
        Security.setProperty(
            "ssl.SocketFactory.provider",
            DummySSLSocketFactory.class.getName()
        );
        final ServerSetup setup = new ServerSetup(
            port, bind, ServerSetup.PROTOCOL_SMTPS
        );
        setup.setServerStartupTimeout(timeout);
        final GreenMail server = new GreenMail(setup);
        server.start();
        try {
            new Postman.Default(
                new Smtps(
                    new Token("", "").access(
                        new Protocol.Smtps(
                            server.getSmtps().getBindTo(),
                            server.getSmtps().getPort()
                        )
                    )
                )
            ).send(
                new Envelope.Safe(
                    new Envelope.Mime()
                        .with(new StSender("from <test-from@jcabi.com>"))
                        .with(new StRecipient("to", "test-to@jcabi.com"))
                        .with(new StSubject("test subject: test me"))
                        .with(new EnPlain("hello"))
                )
            );
            final MimeMessage[] messages = server.getReceivedMessages();
            MatcherAssert.assertThat(
                messages.length, Matchers.is(received)
            );
            for (final Message msg : messages) {
                MatcherAssert.assertThat(
                    msg.getFrom()[0].toString(),
                    Matchers.containsString("<test-from@jcabi.com>")
                );
                MatcherAssert.assertThat(
                    msg.getSubject(),  Matchers.containsString("test me")
                );
            }
        } finally {
            server.stop();
        }
    }

    /**
     * Allocate free port.
     * @return Found port.
     * @throws IOException In case of error.
     */
    private static int port() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

}
