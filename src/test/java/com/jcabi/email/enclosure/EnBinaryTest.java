/*
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
package com.jcabi.email.enclosure;

import java.io.File;
import java.nio.charset.StandardCharsets;
import javax.mail.internet.MimeBodyPart;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test case for {@link EnBinary}.
 *
 * @since 1.3.2
 */
public final class EnBinaryTest {

    /**
     * Temp directory.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @Rule
    public final transient TemporaryFolder temp = new TemporaryFolder();

    /**
     * EnBinary can add a file to the message.
     * @throws Exception If fails
     */
    @Test
    public void addsFileToMessage() throws Exception {
        final File file = this.temp.newFile();
        FileUtils.write(file, "hey, привет", StandardCharsets.UTF_8);
        final MimeBodyPart part = new EnBinary(
            file, "letter-другу.pdf", "text/plain"
        ).part();
        MatcherAssert.assertThat(
            part.getFileName(),
            Matchers.endsWith("другу.pdf")
        );
        MatcherAssert.assertThat(
            IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8),
            Matchers.endsWith("привет")
        );
    }

}
