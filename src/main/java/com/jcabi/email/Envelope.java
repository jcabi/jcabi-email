/*
 * Copyright (c) 2014-2017, jcabi.com
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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.immutable.Array;
import com.jcabi.log.Logger;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Envelope.
 *
 * <p>The best way is to use {@link Mime}, but
 * you can easily create your own implementation of it.
 *
 * <p>It is recommended to always wrap your envelope into
 * {@link com.jcabi.email.Envelope.Safe}.
 *
 * @since 1.0
 */
@Immutable
public interface Envelope {

    /**
     * Empty (always returns an empty MIME message).
     * @since 1.5
     */
    Envelope EMPTY = new Envelope() {
        @Override
        public Message unwrap() {
            return new MimeMessage(
                Session.getDefaultInstance(new Properties())
            );
        }
    };

    /**
     * Get a message out of it.
     * @return Message to send
     * @throws IOException If fails
     */
    Message unwrap() throws IOException;

    /**
     * Default envelope.
     * @since 1.0
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = "encs")
    @Loggable(Loggable.DEBUG)
    final class Mime implements Envelope {
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
        public Mime() {
            this(new Array<Stamp>(), new Array<Enclosure>());
        }

        /**
         * Ctor.
         * @param env Original envelope
         * @since 1.5
         */
        public Mime(final Envelope env) {
            this(
                Mime.class.cast(env).stamps,
                Mime.class.cast(env).encs
            );
        }

        /**
         * Ctor.
         * @param stmps Stamps
         * @param list List of enclosures
         */
        public Mime(final Iterable<Stamp> stmps,
            final Iterable<Enclosure> list) {
            this.stamps = new Array<>(stmps);
            this.encs = new Array<>(list);
        }

        @Override
        public Message unwrap() throws IOException {
            final Message msg = Envelope.EMPTY.unwrap();
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
        public Mime with(final Stamp stamp) {
            return new Mime(
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
        public Mime with(final Enclosure enc) {
            return new Mime(
                this.stamps,
                this.encs.with(enc)
            );
        }
    }

    /**
     * Strict envelope that fails if message is not valid.
     * @since 1.3
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

    /**
     * Safe envelope that adds missing parts to the message.
     * @since 1.3
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = "origin")
    @Loggable(Loggable.DEBUG)
    final class Safe implements Envelope {
        /**
         * Origin env.
         */
        private final transient Envelope origin;

        /**
         * Ctor.
         * @param env Envelope
         */
        public Safe(final Envelope env) {
            this.origin = env;
        }

        @Override
        public Message unwrap() throws IOException {
            final Message msg = this.origin.unwrap();
            try {
                if (msg.getAllRecipients() == null) {
                    msg.addRecipient(
                        Message.RecipientType.TO,
                        new InternetAddress("to@example.com")
                    );
                    Logger.warn(this, "recipients were NULL, fake one set");
                }
                if (msg.getFrom() == null) {
                    msg.setFrom(new InternetAddress("from@example.com"));
                    Logger.warn(this, "senders were NULL, fake one set");
                }
                if (msg.getSubject() == null) {
                    msg.setSubject(this.getClass().getCanonicalName());
                    Logger.warn(this, "subject was NULL, fake one set");
                }
            } catch (final MessagingException ex) {
                throw new IOException(ex);
            }
            return msg;
        }
    }

    /**
     * Envelope that always returns the same message (within one hour).
     * @since 1.4
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = "origin")
    @Loggable(Loggable.DEBUG)
    final class Constant implements Envelope {
        /**
         * Guava cache.
         */
        private static final Cache<Envelope, Message> CACHE =
            CacheBuilder.newBuilder()
                .expireAfterWrite(1L, TimeUnit.HOURS)
                .build();

        /**
         * Origin env.
         */
        private final transient Envelope origin;

        /**
         * Ctor.
         * @param env Envelope
         */
        public Constant(final Envelope env) {
            this.origin = env;
        }

        @Override
        public Message unwrap() throws IOException {
            try {
                return Envelope.Constant.CACHE.get(
                    this.origin,
                    new Callable<Message>() {
                        @Override
                        public Message call() throws Exception {
                            return Envelope.Constant.this.origin.unwrap();
                        }
                    }
                );
            } catch (final ExecutionException ex) {
                throw new IOException(ex);
            }
        }
    }

    /**
     * Envelope that adds DRAFT flag to the message.
     * @since 1.7
     */
    @Immutable
    @ToString
    @EqualsAndHashCode(of = "env")
    @Loggable(Loggable.DEBUG)
    final class Draft implements Envelope {
        /**
         * Origin env.
         */
        private final transient Envelope env;

        /**
         * Ctor.
         * @param origin Envelope
         */
        public Draft(final Envelope origin) {
            this.env = origin;
        }

        @Override
        public Message unwrap() throws IOException {
            final Message msg = this.env.unwrap();
            try {
                msg.setFlag(Flags.Flag.DRAFT, true);
            } catch (final MessagingException ex) {
                throw new IOException(ex);
            }
            return msg;
        }
    }

}
