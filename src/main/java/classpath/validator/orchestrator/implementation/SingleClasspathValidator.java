package classpath.validator.orchestrator.implementation;


import classpath.validator.orchestrator.ClasspathValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleClasspathValidator extends ClasspathValidator {

    @Override
    public boolean validateClasspath(String classAbsolutePath, List<String> jarsAbsolutePaths) throws RuntimeException, IOException {
        checkIfPathsExist(classAbsolutePath, jarsAbsolutePaths);

        Set<String> jars = new HashSet<>(jarsAbsolutePaths);
        Set<String> classDependencies = dependencyFinder.execute(classAbsolutePath);

        boolean res = jarValidator.doClassesExistInJars(jars, classDependencies);
        System.out.println(res);
        return res;
    }

    private void checkIfPathsExist(String classPath, List<String> jarsPaths) throws RuntimeException{
        if(!doesPathExist(classPath)) throw new RuntimeException("Path does not exist: " + classPath);
        for(String path: jarsPaths){
            if(!doesPathExist(path)) throw new RuntimeException("Path does not exist: " + path);
        }
    }

    private boolean doesPathExist(String path){
        Path filePath = Paths.get(path);
        return Files.exists(filePath);
    }
}
