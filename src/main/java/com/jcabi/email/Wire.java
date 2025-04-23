/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email;

import com.jcabi.aspects.Immutable;
import com.jcabi.email.wire.Smtp;
import java.io.IOException;
import javax.mail.Transport;

/**
 * Wire used by a {@link Postman}.
 *
 * <p>This is how you're supposed to use it:
 *
 * <pre> Postman postman = new Postman.Default(
 *   new SMTP(
 *     new Token("user", "password").access(
 *       new Protocol.SMTPS("smtp.gmail.com", 587)
 *     )
 *   )
 * );
 * </pre>
 *
 * @see Smtp
 * @since 1.0
 */
@Immutable
public interface Wire {

    /**
     * Make a transport.
     * @return Transport
     * @throws IOException If fails
     */
    Transport connect() throws IOException;

}
