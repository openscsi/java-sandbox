# java-sandbox

The Eclipse project for SCSI's Java Sandbox tool. Perfect for beginners who want to learn Java code without complicated Object Orientation or IDEs. Download the latest version [here](https://github.com/bbatliner/java-sandbox/raw/master/Sandbox_v2.2.1.zip).

## Developing

While developing, Eclipse will launch any Java files with the JDK, so you can just Run `GUI.java` and you will have the ability to compile and run code in the sandbox.

## Building

When running outside of Eclipse, Java will by default use the installed JRE (which does not support code compilation and execution). So, `Launcher.java` exists to find an installed JDK and launch the GUI with the JDK. Please build the following directory structure when looking to distribute to users:

/<br>
&nbsp;&nbsp;/bin<br>
&nbsp;&nbsp;&nbsp;&nbsp;Launcher.jar (`Launcher.java`)<br>
&nbsp;&nbsp;/lib<br>
&nbsp;&nbsp;&nbsp;&nbsp;Sandbox.jar (`GUI.java`)<br>
&nbsp;&nbsp;/license<br>
&nbsp;&nbsp;&nbsp;&nbsp;licenses<br>
&nbsp;&nbsp;/themes<br>
&nbsp;&nbsp;&nbsp;&nbsp;themes<br>
