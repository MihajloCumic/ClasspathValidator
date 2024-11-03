package classpath.validator.dependency.finder.implementation;

import classpath.validator.dependency.finder.DependencyFinderSpec;
import classpath.validator.dependency.finder.implementation.utils.ClassQualifiedNameResolver;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassDependencyFinder implements DependencyFinderSpec {

    @Override
    public Set<String> execute(String mainClassAbsolutePath) throws RuntimeException, IOException {
        Path classFilePath = Paths.get(mainClassAbsolutePath);
        if (!Files.exists(classFilePath)) throw new RuntimeException("File does not exist.");

        CompilationUnit cu = StaticJavaParser.parse(Files.newInputStream(classFilePath));

        String mainPackageName = cu.getPackageDeclaration()
                .map(PackageDeclaration::getNameAsString)
                .orElse("");

        int index = mainClassAbsolutePath.indexOf(mainPackageName.replace(".", "/"));
        String sourcePath = null;
        if (index != -1) {
            sourcePath = mainClassAbsolutePath.substring(0, index);
        }else throw new RuntimeException("Could not extract source path.");
        if(sourcePath.isBlank() || sourcePath.isEmpty()) throw new RuntimeException("Could not extract source path");

        return findAllDependencies(mainClassAbsolutePath, mainPackageName, sourcePath, new HashSet<>());

    }

    private Set<String> findAllDependencies(String classPath,String mainPackageName, String sourcePath, Set<String> dependencies){
        Set<String> classDependencies = findDependencies(classPath);
        for(String dep: classDependencies) {
            if (dependencies.contains(dep)) continue;
            dependencies.add(dep);
            if (dep.startsWith(mainPackageName)) {
                String classRelativePath = dep.replace(".", "/");
                String classAbsolutePath = sourcePath + "/" + classRelativePath + ".java";
                if (doesPathExist(classAbsolutePath)) {
                    findAllDependencies(classAbsolutePath,mainPackageName, sourcePath, dependencies);
                }
            }
        }
        return dependencies;
    }

    private Set<String> findDependencies(String classAbsolutePath) {
        Path classFilePath = Paths.get(classAbsolutePath);
        if (!Files.exists(classFilePath)) throw new RuntimeException("File does not exist.");

        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(Files.newInputStream(classFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String packageName = cu.getPackageDeclaration()
                .map(PackageDeclaration::getNameAsString)
                .orElse("");

        Set<String> importNames = cu.getImports().stream()
                .map(ImportDeclaration::getNameAsString)
                .collect(Collectors.toSet());

        VoidVisitorAdapter<Set<String>> classOrInterfaceCollector = new ClassOrInterfaceTypeCollector();
        Set<String> classOrInterfaceNames = new HashSet<>();
        classOrInterfaceCollector.visit(cu, classOrInterfaceNames);

        return ClassQualifiedNameResolver.resolveQualifiedNames(packageName, importNames, classOrInterfaceNames);
    }

    private static boolean doesPathExist(String path){
        Path filePath = Paths.get(path);
        return Files.exists(filePath);
    }

    private static class ClassOrInterfaceTypeCollector extends VoidVisitorAdapter<Set<String>>{
        @Override
        public void visit(ClassOrInterfaceType n, Set<String> arg) {
            super.visit(n, arg);
            arg.add(n.getNameWithScope());
        }

        @Override
        public void visit(MethodCallExpr n, Set<String> arg) {
            super.visit(n, arg);
            if(n.getScope().isPresent()){
                String name = n.getScope().get().toString();
                if (!name.isEmpty() && !name.isBlank()){
                    if(Character.isUpperCase(name.charAt(0))){
                        String[] parts = name.split("\\.");
                        arg.add(parts[0]);
                    }
                }
            }
        }

    }
}
