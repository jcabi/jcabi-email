/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.wire;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.email.Wire;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

/**
 * SMTP wire.
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
@Loggable(Loggable.DEBUG)
public final class Smtp implements Wire {

    /**
     * Mail session.
     */
    private final transient Session session;

    /**
     * Public ctor.
     * @param session Session.
     */
    public Smtp(final Session session) {
        this.session = session;
    }

    @Override
    public Transport connect() throws IOException {
        try {
            final Transport transport = this.session.getTransport("smtp");
            transport.connect();
            return transport;
        } catch (final MessagingException ex) {
            throw new IOException(ex);
        }
    }

}
