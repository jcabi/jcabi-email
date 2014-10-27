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
import com.jcabi.immutable.Array;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Envelope.
 *
 * <p>The best way is to use {@link com.jcabi.email.Envelope.MIME}, but
 * you can easily create your own implementation of it.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 1.0
 */
@Immutable
public interface Envelope {

    /**
     * Get a message out of it.
     * @return Message to send
     * @throws IOException If fails
     */
    Message unwrap() throws IOException;

    /**
     * Default envelope.
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = "encs")
    @Loggable(Loggable.DEBUG)
    final class MIME implements Envelope {
        /**
         * List of stamps.
         */
        private final transient Array<Stamp> stamps;
        /**
         * List of enclosures.
         */
        private final transient Array<Enclosure> encs;
        /**
         * Ctor.
         * @since 1.3
         */
        public MIME() {
            this(new Array<Stamp>(), new Array<Enclosure>());
        }
        /**
         * Ctor.
         * @param stmps Stamps
         * @param list List of enclosures
         */
        public MIME(final Iterable<Stamp> stmps,
            final Iterable<Enclosure> list) {
            this.stamps = new Array<Stamp>(stmps);
            this.encs = new Array<Enclosure>(list);
        }
        @Override
        public Message unwrap() throws IOException {
            final Message msg = new MimeMessage(
                Session.getDefaultInstance(new Properties())
            );
            final Multipart multi = new MimeMultipart("alternative");
            try {
                for (final Enclosure enc : this.encs) {
                    multi.addBodyPart(enc.part());
                }
                for (final Stamp stamp : this.stamps) {
                    stamp.attach(msg);
                }
                msg.setContent(multi);
            } catch (final MessagingException ex) {
                throw new IOException(ex);
            }
            return msg;
        }
        /**
         * With this stamp.
         * @param stamp Stamp
         * @return MIME envelope
         * @since 1.3
         */
        public Envelope.MIME with(final Stamp stamp) {
            return new Envelope.MIME(
                this.stamps.with(stamp),
                this.encs
            );
        }
        /**
         * With this enclosure.
         * @param enc Enclosure
         * @return MIME envelope
         * @since 1.3
         */
        public Envelope.MIME with(final Enclosure enc) {
            return new Envelope.MIME(
                this.stamps,
                this.encs.with(enc)
            );
        }
    }

    /**
     * Strict envelope that fails if message is not valid.
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = "origin")
    @Loggable(Loggable.DEBUG)
    final class Strict implements Envelope {
        /**
         * Origin env.
         */
        private final transient Envelope origin;
        /**
         * Ctor.
         * @param env Envelope
         */
        public Strict(final Envelope env) {
            this.origin = env;
        }
        @Override
        public Message unwrap() throws IOException {
            final Message msg = this.origin.unwrap();
            try {
                if (msg.getAllRecipients() == null) {
                    throw new IllegalStateException(
                        "list of recipients is NULL"
                    );
                }
                if (msg.getFrom() == null) {
                    throw new IllegalStateException(
                        "list of senders is NULL"
                    );
                }
                if (msg.getSubject() == null) {
                    throw new IllegalStateException(
                        "subject is NULL"
                    );
                }
            } catch (final MessagingException ex) {
                throw new IOException(ex);
            }
            return msg;
        }
    }

}
