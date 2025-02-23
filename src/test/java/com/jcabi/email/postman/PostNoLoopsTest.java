/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.postman;

import com.jcabi.email.Envelope;
import com.jcabi.email.Postman;
import com.jcabi.email.stamp.StRecipient;
import com.jcabi.email.stamp.StSender;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link PostNoLoops}.
 *
 * @since 1.6
 */
final class PostNoLoopsTest {

    /**
     * PostNoLoops can ignore messages.
     * @throws Exception If fails
     */
    @Test
    @SuppressWarnings("unchecked")
    void ignoresLoopMessages() throws Exception {
        final Postman post = Mockito.mock(Postman.class);
        final String email = "test@example.com";
        new PostNoLoops(post).send(
            new Envelope.Mime()
                .with(new StRecipient(email))
                .with(new StRecipient("hello@example.com"))
                .with(new StSender("Jeff", email))
        );
        Mockito.verify(post, Mockito.never()).send(Mockito.any(Envelope.class));
    }

    /**
     * PostNoLoops can pass normal messages through.
     * @throws Exception If fails
     */
    @Test
    @SuppressWarnings("unchecked")
    void doesntTouchNormalMessages() throws Exception {
        final Postman post = Mockito.mock(Postman.class);
        new PostNoLoops(post).send(
            new Envelope.Mime()
                .with(new StRecipient("jeff@example.com"))
                .with(new StSender("Walter", "walter@example.com"))
        );
        Mockito.verify(post).send(Mockito.any(Envelope.class));
    }

}
