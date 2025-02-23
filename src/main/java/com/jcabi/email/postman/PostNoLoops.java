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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Postman that ignores loops (sender equals to recipient).
 *
 * @since 1.6
 */
@Immutable
@ToString
@EqualsAndHashCode(of = "origin")
@Loggable(Loggable.DEBUG)
public final class PostNoLoops implements Postman {

    /**
     * Original postman.
     */
    private final transient Postman origin;

    /**
     * Ctor.
     * @param post Original postman
     */
    public PostNoLoops(final Postman post) {
        this.origin = post;
    }

    @Override
    public void send(final Envelope env) throws IOException {
        final Message msg = env.unwrap();
        try {
            final Address[] rcpts = msg.getAllRecipients();
            final Address[] reply = msg.getReplyTo();
            final Address[] from = msg.getFrom();
            final boolean intersects =
                this.intersect(rcpts, reply, "Reply-To and Recipients")
                || this.intersect(rcpts, from, "Recipients and From");
            if (!intersects) {
                this.origin.send(env);
            }
        } catch (final MessagingException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Check these two arrays for intersection.
     * @param first Array of addresses
     * @param second Second array
     * @param msg Text for log
     * @return TRUE if they intersect
     */
    private boolean intersect(final Address[] first, final Address[] second,
        final String msg) {
        final Collection<Address> intersection =
            new ArrayList<>(Arrays.asList(first));
        intersection.retainAll(Arrays.asList(second));
        final boolean intersect = !intersection.isEmpty();
        if (intersect) {
            Logger.info(
                this,
                "Address(es) %s exist in both %s",
                intersection, msg
            );
        }
        return intersect;
    }

}
