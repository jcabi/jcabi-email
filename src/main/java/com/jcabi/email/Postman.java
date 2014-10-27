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

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.log.Logger;
import java.io.IOException;
import java.util.Arrays;
import javax.mail.Address;
import javax.mail.Flags;
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
 *   new SMTP("smtp.googlemail.com", 465, "username", "password")
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
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
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

    /**
     * Postman that ignores drafts.
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = "origin")
    @Loggable(Loggable.DEBUG)
    final class NoDrafts implements Postman {
        /**
         * Original postman.
         */
        private final transient Postman origin;
        /**
         * Ctor.
         * @param post Original postman
         */
        public NoDrafts(final Postman post) {
            this.origin = post;
        }
        @Override
        public void send(final Envelope env) throws IOException {
            final Message message = env.unwrap();
            try {
                if (message.isSet(Flags.Flag.DRAFT)) {
                    Logger.info(this, "message has DRAFT flag, ignoring");
                } else {
                    this.origin.send(env);
                }
            } catch (final MessagingException ex) {
                throw new IOException(ex);
            }
        }
    }
}
