package classpath.validator.orchestrator.implementation;

import classpath.validator.dependency.finder.implementation.ClassDependencyFinder;
import classpath.validator.jar.validator.implementation.JarValidator;
import classpath.validator.orchestrator.ClasspathValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SingleClasspathValidatorTest {
    private String absolutePathPrefix = "/home/cuma/jetbrains-inter/monorepo-final-projects/ClasspathValidator/src/test/resources";

    private String jarA = absolutePathPrefix + "/ModuleA-1.0.jar";
    private String jarB = absolutePathPrefix + "/ModuleB-1.0.jar";
    private String jarCommons = absolutePathPrefix + "/commons-io-2.16.1.jar";

    private String moduleAPath = absolutePathPrefix  + "/ModuleA/src/main/java/com/jetbrains/internship2024";
    private String moduleBPath = absolutePathPrefix + "/ModuleB/src/main/java/com/jetbrains/internship2024";

    private String classA = moduleAPath + "/ClassA.java";
    private String internalClassA = moduleAPath + "/InternalClassA.java";
    private String someAnotherClass = moduleAPath + "/SomeAnotherClass.java";
    private String someClass3rdDep = moduleAPath + "/SomeClassWith3dPartyDependency.java";
    private String anotherClassA = moduleAPath + "/another/ClassA.java";

    private String classB = moduleBPath + "/ClassB.java";
    private String classB1 = moduleBPath + "/ClassB1.java";
    private String classB2 = moduleBPath + "/ClassB2.java";

    private ClasspathValidator classpathValidator;

    @BeforeEach
    void setUp(){
        classpathValidator = ClasspathValidator.getSingleClasspathValidator(new ClassDependencyFinder(), new JarValidator());
    }

    @Test
    void nonExistingPath(){
        String path = "/does/not/exist/ClassA.java";
        assertThrows(RuntimeException.class, () -> classpathValidator.isClassRunnable(path, new ArrayList<>()));
    }

    @Test
    void nullJarList(){
        assertThrows(RuntimeException.class, () -> classpathValidator.isClassRunnable(classA, null));
    }

    @Test
    void classBFalse(){
        List<String> jars = new ArrayList<>();
        jars.add(jarB);
        try {
            assertFalse(classpathValidator.isClassRunnable(classB, jars));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void classBTrue(){
        List<String> jars = new ArrayList<>();
        jars.add(jarB);
        jars.add(jarA);
        try {
            assertTrue(classpathValidator.isClassRunnable(classB, jars));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void classAFalse(){
        List<String> jars = new ArrayList<>();
        jars.add(jarB);
        try {
            assertFalse(classpathValidator.isClassRunnable(classA, jars));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void classATrue(){
        List<String> jars = new ArrayList<>();
        jars.add(jarA);
        try {
            assertTrue(classpathValidator.isClassRunnable(classA, jars));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void someAnotherClassFalse(){
        List<String> jars = new ArrayList<>();
        jars.add(jarA);
        try {
            assertFalse(classpathValidator.isClassRunnable(someAnotherClass, jars));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void someAnotherClassTrue(){
        List<String> jars = new ArrayList<>();
        jars.add(jarA);
        jars.add(jarCommons);
        try {
            assertTrue(classpathValidator.isClassRunnable(someAnotherClass, jars));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void classB1False(){
        List<String> jars = new ArrayList<>();
        jars.add(jarA);
        jars.add(jarCommons);
        try {
            assertFalse(classpathValidator.isClassRunnable(classB1, jars));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void classB1True(){
        List<String> jars = new ArrayList<>();
        jars.add(jarB);
        try {
            assertTrue(classpathValidator.isClassRunnable(classB1, jars));
        } catch (IOException e) {
            fail();
        }
    }









}