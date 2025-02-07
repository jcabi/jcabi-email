/*
 * Copyright (c) 2014-2025 Yegor Bugayenko
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
