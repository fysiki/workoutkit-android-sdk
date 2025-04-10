# WorkoutKit demo app

## Overview
This demo app demonstrates how to retrieve a list of workout sessions and run it on the training interface offered by WorkoutKit.

## Getting started guide
The getting started guide is available as part of the WorkoutKit documentation. More info [here](../README.md).

## GraphQL Client Generation

We use **Apollo Android** to auto-generate models from .graphql files. Follow these steps to set it up:

### Configure Authentication
- Open or create the `local.properties` file in the `./WorkoutKitSDK/` directory
- Add the following lines and replace the placeholders with the server URL and authorization key that were provided by our team:
```
GraphQLServerUrl=https://server.com/graphql
GraphQLAuthKey=your_auth_key_here
```
- **Never commit this file to version control!**

### Install the Apollo plugin
- Open the sample app in Android Studio.
- Go to the menu File -> Settings -> Plugins and search for the "Apollo GraphQL" plugin in the marketplace.

### Fetch schema
- Use the Apollo plugin to get the schema through Tools -> Apollo -> Download Schema.
- **[Alternative method]** If there is an issue with the Apollo plugin, you can download the schema manually with gradlew:
```./gradlew downloadServiceApolloSchemaFromIntrospection```
