/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email;

import com.google.common.collect.ImmutableMap;
import com.jcabi.aspects.Immutable;
import java.util.Map;
import javax.net.ssl.SSLSocketFactory;

/**
 * Mail network protocol.
 *
 * <p>This is how you're supposed to use it:
 *
 * <pre> Postman postman = new Postman.Default(
 *   new SMTP(
 *     new Token("user", "password").access(
 *       new Protocol.SMTP("bind", "port")
 *     )
 *   )
 * );
 * </pre>
 * For <b>SMTPS</b> use the {@link com.jcabi.email.wire.Smtps} wire and
 * {@link Smtps} respectively. SMTPS protocol should work only with
 * the SMTPS wire because the wire uses the "smtps" transport to make the
 * connection to the mail server:
 * <pre>
 *     final Transport transport = this.session.getTransport("smtps");
 *     transport.connect();
 * </pre>
 *
 * @since 1.0
 */
@Immutable
public interface Protocol {

    /**
     * Guarantee of access for protocol.
     * @return Entry parameters.
     */
    Map<String, String> entries();

    /**
     * SNMP protocol.
     * @since 1.0
     */
    final class Smtp implements Protocol {
        /**
         * SMTP host.
         */
        private final transient String host;

        /**
         * SMTP port.
         */
        private final transient int port;

        /**
         * Public ctor.
         * @param hst SMTP Host
         * @param prt SMTP Port
         */
        public Smtp(final String hst, final int prt) {
            this.host = hst;
            this.port = prt;
        }

        @Override
        public Map<String, String> entries() {
            return new ImmutableMap.Builder<String, String>()
                .put("mail.smtp.auth", Boolean.TRUE.toString())
                .put("mail.smtp.host", this.host)
                .put("mail.smtp.port", Integer.toString(this.port))
                .build();
        }
    }

    /**
     * SMTPS protocol.
     * @since 1.0
     */
    final class Smtps implements Protocol {
        /**
         * SMTPS host.
         */
        private final transient String host;

        /**
         * SMTPS port.
         */
        private final transient int port;

        /**
         * Public ctor.
         * @param hst SMTPS Host
         * @param prt SMTPS Port
         */
        public Smtps(final String hst, final int prt) {
            this.host = hst;
            this.port = prt;
        }

        @Override
        public Map<String, String> entries() {
            return new ImmutableMap.Builder<String, String>()
                .put("mail.smtps.auth", Boolean.TRUE.toString())
                .put("mail.smtps.host", this.host)
                .put("mail.smtps.port", Integer.toString(this.port))
                .put("mail.smtp.starttls.required", "true")
                .put("mail.smtp.ssl.protocols", "TLSv1.2")
                .put("mail.smtp.starttls.enable", "true")
                .put(
                    "mail.smtp.ssl.checkserveridentity",
                    Boolean.TRUE.toString()
                ).put(
                    "mail.smtp.socketFactory.class",
                    SSLSocketFactory.class.getName()
                ).put(
                    "mail.smtp.socketFactory.port",
                    Integer.toString(this.port)
                ).put("mail.smtp.socketFactory.fallback", "false").build();
        }
    }
}
