public class PathSplitTest {
    public static void main(String[] args) {
        String absolutePath = "/home/cuma/jetbrains-inter/monorepo-final-projects/ClasspathValidator/src/test/resources/com/jetbrains/internship2024/SomeAnotherClass.java";
        String serachPackage = "com.jetbrains.internship2024";
        int index = absolutePath.indexOf(serachPackage.replace(".", "/"));
        if (index != -1) {
            String prefix = absolutePath.substring(0, index);
            System.out.println(prefix); // Output: home/folder/com/
        }
    }
}
