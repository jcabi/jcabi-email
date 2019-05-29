<img src="http://img.jcabi.com/logo-square.svg" width="64px" height="64px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![Managed by Zerocracy](https://www.0crat.com/badge/C3RUBL5H9.svg)](https://www.0crat.com/p/C3RUBL5H9)
[![DevOps By Rultor.com](http://www.rultor.com/b/jcabi/jcabi-email)](http://www.rultor.com/p/jcabi/jcabi-email)

[![Build Status](https://travis-ci.org/jcabi/jcabi-email.svg?branch=master)](https://travis-ci.org/jcabi/jcabi-email)
[![PDD status](http://www.0pdd.com/svg?name=jcabi/jcabi-email)](http://www.0pdd.com/p?name=jcabi/jcabi-email)
[![Build status](https://ci.appveyor.com/api/projects/status/ueunlwvuxyiws57s/branch/master?svg=true)](https://ci.appveyor.com/project/yegor256/jcabi-email/branch/master)
[![Javadoc](https://javadoc.io/badge/com.jcabi/jcabi-email.svg)](http://www.javadoc.io/doc/com.jcabi/jcabi-email)

[![jpeek report](http://i.jpeek.org/com.jcabi/jcabi-email/badge.svg)](http://i.jpeek.org/com.jcabi/jcabi-email/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-email/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-email)
[![Dependencies](https://www.versioneye.com/user/projects/561ac442a193340f2f0011cb/badge.svg?style=flat)](https://www.versioneye.com/user/projects/561ac442a193340f2f0011cb)

More details are here: [email.jcabi.com](http://email.jcabi.com/).
This article explains how this library was designed:
[How Immutability Helps](http://www.yegor256.com/2014/11/07/how-immutability-helps.html).

It is an object-oriented email sending SDK for Java:

```java
Postman postman = new Postman.Default(
  new SMTP(
    new Token("user", "password").access(
      new Protocol.SMTP("smtp.gmail.com", 587)
    )
  )
);
postman.send(
  new Envelope.MIME()
    .with(new StSender("Yegor Bugayenko <yegor@jcabi.com>"))
    .with(new StRecipient("Jeff Lebowski", "jeff@gmail.com"))
    .with(new StSubject("dude, how are you?"))
    .with(new StBCC("my-boss@jcabi.com"))
    .with(new EnPlain("Hi, long time no see! :) Check my pic!"))
    .with(
      new EnBinary(
        new File("/tmp/picture.gif"),
        "my-picture.gif",
        "image/gif"
      )
    )
);
```

Make sure you have this dependencies:

```
<dependency>
  <groupId>javax.mail</groupId>
  <artifactId>mailapi</artifactId>
  <version>1.4.3</version>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>javax.mail</groupId>
  <artifactId>mail</artifactId>
  <version>1.5.0-b01</version>
  <scope>runtime</scope>
</dependency>
```

## Questions?

If you have any questions about the framework, or something doesn't work as expected,
please [submit an issue here](https://github.com/jcabi/jcabi-email/issues/new).

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```
$ mvn clean install -Pqulice
```
