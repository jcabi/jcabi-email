/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
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
 * Stamp for a MIME envelope, with a sender.
 *
 * @since 1.0
 */
@Immutable
@ToString
@EqualsAndHashCode(of = "email")
@Loggable(Loggable.DEBUG)
public final class StSender implements Stamp {

    /**
     * Email to send from.
     */
    private final transient String email;

    /**
     * Ctor.
     * @param addr Address
     */
    public StSender(final Address addr) {
        this(addr.toString());
    }

    /**
     * Ctor.
     * @param name Name of the recipient
     * @param addr His email
     * @since 1.1
     */
    public StSender(final String name, final String addr) {
        this(name, addr, "UTF-8");
    }

    /**
     * Ctor.
     * @param name Name of the recipient
     * @param addr His email
     * @param charset Name charset
     */
    public StSender(final String name, final String addr,
        final String charset) {
        this(StSender.addr(name, addr, charset));
    }

    /**
     * Ctor.
     * @param addr Address
     */
    public StSender(final String addr) {
        this.email = addr;
    }

    @Override
    public void attach(final Message message) throws MessagingException {
        if (this.email == null) {
            throw new IllegalArgumentException(
                "Email address can't be NULL"
            );
        }
        message.setFrom(new InternetAddress(this.email));
    }

    /**
     * Make email.
     * @param name Name of the recipient
     * @param addr His email
     * @param charset Name charset
     * @return Email
     * @since 1.1
     */
    private static String addr(final String name, final String addr,
        final String charset) {
        try {
            return new InternetAddress(addr, name, charset).toString();
        } catch (final UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
