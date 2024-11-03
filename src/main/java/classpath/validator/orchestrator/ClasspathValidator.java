package classpath.validator.orchestrator;

import classpath.validator.dependency.finder.DependencyFinderSpec;
import classpath.validator.dependency.finder.implementation.ClassDependencyFinder;
import classpath.validator.jar.validator.JarValidatorSpec;
import classpath.validator.jar.validator.implementation.JarValidator;
import classpath.validator.orchestrator.implementation.SingleClasspathValidator;

import java.io.IOException;
import java.util.List;

public abstract class ClasspathValidator {
    protected final DependencyFinderSpec dependencyFinder;
    protected final JarValidatorSpec jarValidator;

    public ClasspathValidator() {
        this.dependencyFinder = new ClassDependencyFinder();
        this.jarValidator = new JarValidator();
    }

    public abstract boolean validateClasspath(String classAbsolutePath, List<String> jarsAbsolutePaths) throws RuntimeException, IOException;

    public static ClasspathValidator getSingleClasspathValidator(){
        return new SingleClasspathValidator();
    }

}
