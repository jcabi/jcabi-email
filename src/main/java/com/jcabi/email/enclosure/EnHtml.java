/*
 * Copyright (c) 2014-2023, jcabi.com
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
