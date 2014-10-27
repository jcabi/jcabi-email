<img src="http://img.jcabi.com/logo-square.svg" width="64px" height="64px" />

[![Made By Teamed.io](http://img.teamed.io/btn.svg)](http://www.teamed.io)
[![DevOps By Rultor.com](http://www.rultor.com/b/jcabi/jcabi-email)](http://www.rultor.com/p/jcabi/jcabi-email)

[![Build Status](https://travis-ci.org/jcabi/jcabi-email.svg?branch=master)](https://travis-ci.org/jcabi/jcabi-email)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-email/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-email)

More details are here: [email.jcabi.com](http://email.jcabi.com/)

It is an object-oriented email sending SDK for Java:

```java
Postman postman = new Postman.Default(
  new SMTP("smtp.googlemail.com", 465, "username", "password")
);
postman.send(
  new Envelope.MIME()
    .with(new StSender("Yegor Bugayenko <yegor@jcabi.com>"))
    .with(new StRecipient("Jeff Lebowski <jeff@gmail.com>"))
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
