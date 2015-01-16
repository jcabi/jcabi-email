/**
 * Copyright (c) 2014-2015, jcabi.com
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
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Binary enclosure in MIME envelope.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 1.0
 */
@Immutable
@ToString
@EqualsAndHashCode(of = { "path", "name", "ctype" })
@Loggable(Loggable.DEBUG)
public final class EnBinary implements Enclosure {

    /**
     * File path.
     */
    private final transient String path;

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
        this.path = file.getAbsolutePath();
        this.name = label;
        this.ctype = type;
    }

    @Override
    public MimeBodyPart part() throws MessagingException {
        final MimeBodyPart mime = new MimeBodyPart();
        try {
            mime.attachFile(new File(this.path));
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        mime.setFileName(this.name);
        mime.addHeader("Content-Type", this.ctype);
        return mime;
    }
}
