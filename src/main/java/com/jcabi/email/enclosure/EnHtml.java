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
import javax.mail.internet.MimeUtility;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * HTML enclosure in MIME envelope.
 *
 * @since 1.0
 */
@Immutable
@ToString
@EqualsAndHashCode(of = {"text", "charset"})
@Loggable(Loggable.DEBUG)
public final class EnHtml implements Enclosure {

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
     * @param content HTML content
     */
    public EnHtml(final String content) {
        this(content, "UTF-8");
    }

    /**
     * Ctor.
     * @param content HTML content
     * @param charset Content charset
     */
    public EnHtml(final String content, final String charset) {
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
        final String characterset = MimeUtility.quote(
            this.charset,
            "()<>@,;:\\\"\t []/?="
        );
        final String ctype = String.format(
            "text/html;charset=\"%s\"",
            characterset
        );
        mime.setContent(this.text, ctype);
        mime.addHeader("Content-Type", ctype);
        return mime;
    }

}
