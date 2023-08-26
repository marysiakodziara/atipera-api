# Atipera-api
Welcome to the Atipera-api project! This project offers a user-friendly way to access information about a given user's repositories on GitHub using __`reactive programming and Spring WebFlux`__.

## Table of Contents
- [Features](#features)
- [Error Handling](#error_handling)
- [Endpoints](#endpoints)
- [Tests](#tests)

## Features <a name="features"/>
* **List Repositories:**
> As an API consumer, you can supply a GitHub username and set the Accept header 
to application/json to retrieve a list of the user's repositories that are not forks. The response includes:
- Repository Name
- Owner Login
- Branch Names and Last Commit SHA for each branch

## Error Handling <a name="error_handling"/>
> If the provided GitHub user does not exist, the API responds with a clear and informative 404 message.
```json
{
    "status": 404,
    "message": "User with given username does not exist"
}
```

>  If the Accept header is set to application/xml, the API responds with a 406 message to indicate an unsupported format.
```xml
<ErrorResponse>
    <status>406</status>
    <message>The application does not support XML response</message>
</ErrorResponse>
```

## Endpoints <a name="endpoints"/>
* **List User Repositories**
```console
GET /api/repositories?username={username}
Accept: application/json
```

## Tests <a name="tests"/>
> The project includes a comprehensive set of tests to ensure the functionality and reliability of the GitHubController using the JUnit and Spring WebTestClient frameworks.
  
