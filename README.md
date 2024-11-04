# Java Class Dependency Validator

This Java library provides a way to validate whether a Java class can be run with a classpath made out of a list of passed jars.

## Features

The library comprises three main components:

- **Dependency Finder** -  Finds all classes and interfaces used by a given Java class, including transitively used classes and excluding standard Java standard library classes.
- **Jar Validator** - Checks if all the required classes can be found in a list of provided JAR files
- **Classpath Validator** - Provides the main API for using the library by orchestrating the Dependency Finder and Jar Validator.

## Components

### Dependency Finder (`ClassDependencyFinder`)

The Dependency Finder component analyzes the target class and:
- Uses the JavaParser library to find all classes and interfaces that the target class references.
- Resolves fully qualified names for each dependency.
- Filters out Java standard library classes and recursively finds dependencies for all user-defined classes within the same project.

Output: A `Set<String>` of fully qualified class names required to run the target class.

### Jar Validator (`JarValidator`)

The Jar Validator component verifies the availability of the required classes by:
- Caching the entries of each JAR file into a `Map<String, Set<String>>`, where the key is the path to the JAR file and the value is a set of the fully qualified class names found in that JAR.
- Checking the presence of each required class using parallel streams to ensure efficiency.
- Returning `true` if all classes are available; otherwise, returns `false`.

### Classpath Validator (`ClasspathValidator`)

This component serves as the library's main API. It coordinates the Dependency Finder and Jar Validator to:
1. Extract the dependencies of the target class.
2. Validate whether all dependencies are available within the provided JAR files.

## Installation

To use this library, you need Java 17 and Gradle. Additionally, add the following dependency:
**Groovy Gradle**
```groovy
dependencies {
    implementation 'com.github.javaparser:javaparser-symbol-solver-core:3.26.2'
}
```
**Kotlin DSL Gradle**
```kotlin
dependencies {
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.26.2")
}
```
**Maven**
```xml
<dependency>
    <groupId>com.github.javaparser</groupId>
    <artifactId>javaparser-symbol-solver-core</artifactId>
    <version>3.26.2</version>
</dependency>
```
## Usage

The main entry point for this library is the `ClasspathValidator` class. Here's an example of how to use it:

```java

ClasspathValidator validator = ClasspathValidator.getSingleClasspathValidator(new ClassDependencyFinder(), new JarValidator());
boolean isValid = validator.isClassRunnable(
    "absolute/path/to/MainClass.java",
    Arrays.asList("absolute/path/to/dependency1.jar", "absolute/path/to/dependency2.jar")
);
