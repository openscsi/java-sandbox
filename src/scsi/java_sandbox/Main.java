package scsi.java_sandbox;

import java.io.IOException;


public class Main {

	public static void main(String[] args) {
		
		System.out.println(System.getProperty("java.home"));
		
		String imports = "";
		
		String methods =
				"int add(int num1, int num2) {\n" +
						"return num1 + num2;\n" +
				"}";
		
		String code =
				"System.out.println(add(5, 6));";
		
		try {
			String output = CodeRunner.run(imports, methods, code);
			System.out.println(output);		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
