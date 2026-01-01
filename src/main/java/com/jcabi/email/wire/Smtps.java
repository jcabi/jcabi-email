/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.wire;

import com.jcabi.email.Wire;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

/**
 * SMTPS wire.
 *
 * <p>This is how you're supposed to use it:
 *
 * <pre> Postman postman = new Postman.Default(
 *   new SMTPS(
 *     new Token("user", "password").access(
 *       new Protocol.SMTPS("smtp.gmail.com", 587)
 *     )
 *   )
 * );
 * </pre>
 *
 * @since 1.9
 */
public final class Smtps implements Wire {

    /**
     * Mail session.
     */
    private final transient Session session;

    /**
     * Public ctor.
     * @param session Session.
     */
    public Smtps(final Session session) {
        this.session = session;
    }

    @Override
    public Transport connect() throws IOException {
        try {
            final Transport transport = this.session.getTransport("smtps");
            transport.connect();
            return transport;
        } catch (final MessagingException ex) {
            throw new IOException(ex);
        }
    }
}
