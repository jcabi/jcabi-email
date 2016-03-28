/**
 * Copyright (c) 2014-2015, jcabi.com
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
package com.jcabi.email;

import com.google.common.collect.ImmutableMap;
import com.jcabi.aspects.Immutable;
import java.util.Map;
import javax.net.ssl.SSLSocketFactory;

/**
 * Mail network protocol.
 * @author Piotr Kotlicki (piotr.kotlicki@gmail.com)
 * @version $Id$
 * @since 1.0
 */
@Immutable
public interface Protocol {

    /**
     * Guarantee of access for protocol.
     * @return Entry parameters.
     */
    Map<String, String> entries();

    /**
     * SNMP protocol.
     */
    final class SMTP implements Protocol {
        /**
         * SMTP host.
         */
        private final transient String host;

        /**
         * SMTP port.
         */
        private final transient int port;

        /**
         * Public ctor.
         * @param hst SMTP Host
         * @param prt SMTP Port
         */
        public SMTP(final String hst, final int prt) {
            this.host = hst;
            this.port = prt;
        }

        @Override
        public Map<String, String> entries() {
            return new ImmutableMap.Builder<String, String>()
                .put("mail.smtp.auth", Boolean.TRUE.toString())
                .put("mail.smtp.host", this.host)
                .put("mail.smtp.port", Integer.toString(this.port))
                .build();
        }
    }

    /**
     * SMTPS protocol.
     */
    final class SMTPS implements Protocol {
        /**
         * SMTPS host.
         */
        private final transient String host;

        /**
         * SMTPS port.
         */
        private final transient int port;

        /**
         * Public ctor.
         * @param hst SMTPS Host
         * @param prt SMTPS Port
         */
        public SMTPS(final String hst, final int prt) {
            this.host = hst;
            this.port = prt;
        }

        @Override
        public Map<String, String> entries() {
            return new ImmutableMap.Builder<String, String>()
                .putAll(new Protocol.SMTP(this.host, this.port).entries())
                .put(
                    "mail.smtp.ssl.checkserveridentity",
                    Boolean.TRUE.toString()
                ).put(
                    "mail.smtp.socketFactory.class",
                    SSLSocketFactory.class.getName()
                ).put(
                    "mail.smtp.socketFactory.port",
                    Integer.toString(this.port)
                ).put("mail.smtp.socketFactory.fallback", "false").build();
        }
    }
}
