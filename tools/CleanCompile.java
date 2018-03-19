
package learningGame.tools;


// Own packages
import learningGame.tools.MultiTool;


// Java packages
import java.io.IOException;
import java.io.File;

import java.util.ArrayList;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;


public class CleanCompile {
    public static void cleanCompile(File dir) {
        ArrayList<File[]> filesInDir = MultiTool.listFilesAndPathsFromRootDir(dir, false);
        
        // Delete all class files.
        for (File[] file : filesInDir) {
            String name = file[0].getName();
            if (name.endsWith(".class") && !name.equals("CleanCompile.class") && !name.contains("MultiTool")) {
                file[0].delete();
            }
        }
        
        // Compile all files
        ArrayList<String> filesToCompileList = new ArrayList<String>();
        
        for (File[] file : filesInDir) {
            String name = file[0].getName();
            if (name.endsWith(".java") && !name.equals("CleanCompile.java")) {
                filesToCompileList.add(file[0].getPath());
            }
        }
        
        String[] filesToCompile = MultiTool.listToArray(filesToCompileList, String.class);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, filesToCompile);
    }
    
    public static void main(String[] args) {
        CleanCompile.cleanCompile(new File(System.getProperty("user.dir")));
        System.out.println("done");
    }
}