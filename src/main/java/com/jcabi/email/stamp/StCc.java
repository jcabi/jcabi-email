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
package com.jcabi.email.stamp;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.email.Stamp;
import java.io.UnsupportedEncodingException;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Stamp for a MIME envelope, with a CC recipient.
 *
 * @since 1.0
 */
@Immutable
@ToString
@EqualsAndHashCode(of = "email")
@Loggable(Loggable.DEBUG)
public final class StCc implements Stamp {

    /**
     * Email to send to.
     */
    private final transient String email;

    /**
     * Ctor.
     * @param addr Address
     */
    public StCc(final Address addr) {
        this(addr.toString());
    }

    /**
     * Ctor.
     * @param name Name of the recipient
     * @param addr His email
     * @since 1.1
     */
    public StCc(final String name, final String addr) {
        this(name, addr, "UTF-8");
    }

    /**
     * Ctor.
     * @param name Name of the recipient
     * @param addr His email
     * @param charset Name charset
     */
    public StCc(final String name, final String addr, final String charset) {
        this(StCc.addr(name, addr, charset));
    }

    /**
     * Ctor.
     * @param addr Address
     */
    public StCc(final String addr) {
        this.email = addr;
    }

    @Override
    public void attach(final Message message) throws MessagingException {
        if (this.email == null) {
            throw new IllegalArgumentException(
                "Email address can't be NULL"
            );
        }
        message.addRecipient(
            Message.RecipientType.CC,
            new InternetAddress(this.email)
        );
    }

    /**
     * Make email.
     * @param name Name of the recipient
     * @param addr His email
     * @param charset Name charset
     * @return Email
     * @since 1.1
     */
    private static String addr(
        final String name,
        final String addr,
        final String charset
    ) {
        try {
            return new InternetAddress(addr, name, charset).toString();
        } catch (final UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
