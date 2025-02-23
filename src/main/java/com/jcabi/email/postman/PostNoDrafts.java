/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.postman;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.email.Envelope;
import com.jcabi.email.Postman;
import com.jcabi.log.Logger;
import java.io.IOException;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Postman that ignores drafts.
 *
 * @since 1.6
 */
@Immutable
@ToString
@EqualsAndHashCode(of = "origin")
@Loggable(Loggable.DEBUG)
public final class PostNoDrafts implements Postman {

    /**
     * Original postman.
     */
    private final transient Postman origin;

    /**
     * Ctor.
     * @param post Original postman
     */
    public PostNoDrafts(final Postman post) {
        this.origin = post;
    }

    @Override
    public void send(final Envelope env) throws IOException {
        final Message message = env.unwrap();
        try {
            if (message.isSet(Flags.Flag.DRAFT)) {
                Logger.info(this, "message has DRAFT flag, ignoring");
            } else {
                this.origin.send(env);
            }
        } catch (final MessagingException ex) {
            throw new IOException(ex);
        }
    }

}
