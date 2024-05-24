# Webapp

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 17.3.2.

## Generating the Api Client

The Api Client is generated using the OpenAPI Generator.
When the backend is running, it can be executed by using `npm run build-api`.

For it to work, Java needs to be installed locally on the system and JAVA_HOME needs to be set as an environment variable.
(At least Java 11 is required)

Also note that for some reason JSON is not used by default for the generated API client so in every Controller the `RequestMapping` annotation needs to have `produces = "application/json"`.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.
