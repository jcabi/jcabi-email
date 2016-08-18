/**
 * Copyright (c) 2014-2015, jcabi.com
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
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 1.0
 */
@Immutable
@Loggable(Loggable.DEBUG)
public final class SMTP implements Wire {

    /**
     * Mail session.
     */
    private final transient Session session;

    /**
     * Public ctor.
     * @param sess Session.
     */
    public SMTP(final Session sess) {
        this.session = sess;
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
