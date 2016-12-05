[![Build Status](https://travis-ci.org/sandervanloock/neighbournet.svg?branch=master)](https://travis-ci.org/sandervanloock/neighbournet)
# NeighbourNET
## About

This application is for educational purposesl only.

The app consists of two modules: app and api.

### App

This is a lightweight ReactJs App, started from [React Starter Kit](https://github.com/kriasoft/react-starter-kit)

### API

This a [JHipster](https://jhipster.github.io/) application that uses [Jsoup](https://jsoup.org/) and [Crawler4j](https://github.com/yasserg/crawler4j).

## Getting started
### Run localy
To run the API, in neighbournet-api run:
`mvnw spring-boot:run -Dspring.profiles.active=dev`
### Additional configuration
To enable Google Geolocation API for converting adresses to latitude/longitude coordinates, put a file named `secret-properties.yml` on the classpath and add a property `google-geocode-api-key:XXXX`
### Prepare for production
To create executable JAR for production run:
`mvnw clean package -Pprod`

In react-app folder run
`npm install`
`npm run start`



