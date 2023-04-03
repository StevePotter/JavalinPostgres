A weekend project to set up a tiny REST API using Kotlin.  Just a few routes for signup, login, and logout.

## Stack

- Kotlin
- Gradle with Kotlin DSL
- [Javalin web framework](https://javalin.io)
- [KTorm OPR](https://www.ktorm.org/)
- Postgres
- Docker

All the latest and greatest versions as of this commit

## Run it

```
docker compose up
```

The all runs on port 8765

Then POST /users with this body:
```
{
    "email": "myemail@myemail.me",
    "password": "testAS81!!ash"
}
```

## Development

To start the app, you'll need a running Postgres server.  To start it up:
```
docker compose up postgres
```
The schema is created in `sql/create-db.sql`

I used Amazon Correct 19 for the JDK.  I suggest you do the same.

If you are using IntelliJ, there is a run configuration set up for you called WebApp.

If you want to build and run using CLI:
```
./gradlew build 
java -jar build/libs/app.jar
```

To build the app's dockerfile:
```
docker build -t javalin-app .
```

You can then run this via
```
docker run -p 8765:8765 javalin-app

```

### Running Postman tests

There is a small package of test requests.  To run those:
```
newman run test/tests.postman_collection.json
```

## What's missing?

This is a decent starting point for a web app.  But there is so much more that would need to be done in order to make it production-grade.  In no particular order:

**Auth**

This app uses simple password hashing with a salt and API tokens provided in the request body.  Don't use that.  Sleep better and give users a better experience with something like OAuth 2.  Decide between using API keys, sessions, JWTs (or some hybrid).  Make your life easier by using a service like Okta or the great products offered by every cloud provider.

[Here](https://developer.okta.com/blog/2020/10/19/ktor-kotlin) is a nice article although it's it a bit heavy on the marketing side for Okta.

**Self-describing API**

I would use Open API to define the service API.  With that, we could:
1. Generate request/response payload classes
2. Generate routing code on the server
3. Create clients in multiple languages.  A CI/CD process would generate, test, and publish these to various repositories.

**Validation**

I got a little lazy with the validation after not finding a good turnkey solution.  Every input field should be validated and it should be simple for devs to configure it.

**Tests**

Tests are missing.  Given time, I would add unit test coverage.  More importantly, I would implement full integration tests that use a live database with seed data.

**More**

- Logging
- Database migrations
- Frontend
- Idempotency
- Profiling
- Monitoring
- CI/CD
