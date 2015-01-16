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
import com.jcabi.log.Logger;
import java.io.IOException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * SMTP postman.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 1.0
 */
@Immutable
@ToString
@EqualsAndHashCode(of = { "host", "port", "user", "password" })
@Loggable(Loggable.DEBUG)
public final class SMTP implements Wire {

    /**
     * SMTP host.
     */
    private final transient String host;

    /**
     * SMTP port.
     */
    private final transient int port;

    /**
     * SMTP user name.
     */
    private final transient String user;

    /**
     * SMTP password.
     */
    private final transient String password;

    /**
     * Public ctor.
     * @param hst SMTP Host
     * @param prt SMTP Port
     * @param usr SMTP user name
     * @param pwd SMTP password
     * @checkstyle ParameterNumberCheck (6 lines)
     */
    public SMTP(final String hst, final int prt,
        final String usr, final String pwd) {
        this.host = hst;
        this.port = prt;
        this.user = usr;
        this.password = pwd;
    }

    @Override
    public Transport connect() throws IOException {
        final Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        final Session session = Session.getInstance(props);
        try {
            final Transport transport = session.getTransport("smtp");
            transport.connect(this.host, this.port, this.user, this.password);
            Logger.info(
                this, "sending email through %s:%s as %s...",
                this.host, this.port, this.user
            );
            return transport;
        } catch (final MessagingException ex) {
            throw new IOException(ex);
        }
    }
}
