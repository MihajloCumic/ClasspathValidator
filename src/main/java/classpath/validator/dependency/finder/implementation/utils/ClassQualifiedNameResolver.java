package classpath.validator.dependency.finder.implementation.utils;


import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassQualifiedNameResolver {
    public static final Set<String> JAVA_PACKAGE_PREFIXES = Set.of(
            "java.",
            "javax.",
            "sun.",
            "com.sun.",
            "jdk."
    );

    public static Set<String> resolveQualifiedNames(String packageName, Set<String> importNames, Set<String> classOrInterfaceNames){
        classOrInterfaceNames.removeIf(className -> isClassOrInterfaceImported(className, importNames));
        classOrInterfaceNames.removeIf(ClassQualifiedNameResolver::isClassOrInterfaceFromJavaLang);

        Set<String> classesOrInterfacesFromSamePackage = classOrInterfaceNames.stream()
                .map(name -> packageName + "." + name)
                .collect(Collectors.toSet());

        importNames.removeIf(ClassQualifiedNameResolver::isJavaStandardLibraryClass);
        Set<String> resolvedQualifiedNames = new HashSet<>(importNames);
        resolvedQualifiedNames.addAll(classesOrInterfacesFromSamePackage);
        return resolvedQualifiedNames;
    }

    private static boolean isClassOrInterfaceFromJavaLang(String className){
        try{
            ClassLoader classLoader = Class.forName("java.lang." + className).getClassLoader();
            return classLoader == null;

        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean isClassOrInterfaceImported(String className, Set<String> importNames){
        return importNames.stream()
                .anyMatch(importName -> importName.endsWith(className));
    }

    private static boolean isJavaStandardLibraryClass(String name){
        return JAVA_PACKAGE_PREFIXES.stream()
                .anyMatch(name::startsWith);
    }

}
