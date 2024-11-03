package classpath.validator.dependency.finder.implementation;

import classpath.validator.dependency.finder.DependencyFinderSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClassDependencyFinderTest {

    private String moduleAPath = "/home/cuma/jetbrains-inter/monorepo-final-projects/ClasspathValidator/src/test/resources/ModuleA/src/main/java/com/jetbrains/internship2024";
    private String moduleBPath = "/home/cuma/jetbrains-inter/monorepo-final-projects/ClasspathValidator/src/test/resources/ModuleB/src/main/java/com/jetbrains/internship2024";

    private String classA = moduleAPath + "/ClassA.java";
    private String internalClassA = moduleAPath + "/InternalClassA.java";
    private String someAnotherClass = moduleAPath + "/SomeAnotherClass.java";
    private String someClass3rdDep = moduleAPath + "/SomeClassWith3dPartyDependency.java";
    private String anotherClassA = moduleAPath + "/another/ClassA.java";

    private String classB = moduleBPath + "/ClassB.java";
    private String classB1 = moduleBPath + "/ClassB1.java";
    private String classB2 = moduleBPath + "/ClassB2.java";

    private DependencyFinderSpec dependencyFinder;

    @BeforeEach
    void setUp(){
        dependencyFinder = new ClassDependencyFinder();
    }

    @Test
    void nonExistingPath(){
        String path = "/does/not/exist/ClassA.java";
        assertThrows(RuntimeException.class, () -> dependencyFinder.execute(path));
    }

    @Test
    void classATest(){
        Set<String> result = new HashSet<>();
        result.add("com.jetbrains.internship2024.InternalClassA");
        result.add("com.jetbrains.internship2024.ClassA");
        try {
            Set<String> value = dependencyFinder.execute(classA);
            assertEquals(result, value);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void internalClassATest(){
        Set<String> result = new HashSet<>();
        result.add("com.jetbrains.internship2024.InternalClassA");
        result.add("com.jetbrains.internship2024.ClassA");
        try {
            Set<String> value = dependencyFinder.execute(internalClassA);
            assertEquals(result, value);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void someAnotherClassTest(){
        Set<String> result = new HashSet<>();
        result.add("com.jetbrains.internship2024.another.ClassA");
        result.add("com.jetbrains.internship2024.SomeClassWith3dPartyDependency");
        result.add("org.apache.commons.io.FilenameUtils");

        try {
            Set<String> value = dependencyFinder.execute(someAnotherClass);
            assertEquals(result, value);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void class3dTest(){
        Set<String> result = new HashSet<>();
        result.add("org.apache.commons.io.FilenameUtils");

        try {
            Set<String> value = dependencyFinder.execute(someClass3rdDep);
            assertEquals(result, value);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void classBTest(){
        Set<String> result = new HashSet<>();
        result.add("com.jetbrains.internship2024.ClassA");

        try {
            Set<String> value = dependencyFinder.execute(classB);
            assertEquals(result, value);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void classB1Test(){
        Set<String> result = new HashSet<>();
        result.add("com.jetbrains.internship2024.ClassB2");
        result.add("com.jetbrains.internship2024.ClassB1");

        try {
            Set<String> value = dependencyFinder.execute(classB1);
            assertEquals(result, value);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void classB2Test(){
        Set<String> result = new HashSet<>();
        result.add("com.jetbrains.internship2024.ClassB2");
        result.add("com.jetbrains.internship2024.ClassB1");

        try {
            Set<String> value = dependencyFinder.execute(classB2);
            assertEquals(result, value);
        } catch (IOException e) {
            fail();
        }
    }












}