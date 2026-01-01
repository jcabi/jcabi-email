/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.enclosure;

import java.io.ByteArrayOutputStream;
import javax.mail.internet.MimeBodyPart;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link EnHtml}.
 *
 * @since 1.8.2
 */
final class EnHtmlTest {

    /**
     * EnPlain can create a plain MIME part with custom encoding.
     * @throws Exception If fails
     */
    @Test
    void createsHtmlMimePartWithCustomEncoding() throws Exception {
        final String charset = "KOI8-R";
        final MimeBodyPart part = new EnHtml(
            "<p>hello, приятель</p>",
            charset
        ).part();
        final String substring = "приятель";
        MatcherAssert.assertThat(
            part.getContent().toString(),
            Matchers.containsString(substring)
        );
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        part.writeTo(bytes);
        MatcherAssert.assertThat(
            new String(bytes.toByteArray(), charset),
            Matchers.containsString(substring)
        );
        MatcherAssert.assertThat(
            new String(bytes.toByteArray(), "UTF-8"),
            Matchers.not(Matchers.containsString(substring))
        );
    }
}
