package scsi.java_sandbox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CodeRunner {

	private static final String PACKAGE_NAME = "test";
	private static final String CLASS_NAME = "Sandbox";

	private static final String WRAPPER_A = "package " + PACKAGE_NAME + ";" + System.getProperty("line.separator");
	private static final String WRAPPER_B = System.getProperty("line.separator") + "public class " + CLASS_NAME + " {" + System.getProperty("line.separator") + "public " + CLASS_NAME + "() {" + System.getProperty("line.separator");
	private static final String WRAPPER_C = System.getProperty("line.separator") + "}" + System.getProperty("line.separator");
	private static final String WRAPPER_D = System.getProperty("line.separator") + "}";

	public static String run(String imports, String methods, String code) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		// Construct the source code
		String source = WRAPPER_A + imports + WRAPPER_B + code + WRAPPER_C + methods + WRAPPER_D;

		// Create a temporary file to store the source code
		File sourceFile = File.createTempFile(CLASS_NAME, ".java");
		File root = sourceFile.getParentFile();

		// Create the directory in the temporary directory that will hold the source class (for package naming reasons)
		File packageDir = new File(sourceFile.getParent() + System.getProperty("file.separator") + PACKAGE_NAME);
		packageDir.mkdir();

		// Delete the source file if it exists already
		Files.deleteIfExists(new File(sourceFile.getParent() + System.getProperty("file.separator") + PACKAGE_NAME + System.getProperty("file.separator") + CLASS_NAME + ".java").toPath());

		// Renames the source file to the correct name (no unique key prefix)
		sourceFile = Files.move(sourceFile.toPath(), new File(sourceFile.getParent() + System.getProperty("file.separator") + PACKAGE_NAME + System.getProperty("file.separator") + CLASS_NAME + ".java").toPath(), StandardCopyOption.REPLACE_EXISTING).toFile();

		// Write the source code to the temporary file
		FileWriter fw = new FileWriter(sourceFile);
		fw.append(source);
		fw.close();

		// Redirect stderr to capture any compilation errors
		ByteArrayOutputStream baosStderr = new ByteArrayOutputStream();
		PrintStream stderr = System.err;
		PrintStream myStderr = new PrintStream(baosStderr);
		System.setErr(myStderr);

		// Compile the source file
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		int success = compiler.run(null, null, null, sourceFile.getPath());

		// If compilation was successful
		if (success == 0) {
			// Redirect stdout to capture the program's output
			ByteArrayOutputStream baosStdout = new ByteArrayOutputStream();
			PrintStream stdout = System.out;
			PrintStream myStdout = new PrintStream(baosStdout);
			System.setOut(myStdout);

			// Load class
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
			Class.forName(PACKAGE_NAME + "." + CLASS_NAME, true, classLoader).newInstance();

			// Return stderr and stdout to the System default
			System.setErr(stderr);
			System.setOut(stdout);

			// Return any output from the program
			String output = baosStdout.toString();
			return output;
		}
		// Else If compilation was not successful
		else {
			// Return stderr to the System default
			System.setErr(stderr);

			// Return the error
			String error = baosStderr.toString();
			return error;
		}
	}
}
