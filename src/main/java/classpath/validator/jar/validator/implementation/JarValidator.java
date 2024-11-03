package classpath.validator.jar.validator.implementation;

import classpath.validator.jar.validator.JarValidatorSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JarValidator implements JarValidatorSpec {
    @Override
    public boolean doClassesExistInJars(Set<String> jarAbsolutePaths, Set<String> classesQualifiedNames) {
        Set<String> classPaths = classesQualifiedNames.stream()
                .map(name -> name.replace(".", "/") + ".class")
                .collect(Collectors.toSet());

        // Cache all JAR entries first
        Map<String, Set<String>> jarEntries = jarAbsolutePaths.stream()
                .collect(Collectors.toMap(
                        path -> path,
                        path -> {
                            try (JarFile jarFile = new JarFile(path)) {
                                return jarFile.stream()
                                        .map(JarEntry::getName)
                                        .collect(Collectors.toSet());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ));
        return classPaths.parallelStream()
                .allMatch(classPath ->
                        jarEntries.values().stream()
                                .anyMatch(entries -> entries.contains(classPath))
                );
    }

    }
}
