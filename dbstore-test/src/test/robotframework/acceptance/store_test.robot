*** Settings ***
Documentation    DBStore Tests

Library  Collections
Library  HttpLibrary.HTTP

*** Variables ***
${SERVER}   localhost:8080
${CONTEXT}  /dbstore-web/store

*** Keywords ***
Create POST Request
    Set Suite Variable                          ${body}       Sample Request Body
    [return]                                    ${body}

*** Test Cases ***
POST Request Test
    ${requestBody}=                             Create POST Request
    Create Http Context                         ${SERVER}               http
    Set Request Header                          Content-Type            text/plain
    Set Request Header                          Accept                  text/plain
    Set Request Body                            ${requestBody}
    POST                                        ${CONTEXT}
    Response Status Code Should Equal           201
    Response Should Have Header                 location
    ${location}=                                Get Response Header     location
    ${location}=                                Get From List           ${location}             0
    Set Suite Variable                          ${location}             ${location}
    Log                                         ${location}

GET Request Test
    Create Http Context                         ${SERVER}
    Set Request Header                          Content-Type            text/plain
    Set Request Header                          Accept                  text/plain
    GET                                         ${location}
    Response Status Code Should Equal           200
    ${responseBody}=                            Get Response Body
    Log                                         ${responseBody}
    Should Match                                ${responseBody}         ${body}