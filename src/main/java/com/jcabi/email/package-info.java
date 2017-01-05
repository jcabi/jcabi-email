/**
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
 * <pre>&lt;depedency&gt;
 *   &lt;groupId&gt;com.jcabi&lt;/groupId&gt;
 *   &lt;artifactId&gt;jcabi-email&lt;/artifactId&gt;
 * &lt;/dependency&gt;</pre>
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 1.0
 * @see <a href="http://email.jcabi.com/">project website</a>
 */
package com.jcabi.email;
