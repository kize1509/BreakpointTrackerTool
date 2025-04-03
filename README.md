# BreakpointTrackerTool

This project uses [Gradle](https://gradle.org/).

## Project Structure

This project is structured as a multi-module Gradle project, consisting of two modules:

* **`frontend-module`**: Contains the frontend application, responsible for the user interface and static assets serving.
* **`plugin-module`**: Contains the IntelliJ IDEA plugin, responsible for tracking breakpoints and communicating with the frontend.

## Building and Running

To build and run the application, use the *Gradle* tool window in IntelliJ IDEA (by clicking the Gradle icon in the right-hand toolbar) or run the following commands directly from the terminal:

* **Run `./gradlew run`**: Builds and runs the `frontend-module`. This will start the frontend application.
* **Run `./gradlew build`**: Builds both `frontend-module` and `plugin-module`.
* **Run `./gradlew check`**: Runs all checks, including tests, for both modules.
* **Run `./gradlew clean`**: Cleans all build outputs for both modules.
* **Run `./gradlew buildPlugin`**: Builds the `plugin-module` specifically, creating the plugin JAR file.
* **Run `./gradlew runIde`**: Runs a development instance of IntelliJ IDEA with the `plugin-module` installed. This is useful for testing the plugin.

**Note:** It is highly recommended to use the Gradle Wrapper (`./gradlew`). This ensures that the correct version of Gradle is used for building the project.

[Learn more about the Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).

[Learn more about Gradle tasks](https://docs.gradle.org/current/userguide/command_line_interface.html#common_tasks).

## Project Configuration

* **Dependencies and Versioning**: This project uses a version catalog (see `gradle/libs.versions.toml`) to declare and manage dependencies and their versions.
* **Build and Configuration Caches**: The project utilizes both build cache and configuration cache (see `gradle.properties`) to speed up the build process.

## IntelliJ IDEA Plugin Development

The `plugin-module` is an IntelliJ IDEA plugin. To develop and test the plugin:

1.  Build the plugin using `./gradlew buildPlugin`.
2.  Run a development instance of IntelliJ IDEA with the plugin installed using `./gradlew runIde`.
3.  Running the plugin tool, frontend automatically gets hosted on the first free port.

## Additional Notes

* Make sure you have a compatible Java Development Kit (JDK) installed.
* IntelliJ IDEA is recommended for developing this project.
* For plugin development, ensure you have the IntelliJ IDEA Community or Ultimate Edition.
