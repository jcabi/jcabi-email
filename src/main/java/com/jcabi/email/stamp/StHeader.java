/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.stamp;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.email.Stamp;
import javax.mail.Message;
import javax.mail.MessagingException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Stamp for a MIME envelope, with a header name and value.
 *
 * @since 1.6.2
 */
@Immutable
@ToString
@EqualsAndHashCode(of = {"name", "value" })
@Loggable(Loggable.DEBUG)
public final class StHeader implements Stamp {

    /**
     * Header name.
     */
    private final transient String name;

    /**
     * Header value.
     */
    private final transient String value;

    /**
     * Ctor.
     * @param nme Name of header
     * @param val Value to add under header's name
     * @since 1.1
     */
    public StHeader(final String nme, final String val) {
        this.name = nme;
        this.value = val;
    }

    @Override
    public void attach(final Message message) throws MessagingException {
        if (this.name == null) {
            throw new IllegalArgumentException(
                "Header name can't be NULL"
            );
        }
        if (this.value == null) {
            throw new IllegalArgumentException(
                "Header value can't be NULL"
            );
        }
        message.addHeader(
            this.name,
            this.value
        );
    }
}
