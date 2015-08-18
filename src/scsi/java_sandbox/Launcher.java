package scsi.java_sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Launcher {

	public static void main(String[] args) {
		// Determine Unix or Windows
		String os = System.getProperty("os.name");
		// If Windows
		if (os.startsWith("Windows")) {
			// Attempt to find the JDK
			String JDKPath = null;
			
			// Start by getting java.home
			File JREPath = new File(System.getProperty("java.home"));
			
			// If the name of this dir is "jre", then we're currently inside of a jdk dir
			// and we can assume we're on a development build
			if (JREPath.getName().equals("jre")) {
				// Move up to the root jdk directory
				JDKPath = JREPath.getParent();
			}
			// Otherwise we're in a "jreX.X_X" dir and we need to see the other dirs next
			// to it in order to find the JDK installation (jdkX.X_X)
			else {
				File[] otherJavaInstallations = JREPath.getParentFile().listFiles();
				for (File javaInstallationRootDir : otherJavaInstallations) {
					if (javaInstallationRootDir.isDirectory() 
							&& javaInstallationRootDir.getName().startsWith("jdk")) {
						JDKPath = javaInstallationRootDir.getAbsolutePath();
					}
				}
			}
			
			if (JDKPath != null) {
				// Get the bin directory
				File JDKBinDir = new File(JDKPath + System.getProperty("file.separator") + "bin");
				
				// Setup a process that runs java -jar <path>\lib\Sandbox.jar
				ProcessBuilder builder = new ProcessBuilder(JDKBinDir.getAbsolutePath() + System.getProperty("file.separator") + "java.exe", "-jar", new File(System.getProperty("user.dir")).getParent() + System.getProperty("file.separator") + "lib" + System.getProperty("file.separator") + "Sandbox.jar");
				try {
					builder.start();
				} catch (IOException e) {
					System.out.println("Unable to start Sandbox.");
				}
			}
		}
	}
	
	public static String getCommandOutput(String command)  {
	    String output = null; //the string to return

	    Process process = null;
	    BufferedReader reader = null;
	    InputStreamReader streamReader = null;
	    InputStream stream = null;

	    try {
	        process = Runtime.getRuntime().exec(command);

	        //Get stream of the console running the command
	        stream = process.getInputStream();
	        streamReader = new InputStreamReader(stream);
	        reader = new BufferedReader(streamReader);

	        String currentLine = null;  //store current line of output from the cmd
	        StringBuilder commandOutput = new StringBuilder();  //build up the output from cmd
	        while ((currentLine = reader.readLine()) != null) {
	            commandOutput.append(currentLine);
	        }

	        int returnCode = process.waitFor();
	        if (returnCode == 0) {
	            output = commandOutput.toString();
	        }

	    } catch (IOException e) {
	        System.err.println("Cannot retrieve output of command");
	        System.err.println(e);
	        output = null;
	    } catch (InterruptedException e) {
	        System.err.println("Cannot retrieve output of command");
	        System.err.println(e);
	    } finally {
	        //Close all inputs / readers

	        if (stream != null) {
	            try {
	                stream.close();
	            } catch (IOException e) {
	                System.err.println("Cannot close stream input! " + e);
	            }
	        } 
	        if (streamReader != null) {
	            try {
	                streamReader.close();
	            } catch (IOException e) {
	                System.err.println("Cannot close stream input reader! " + e);
	            }
	        }
	        if (reader != null) {
	            try {
	                streamReader.close();
	            } catch (IOException e) {
	                System.err.println("Cannot close stream input reader! " + e);
	            }
	        }
	    }
	    //Return the output from the command - may be null if an error occured
	    return output;
	}

}
