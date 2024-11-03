package classpath.validator.dependency.finder;

import java.util.Set;

public interface DependencyFinderSpec {
    Set<String> findDependencies(String classAbsolutePath);
}
