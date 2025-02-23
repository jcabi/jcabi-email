/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.stamp;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.email.Stamp;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Stamp for a MIME envelope, with a replyTo.
 *
 * @since 1.8
 */
@Immutable
@ToString
@EqualsAndHashCode(of = "email")
@Loggable(Loggable.DEBUG)
public final class StReplyTo implements Stamp {

    /**
     * Email to send reply.
     */
    private final transient String email;

    /**
     * Ctor.
     * @param addr Address
     */
    public StReplyTo(final Address addr) {
        this(addr.toString());
    }

    /**
     * Ctor.
     * @param addr Address
     */
    public StReplyTo(final String addr) {
        this.email = addr;
    }

    @Override
    public void attach(final Message message) throws MessagingException {
        if (this.email == null) {
            throw new IllegalArgumentException(
                "Email address can't be NULL"
            );
        }
        final Address[] addresses = {new InternetAddress(this.email)};
        message.setReplyTo(addresses);
    }
}
