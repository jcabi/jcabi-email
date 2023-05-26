/*
 * Copyright (c) 2014-2023, jcabi.com
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
