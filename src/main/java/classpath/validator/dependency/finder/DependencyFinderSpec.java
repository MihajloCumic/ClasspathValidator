package classpath.validator.dependency.finder;

import java.io.IOException;
import java.util.Set;

public interface DependencyFinderSpec {
   Set<String> execute(String mainClassAbsolutePath) throws RuntimeException, IOException;
}
