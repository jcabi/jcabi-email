<img src="https://www.jcabi.com/logo-square.svg" width="64px" height="64px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](https://www.rultor.com/b/jcabi/jcabi-email)](https://www.rultor.com/p/jcabi/jcabi-email)

[![mvn](https://github.com/jcabi/jcabi-email/actions/workflows/mvn.yml/badge.svg)](https://github.com/jcabi/jcabi-email/actions/workflows/mvn.yml)
[![PDD status](https://www.0pdd.com/svg?name=jcabi/jcabi-email)](https://www.0pdd.com/p?name=jcabi/jcabi-email)
[![Javadoc](https://javadoc.io/badge/com.jcabi/jcabi-email.svg)](https://www.javadoc.io/doc/com.jcabi/jcabi-email)
[![jpeek report](https://i.jpeek.org/com.jcabi/jcabi-email/badge.svg)](https://i.jpeek.org/com.jcabi/jcabi-email/)
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

Make sure you have these dependencies:
```xml
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
