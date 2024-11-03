package classpath.validator.orchestrator.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SingleClasspathValidatorTest {

    private final String absolutePath = "/home/cuma/jetbrains-inter/monorepo-final-projects/ClasspathValidator/src/test/resources";

    private final String commonsJar = absolutePath + "/commons-io-2.16.1.jar";
    private final String moduleAJar = absolutePath + "/ModuleA-1.0.jar";
    private final String moduleBJar = absolutePath + "/ModuleB-1.0.jar";

    private final String classA = absolutePath + "/ModuleA/ClassA.java";
    private final String anotherClassA = absolutePath + "/ModuleA/another/ClassA.java";
    private final String internalClassA = absolutePath + "/ModuleA/InternalClassA.java";
    private final String someAnotherClass = absolutePath + "/ModuleA/SomeAnotherClass.java";
    private final String someClassWith3rdPartyDependency = absolutePath + "/ModuleA/SomeClassWith3dPartyDependency.java";

    private final String classB = absolutePath + "/ModuleB/ClassB.java";
    private final String classB1 = absolutePath + "/ModuleB/ClassB1.java";
    private final String classB2 = absolutePath + "/ModuleB/ClassB2.java";

    private SingleClasspathValidator classpathValidator;

    @BeforeEach
    void setUp(){
        classpathValidator = new SingleClasspathValidator();
    }

    @Test
    void classPathNotExisting(){
        String path = absolutePath + "/does/not/exist.java";
        assertThrows(RuntimeException.class, () -> classpathValidator.validateClasspath(path, null));
        assertThrows(RuntimeException.class, () -> classpathValidator.validateClasspath(null, null));
    }

    @Test
    void JarPathNotExisting(){
        String path = absolutePath + "/doesnotexist.jar";
        List<String> jars = new ArrayList<>();
        jars.add(commonsJar);
        jars.add(path);
        jars.add(moduleAJar);

        assertThrows(RuntimeException.class, () -> classpathValidator.validateClasspath(classA, jars));
        assertThrows(RuntimeException.class, () -> classpathValidator.validateClasspath(classA, null));
    }

    @Test
    void e2eTest1(){
        List<String> jars = new ArrayList<>();
        jars.add(moduleBJar);
        boolean res = classpathValidator.validateClasspath(classB, jars);
        assertFalse(res);
    }

    @Test
    void e2eTest2(){
        List<String> jars = new ArrayList<>();
        jars.add(moduleAJar);
        jars.add(moduleBJar);
        boolean res = classpathValidator.validateClasspath(classB, jars);
        assertTrue(res);
    }

    @Test
    void e2eTest3(){
        List<String> jars = new ArrayList<>();
        jars.add(moduleAJar);
        boolean res = classpathValidator.validateClasspath(classA, jars);
        assertTrue(res);
    }

    @Test
    void e2eTest4(){
        List<String> jars = new ArrayList<>();
        jars.add(moduleAJar);
        boolean res = classpathValidator.validateClasspath(someAnotherClass, jars);
        assertFalse(res);
    }




}