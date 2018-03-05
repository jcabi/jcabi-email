/**
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
package com.jcabi.email.enclosure;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.email.Enclosure;
import java.io.File;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Binary enclosure in MIME envelope.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 1.0
 */
@Immutable
@ToString
@EqualsAndHashCode(of = { "source", "name", "ctype" })
@Loggable(Loggable.DEBUG)
public final class EnBinary implements Enclosure {

    /**
     * Data source.
     */
    private final transient DataSource source;

    /**
     * Name.
     */
    private final transient String name;

    /**
     * Content type.
     */
    private final transient String ctype;

    /**
     * Ctor.
     * @param file File to attach
     * @param label Name of the file to show
     * @param type MIME content type
     */
    public EnBinary(final File file, final String label, final String type) {
        this(new FileDataSource(file), label, type);
    }

    /**
     * Ctor.
     * @param bytes Raw data to attach
     * @param label Name of the file to show
     * @param type MIME content type
     */
    public EnBinary(final byte[] bytes, final String label, final String type) {
        this(new ByteArrayDataSource(bytes, type), label, type);
    }

    /**
     * Ctor.
     * @param src Data source to attach
     * @param label Name of the file to show
     * @param type MIME content type
     */
    public EnBinary(final DataSource src, final String label,
        final String type) {
        this.source = src;
        this.name = label;
        this.ctype = type;
    }

    @Override
    public MimeBodyPart part() throws MessagingException {
        if (this.name == null) {
            throw new IllegalArgumentException(
                "Attachment name can't be NULL"
            );
        }
        if (this.ctype == null) {
            throw new IllegalArgumentException(
                "Attachment ctype can't be NULL"
            );
        }
        final MimeBodyPart mime = new MimeBodyPart();
        mime.setDataHandler(new DataHandler(this.source));
        mime.setFileName(this.name);
        mime.addHeader("Content-Type", this.ctype);
        return mime;
    }
}
