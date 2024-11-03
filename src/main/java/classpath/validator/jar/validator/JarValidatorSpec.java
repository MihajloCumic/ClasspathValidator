package classpath.validator.jar.validator;

import java.util.Set;

public interface JarValidatorSpec {
    boolean doClassesExistInJars(Set<String> jarAbsolutePaths, Set<String> classesQualifiedNames);
}
