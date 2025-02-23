/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.enclosure;

import java.io.ByteArrayOutputStream;
import javax.mail.internet.MimeBodyPart;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link EnPlain}.
 *
 * @since 1.3.2
 */
final class EnPlainTest {

    /**
     * EnPlain can create a plain MIME part.
     * @throws Exception If fails
     */
    @Test
    void createsPlainMimePart() throws Exception {
        final MimeBodyPart part = new EnPlain("hello, друг").part();
        MatcherAssert.assertThat(
            part.getContent().toString(),
            Matchers.endsWith("друг")
        );
    }

    /**
     * EnPlain can create a plain MIME part with custom encoding.
     * @throws Exception If fails
     */
    @Test
    void createsPlainMimePartWithCustomEncoding() throws Exception {
        final String charset = "KOI8-R";
        final MimeBodyPart part = new EnPlain(
            "hello, приятель",
            charset
        ).part();
        final String suffix = "приятель";
        MatcherAssert.assertThat(
            part.getContent().toString(),
            Matchers.endsWith(suffix)
        );
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        part.writeTo(bytes);
        MatcherAssert.assertThat(
            new String(bytes.toByteArray(), charset),
            Matchers.endsWith(suffix)
        );
        MatcherAssert.assertThat(
            new String(bytes.toByteArray(), "UTF-8"),
            Matchers.not(Matchers.endsWith(suffix))
        );
    }
}
