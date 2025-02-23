/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.enclosure;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.email.Enclosure;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Plain enclosure in MIME envelope.
 *
 * @since 1.0
 */
@Immutable
@ToString
@EqualsAndHashCode(of = {"text", "charset"})
@Loggable(Loggable.DEBUG)
public final class EnPlain implements Enclosure {

    /**
     * Text content.
     */
    private final transient String text;

    /**
     * Text charset.
     */
    private final transient String charset;

    /**
     * Ctor.
     * @param content Plain content
     */
    public EnPlain(final String content) {
        this(content, "UTF-8");
    }

    /**
     * Ctor.
     * @param content Plain content
     * @param charset Content charset
     */
    public EnPlain(final String content, final String charset) {
        this.text = content;
        this.charset = charset;
    }

    @Override
    public MimeBodyPart part() throws MessagingException {
        if (this.text == null) {
            throw new IllegalArgumentException(
                "Attachment text can't be NULL"
            );
        }
        if (this.charset == null) {
            throw new IllegalArgumentException(
                "Attachment charset can't be NULL"
            );
        }
        final MimeBodyPart mime = new MimeBodyPart();
        mime.setText(this.text, this.charset);
        mime.addHeader("Content-Type", "text/plain");
        return mime;
    }

}
