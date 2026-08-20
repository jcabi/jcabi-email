/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Authenticating credentials.
 * @since 1.0
 */
final class Verification extends Authenticator {

    /**
     * User name.
     */
    private final transient String user;

    /**
     * User password.
     */
    private final transient String password;

    /**
     * Public ctor.
     * @param usr User name
     * @param pwd User password
     */
    Verification(final String usr, final String pwd) {
        super();
        this.user = usr;
        this.password = pwd;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user, this.password);
    }
}
