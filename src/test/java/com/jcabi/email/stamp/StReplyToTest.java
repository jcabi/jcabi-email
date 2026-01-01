/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.stamp;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link StReplyTo}.
 *
 * @since 1.8
 */
final class StReplyToTest {

    /**
     * StReplyTo can attach the reply to address to a message.
     * @throws Exception If something goes wrong.
     */
    @Test
    void addsReplyToAddressToMessage() throws Exception {
        final Message msg = new MimeMessage(
            Session.getDefaultInstance(new Properties())
        );
        final String address = "john.doe@testmail.com";
        new StReplyTo(address).attach(msg);
        MatcherAssert.assertThat(
            new InternetAddress(
                msg.getReplyTo()[0].toString()
            ).getAddress(),
            Matchers.equalTo(address)
        );
    }
}
