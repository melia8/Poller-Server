# Poller-Server

This is the server side of the poller application

It is written in Java 17 using Spring Boot with an H2 in memory database

### Running the server

From the command line in the root of the repo run :
`./mvnw spring-boot:run
`

### Create a poll

Post to : `localhost:8080/poll`

Payload :
`    {"title" : "Who will win premier league",  
    "options": ["Liverpool", "Arsenal", "Chelsea"]
    }`

### Vote on a poll

Post to : `localhost:8080/vote`

Payload: `{
    "pollId":1,
    "name": "Liverpool"
}`

### Get current poll

Get: `localhost:8080/poll`

### Get Specific poll

Get: `localhost:8080/poll/2`

### Get all votes for a poll

Get: `localhost:8080/poll/2/votes`

## Testing

I opted to create one integration test to cover all scenarios

It can be run with: `./mvnw clean verify`
