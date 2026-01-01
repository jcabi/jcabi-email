/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
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
