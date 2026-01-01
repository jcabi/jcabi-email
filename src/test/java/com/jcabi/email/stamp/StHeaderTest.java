/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.stamp;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link StHeader}.
 *
 * @since 1.6.2
 */
final class StHeaderTest {

    /**
     * StHeader can add a header to the message.
     * @throws Exception If fails
     */
    @Test
    void addsHeaderToMessage() throws Exception {
        final Message msg = new MimeMessage(
            Session.getDefaultInstance(new Properties())
        );
        final String name = "Content-Type";
        final String value = "text/plain";
        new StHeader(name, value).attach(msg);
        MatcherAssert.assertThat(
            msg.getHeader(name)[0],
            Matchers.equalTo(value)
        );
    }

}
