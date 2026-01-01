/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.log.Logger;
import java.io.IOException;
import java.util.Arrays;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Postman.
 *
 * <p>The best way to use it is to make an instance of
 * {@link com.jcabi.email.Postman.Default}:
 *
 * <pre> Postman postman = new Postman.Default(
 *   new SMTP(
 *     new Token("user", "password").access(
 *       new Protocol.SMTPS("smtp.gmail.com", 587)
 *     )
 *   )
 * );
 * postman.send(
 *   new Envelope.MIME(
 *     new Array&lt;Stamp&gt;(
 *       new StSender("Yegor Bugayenko &lt;yegor@jcabi.com&gt;"),
 *       new StRecipient("Jeff Lebowski &lt;jeff@gmail.com&gt;"),
 *       new StSubject("dude, how are you?"),
 *       new StBCC("my-boss@jcabi.com")
 *     ),
 *     new Array&lt;Enclosure&gt;(
 *       new EnPlain("Hi, long time no see! :) Check my pic!"),
 *       new EnBinary(
 *         new File("/tmp/picture.gif"),
 *         "my-picture.gif",
 *         "image/gif"
 *       )
 *     )
 *   )
 * );</pre>
 *
 * @since 1.0
 */
@Immutable
public interface Postman {

    /**
     * Doesn't send anything, just logs to console.
     * @since 1.1
     */
    Postman CONSOLE = new Postman() {
        @Override
        public void send(final Envelope env) throws IOException {
            final Message msg = new Envelope.Strict(env).unwrap();
            try {
                Logger.info(
                    this, "fake email \"%s\" from %s to %s",
                    msg.getSubject(),
                    Arrays.asList(msg.getFrom()),
                    Arrays.asList(msg.getAllRecipients())
                );
            } catch (final MessagingException ex) {
                throw new IOException(ex);
            }
        }
    };

    /**
     * Send this envelope.
     * @param env Envelope to send
     * @throws IOException If fails
     */
    void send(Envelope env) throws IOException;

    /**
     * Default postman.
     * @since 1.0
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = "wire")
    @Loggable(Loggable.DEBUG)
    final class Default implements Postman {
        /**
         * Wire.
         */
        private final transient Wire wire;

        /**
         * Ctor.
         * @param wre Wire
         */
        public Default(final Wire wre) {
            this.wire = wre;
        }

        @Override
        public void send(final Envelope env) throws IOException {
            final Message message = new Envelope.Strict(env).unwrap();
            final Transport transport = this.wire.connect();
            try {
                final Address[] rcpts = message.getAllRecipients();
                transport.sendMessage(message, rcpts);
                Logger.info(
                    this, "email sent from %s to %[list]s",
                    Arrays.asList(message.getFrom()),
                    Arrays.asList(rcpts)
                );
            } catch (final MessagingException ex) {
                throw new IOException(ex);
            } finally {
                Postman.Default.close(transport);
            }
        }

        /**
         * Close transport.
         * @param transport Transport to close
         * @throws IOException If fails
         */
        private static void close(final Transport transport)
            throws IOException {
            try {
                transport.close();
            } catch (final MessagingException ex) {
                throw new IOException(ex);
            }
        }
    }

}
