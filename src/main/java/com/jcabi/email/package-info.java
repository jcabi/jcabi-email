/*
 * SPDX-FileCopyrightText: Copyright (c) 2014-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */

/**
 * Object-oriented email sending SDK.
 *
 * <p>The best way to use it is to make an instance of
 * {@link com.jcabi.email.Postman.Default}:
 *
 * <pre> Postman postman = new Postman.Default(
 *   new SMTP(
 *     new Token("user", "password").access(
 *       new Protocol.SMTP("smtp.gmail.com", 587)
 *     )
 *   )
 * );
 * postman.send(
 *   new Envelope.MIME(
 *     new Array&lt;Stamp&gt;(
 *       new StSender("Yegor Bugayenko &lt;yegor@jcabi.com&gt;"),
 *       new StRecipient("Jeff Lebowski &lt;jeff@gmail.com&gt;"),
 *       new StSubject("dude, how are you?"),
 *       new StBCC("my-boss@jcabi.com")
 *     ),
 *     new Array&lt;Enclosure&gt;(
 *       new EnPlain("Hi, long time no see! :) Check my pic!"),
 *       new EnBinary(
 *         new File("/tmp/picture.gif"),
 *         "my-picture.gif",
 *         "image/gif"
 *       )
 *     )
 *   )
 * );</pre>
 *
 * <p>The only dependency you need is (check our latest version available
 * at <a href="http://email.jcabi.com">email.jcabi.com</a>):
 *
 * <pre>&lt;dependency&gt;
 *   &lt;groupId&gt;com.jcabi&lt;/groupId&gt;
 *   &lt;artifactId&gt;jcabi-email&lt;/artifactId&gt;
 * &lt;/dependency&gt;</pre>
 *
 * @since 1.0
 * @see <a href="http://email.jcabi.com/">project website</a>
 */
package com.jcabi.email;
