/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email;

import com.jcabi.aspects.Immutable;
import com.jcabi.email.stamp.StBcc;
import com.jcabi.email.stamp.StCc;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * Stamp for a MIME envelope.
 *
 * @since 1.0
 * @see com.jcabi.email.stamp.StRecipient
 * @see com.jcabi.email.stamp.StSender
 * @see StCc
 * @see StBcc
 * @see com.jcabi.email.stamp.StSubject
 */
@Immutable
public interface Stamp {

    /**
     * Attach yourself to the message.
     * @param message Message to add to
     * @throws MessagingException If fails
     */
    void attach(Message message) throws MessagingException;

}
