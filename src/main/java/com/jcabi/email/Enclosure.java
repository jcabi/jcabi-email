/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email;

import com.jcabi.aspects.Immutable;
import com.jcabi.email.enclosure.EnHtml;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 * Enclosure in MIME envelope.
 *
 * @since 1.0
 * @see com.jcabi.email.enclosure.EnPlain
 * @see com.jcabi.email.enclosure.EnBinary
 * @see EnHtml
 */
@Immutable
public interface Enclosure {

    /**
     * Create a MIME body part.
     * @return MIME body part
     * @throws MessagingException If fails
     */
    MimeBodyPart part() throws MessagingException;

}
