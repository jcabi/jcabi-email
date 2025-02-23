/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.stamp;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.email.Stamp;
import java.io.UnsupportedEncodingException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Stamp for a MIME envelope, with a subject.
 *
 * @since 1.0
 */
@Immutable
@ToString
@EqualsAndHashCode(of = {"subject", "charset"})
@Loggable(Loggable.DEBUG)
public final class StSubject implements Stamp {

    /**
     * Subject.
     */
    private final transient String subject;

    /**
     * Subject charset.
     */
    private final transient String charset;

    /**
     * Ctor.
     * @param subj Subject
     */
    public StSubject(final String subj) {
        this(subj, "UTF-8");
    }

    /**
     * Ctor.
     * @param subj Subject
     * @param charset Subject charset
     */
    public StSubject(final String subj, final String charset) {
        this.subject = subj;
        this.charset = charset;
    }

    @Override
    public void attach(final Message message) throws MessagingException {
        if (this.subject == null) {
            throw new IllegalArgumentException(
                "Email subject can't be NULL"
            );
        }
        if (this.charset == null) {
            throw new IllegalArgumentException(
                "Subject charset can't be NULL"
            );
        }
        try {
            message.setSubject(
                MimeUtility.encodeText(this.subject, this.charset, "Q")
            );
        } catch (final UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
