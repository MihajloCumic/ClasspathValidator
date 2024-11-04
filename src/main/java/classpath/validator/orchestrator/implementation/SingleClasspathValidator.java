package classpath.validator.orchestrator.implementation;


import classpath.validator.dependency.finder.DependencyFinderSpec;
import classpath.validator.jar.validator.JarValidatorSpec;
import classpath.validator.orchestrator.ClasspathValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleClasspathValidator extends ClasspathValidator {

    public SingleClasspathValidator(DependencyFinderSpec dependencyFinder, JarValidatorSpec jarValidator) {
        super(dependencyFinder, jarValidator);
    }

    @Override
    public boolean isClassRunnable(String classAbsolutePath, List<String> jarsAbsolutePaths) throws RuntimeException, IOException {
        checkIfPathsExist(classAbsolutePath, jarsAbsolutePaths);

        Set<String> jars = new HashSet<>(jarsAbsolutePaths);
        Set<String> classDependencies = dependencyFinder.execute(classAbsolutePath);

        boolean res = jarValidator.doClassesExistInJars(jars, classDependencies);
        System.out.println(res);
        return res;
    }

    private void checkIfPathsExist(String classPath, List<String> jarsPaths) throws RuntimeException{
        if(!doesPathExist(classPath)) throw new RuntimeException("Path does not exist: " + classPath);
        if(jarsPaths == null) throw new RuntimeException("Jar list is null.");
        for(String path: jarsPaths){
            if(!doesPathExist(path)) throw new RuntimeException("Path does not exist: " + path);
        }
    }

    private boolean doesPathExist(String path){
        Path filePath = Paths.get(path);
        return Files.exists(filePath);
    }
}
