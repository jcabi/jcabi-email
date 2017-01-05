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
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
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
