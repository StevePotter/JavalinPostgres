

## Where to go from here?

Because of limited time, I stopped after meeting the requirements.  But there's so much more I would do if I had the time.  In no particular order:

## Running the app


### Running all locally

`docker compose up postgres`
Then you start app using IntelliJ

Want to build and run in the CLI?
```
./gradlew build 
java -jar build/libs/app.jar
```


docker build -t arrayapp .

docker run -p 8765:8765 arrayapp

### Running Postman Tests

newman run test/tests.postman_collection.json

### Self-describing API

I would use Open API to define the service API.  With that, we could:
1. Generate request/response payload classes
2. Generate routing code on the server
3. Create clients in multiple languages.  A CI/CD process would generate, test, and publish these to various repositories.

Validation
I got a litle lazy with the validation after not finding a good turnkey solution.  

Auth
session vs oauth jwt api key oh my
RBAC
- Tests
- Logging
- DB creds
- Use oauth2
- Integration tests
- Database migrations
- Frontend
- Strict linting
- Better exception handling
- Idempotency
- Profiling
- Monitoring