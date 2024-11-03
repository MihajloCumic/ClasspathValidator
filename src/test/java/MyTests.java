import classpath.validator.dependency.finder.DependencyFinderSpec;
import classpath.validator.dependency.finder.implementation.ClassDependencyFinder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class MyTests {
    private static final String absolutePath = "/home/cuma/jetbrains-inter/monorepo-final-projects/ClasspathValidator/src/test/resources";

    private static final String commonsJar = absolutePath + "/commons-io-2.16.1.jar";
    private static final String moduleAJar = absolutePath + "/ModuleA-1.0.jar";
    private static final String moduleBJar = absolutePath + "/ModuleB-1.0.jar";

    private static final String classA = absolutePath + "/com/jetbrains/internship2024/ClassA.java";
    private static final String anotherClassA = absolutePath + "/com/jetbrains/internship2024/another/ClassA.java";
    private static final String internalClassA = absolutePath + "/com/jetbrains/internship2024/InternalClassA.java";
    private static final String someAnotherClass = absolutePath + "/com/jetbrains/internship2024/SomeAnotherClass.java";
    private static final String someClassWith3rdPartyDependency = absolutePath + "/com/jetbrains/internship2024/SomeClassWith3dPartyDependency.java";

//    private static final String classB = absolutePath + "/ModuleB/ClassB.java";
//    private static final String classB1 = absolutePath + "/ModuleB/ClassB1.java";
//    private static final String classB2 = absolutePath + "/ModuleB/ClassB2.java";
    private static final DependencyFinderSpec dependencyFinderSpec = new ClassDependencyFinder();

    public static void main(String[] args) {
        Set<String> strs = recursiveRes(classA, "com.jetbrains.internship2024", new HashSet<>());
        for(String s: strs){
            System.out.println(s);
        }
    }

    private static String source = "/home/cuma/jetbrains-inter/monorepo-final-projects/ClasspathValidator/src/test/resources";

    public static Set<String> recursiveRes(String classPath, String packageName, Set<String> dependencies){
        Set<String> deps = dependencyFinderSpec.findDependencies(classPath);
        for(String dep: deps) {
            if (dependencies.contains(dep)) continue;
            dependencies.add(dep);
            if (dep.startsWith(packageName)) {
                String classRelativePath = dep.replace(".", "/");
                String classAbsolutePath = source + "/" + classRelativePath + ".java";
                if (doesPathExist(classAbsolutePath)) {
                    recursiveRes(classAbsolutePath, packageName, dependencies);
//                    Set<String> res = recursiveRes(classAbsolutePath, packageName, dependencies);
                    //dependencies.addAll(res);
                }
            }
        }
        return dependencies;
    }

    private static boolean doesPathExist(String path){
        Path filePath = Paths.get(path);
        return Files.exists(filePath);
    }
}
