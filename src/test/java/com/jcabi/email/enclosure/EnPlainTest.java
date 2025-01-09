/*
 * Copyright (c) 2014-2025, jcabi.com
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
