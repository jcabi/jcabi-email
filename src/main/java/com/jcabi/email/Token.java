/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * User signature to network node.
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
 *
 * @since 1.0
 */
@Immutable
@ToString
@EqualsAndHashCode(of = {"user", "password"})
@Loggable(Loggable.DEBUG)
public final class Token {

    /**
     * User name with access.
     */
    private final transient String user;

    /**
     * User's password.
     */
    private final transient String password;

    /**
     * Public ctor.
     * @param usr User name with access
     * @param pwd User's password
     */
    public Token(final String usr, final String pwd) {
        this.user = usr;
        this.password = pwd;
    }

    /**
     * Access for given protocol.
     * @param protocol Protocol.
     * @return Session.
     */
    public Session access(final Protocol protocol) {
        final Properties props = new Properties();
        for (final Map.Entry<String, String> entry
            : protocol.entries().entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }
        return Session.getInstance(
            props, new Token.Verification(this.user, this.password)
        );
    }

    /**
     * Authenticating credentials.
     * @since 1.0
     */
    private static final class Verification extends Authenticator {
        /**
         * User name.
         */
        private final transient String user;

        /**
         * User password.
         */
        private final transient String password;

        /**
         * Public ctor.
         * @param usr User name
         * @param pwd User password
         */
        Verification(final String usr, final String pwd) {
            super();
            this.user = usr;
            this.password = pwd;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.user, this.password);
        }
    }
}
