package classpath.validator.dependency.finder.implementation;

import classpath.validator.dependency.finder.DependencyFinderSpec;
import classpath.validator.dependency.finder.implementation.utils.ClassQualifiedNameResolver;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
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
    public Set<String> findDependencies(String classAbsolutePath) {
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

    private static class ClassOrInterfaceTypeCollector extends VoidVisitorAdapter<Set<String>>{
        @Override
        public void visit(ClassOrInterfaceType n, Set<String> arg) {
            super.visit(n, arg);
            arg.add(n.getNameWithScope());
        }

    }
}
