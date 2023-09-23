# Front-end. Decision Engine (Inbank test assignment)

## Requirements

Angular 16.2.x, Node.js 18.x

## Structure of the source code

### Source code layers

- `_models` - data models used in front-end app
- `_pages` - pages of user interface
- `_components` - sub-components of user interface pages that page consists of
- `_services` - service layer for API requests

## Application configurations

NGINX server configuration [nginx.conf.template](nginx.conf.template),
together with **environmental** variables.

* `API_HOST` - base domain for making requests to backend API

**NB! Please look at parameters description used as arguments in paragraph [Installation guide](#installation-guide)**

## Installation guide

Application is being built as `Docker image`.

```sh
docker build --no-cache -t decision-engine-frontend .
```

## Local configuration for front-end

### Server base domain

In case of running application locally, it is possible to set up the base domain for API requests in file 
[.proxy.conf.json](proxy.conf.json), please check `target` property.

## Guide to install and run locally

In order to install and run front end application locally:

1. Run the command to install all necessary dependencies listed in `package.json`
in case something has been changed

```sh
npm install
```
2. Run the command to start the application

```sh
npm start
```