package classpath.validator.orchestrator;

import classpath.validator.dependency.finder.DependencyFinderSpec;
import classpath.validator.jar.validator.JarValidatorSpec;
import classpath.validator.orchestrator.implementation.SingleClasspathValidator;

import java.io.IOException;
import java.util.List;

public abstract class ClasspathValidator {
    protected final DependencyFinderSpec dependencyFinder;
    protected final JarValidatorSpec jarValidator;

    public ClasspathValidator(DependencyFinderSpec dependencyFinder, JarValidatorSpec jarValidator) {
        this.dependencyFinder = dependencyFinder;
        this.jarValidator = jarValidator;
    }

    public abstract boolean validateClasspath(String classAbsolutePath, List<String> jarsAbsolutePaths) throws RuntimeException, IOException;

    public static ClasspathValidator getSingleClasspathValidator(DependencyFinderSpec dependencyFinder, JarValidatorSpec jarValidator){
        return new SingleClasspathValidator(dependencyFinder, jarValidator);
    }

}
