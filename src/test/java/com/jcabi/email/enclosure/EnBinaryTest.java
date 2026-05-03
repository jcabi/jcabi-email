/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email.enclosure;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import javax.mail.internet.MimeBodyPart;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test case for {@link EnBinary}.
 * @since 1.3.2
 */
final class EnBinaryTest {

    @Test
    @SuppressWarnings({
        "PMD.UnitTestContainsTooManyAsserts", "PMD.UnnecessaryLocalRule"
    })
    void addsFileToMessage(@TempDir final Path temp) throws Exception {
        Assumptions.assumeTrue("UTF-8".equals(Charset.defaultCharset().name()));
        final File file = temp.resolve("test.txt").toFile();
        FileUtils.write(file, "hey, привет", StandardCharsets.UTF_8);
        final MimeBodyPart part = new EnBinary(
            file, "letter-другу.pdf", "text/plain"
        ).part();
        MatcherAssert.assertThat(
            part.getFileName(),
            Matchers.endsWith("другу.pdf")
        );
        try (InputStream stream = part.getInputStream()) {
            MatcherAssert.assertThat(
                IOUtils.toString(stream, StandardCharsets.UTF_8),
                Matchers.endsWith("привет")
            );
        }
    }
}
