package scsi.java_sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

@SuppressWarnings("serial")
public class GUI extends JPanel implements ActionListener {
	
	// Themes and colors
	private static Theme SUBLIME_THEME;
	private static Theme DEFAULT_THEME;
	private final static Color SUBLIME_FG = new Color(248, 248, 242);
	private final static Color SUBLIME_BG = new Color(39, 40, 34);
	private final static Color DEFAULT_FG = Color.BLACK;
	private final static Color DEFAULT_BG = Color.WHITE;
	
	// Save directory
	private static final File SAVE_DIRECTORY = new File(
			FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath()
			+ System.getProperty("file.separator")
			+ "My Sandbox Files");
	
	// Terminal output separator
	private final static String TERMINAL_SEPARATOR = "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
	
	// Screen dimensions
	private final static Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
	
	//GUI Components
	private JButton runButton;
	private JButton clearTerminalButton;
	private JButton toggleThemeButton;
	private JButton saveButton;
	private JButton loadButton;
	private RTextScrollPane importsScrollPane;
	private RSyntaxTextArea importsInput;
	private RTextScrollPane methodsScrollPane;
	private RSyntaxTextArea methodsInput;
	private RTextScrollPane codeScrollPane;
	private RSyntaxTextArea codeInput;
	private JScrollPane terminalScrollPane;
	private JTextPane terminalOutput;
	private Theme currentTheme;
	private Color terminalTextColor;
	
	public GUI() {
		super(new GridBagLayout());
		
		// Create save directory
		SAVE_DIRECTORY.mkdir();
		
		// Setup some screen size metrics
		double screenWidth = SCREEN.getWidth();
		double screenHeight = SCREEN.getHeight();
		FontMetrics defaultEditorFontMetrics = getFontMetrics(RSyntaxTextArea.getDefaultFont());
		int lineHeight = defaultEditorFontMetrics.getHeight();
		int charWidth = defaultEditorFontMetrics.charWidth('x');
		int linesPerScreenHeight = (int) (screenHeight / lineHeight);
		int charsPerScreenWidth = (int) (screenWidth / charWidth);
		
		// Load source highlighting themes
		try {
			SUBLIME_THEME = Theme.load(new FileInputStream(new File("..\\themes\\sublime.xml")));
			DEFAULT_THEME = Theme.load(new FileInputStream(new File("..\\themes\\default.xml")));
		} catch (FileNotFoundException e) {
			System.err.println("Theme file(s) not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not load theme(s).");
			e.printStackTrace();
		}
		
		GridBagConstraints c = new GridBagConstraints();
		
		runButton = new JButton("Run Code");
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 5, 5);
		add(runButton, c);
		runButton.addActionListener(this);
		
		clearTerminalButton = new JButton("Clear Terminal");
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 5, 5);
		add(clearTerminalButton, c);
		clearTerminalButton.addActionListener(this);
		
		toggleThemeButton = new JButton("Toggle Theme");
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 5, 5);
		add(toggleThemeButton, c);
		toggleThemeButton.addActionListener(this);
		
		saveButton = new JButton("Save");
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 5, 5);
		add(saveButton, c);
		saveButton.addActionListener(this);
		
		loadButton = new JButton("Load");
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 5, 5);
		add(loadButton, c);
		loadButton.addActionListener(this);
		
		// Native Swing implementation
//		importsInput = new JEditorPane();
//		importsInput.setEditable(true);
//		importsInput.setPreferredSize(new Dimension((int)(screenWidth*0.5), (int)(screenHeight*0.2)));
//		importsInput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
//		importsInput.setText("// Import any additional libraries here" + System.getProperty("line.separator") 
//							+ "// Example: import java.util.ArrayList;");
//		Document doc = importsInput.getDocument();
//		if (doc instanceof PlainDocument) {
//			doc.putProperty(PlainDocument.tabSizeAttribute, 4);
//		}
//		importsScrollPane = new JScrollPane(importsInput);
//		importsScrollPane.setPreferredSize(importsInput.getPreferredSize());
//		c = new GridBagConstraints();
//		c.gridx = 1;
//		c.gridy = 0;
//		c.gridwidth = 1;
//		c.gridheight = 3;
//		c.fill = GridBagConstraints.BOTH;
//		c.insets = new Insets(10, 10, 5, 5);
//		add(importsScrollPane, c);
		
		// External source code highlighting library implementation
		importsInput = new RSyntaxTextArea((int)(linesPerScreenHeight*0.2), (int)(charsPerScreenWidth*0.5));
		importsInput.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		importsInput.setText("// Import any additional libraries here" + System.getProperty("line.separator")
				+ "// Example: import java.util.ArrayList;" + System.getProperty("line.separator") + System.getProperty("line.separator"));
		importsScrollPane = new RTextScrollPane(importsInput);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 5, 5);
		add(importsScrollPane, c);
		
		
		// Native Swing implementation
//		methodsInput = new JEditorPane();
//		methodsInput.setEditable(true);
//		methodsInput.setPreferredSize(new Dimension((int)(screenWidth*0.5), (int)(screenHeight*0.3)));
//		methodsInput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
//		methodsInput.setText("// Write any custom methods here" + System.getProperty("line.separator")
//							+ "// You only have to specify the return type and the method name" + System.getProperty("line.separator")
//							+ "// Example: int add(int num1, int num2) { ... }");
//		doc = methodsInput.getDocument();
//		if (doc instanceof PlainDocument) {
//			doc.putProperty(PlainDocument.tabSizeAttribute, 4);
//		}
//		methodsScrollPane = new JScrollPane(methodsInput);
//		methodsScrollPane.setPreferredSize(methodsInput.getPreferredSize());
//		c = new GridBagConstraints();
//		c.gridx = 1;
//		c.gridy = 4;
//		c.gridwidth = 1;
//		c.gridheight = 3;
//		c.fill = GridBagConstraints.BOTH;
//		c.insets = new Insets(10, 10, 5, 5);
//		add(methodsScrollPane, c);
		
		// External source code highlighting library implementation
		methodsInput = new RSyntaxTextArea((int)(linesPerScreenHeight*0.3), (int)(charsPerScreenWidth*0.5));
		methodsInput.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		methodsInput.setText("// Write any custom methods here" + System.getProperty("line.separator")
							+ "// You only have to specify the return type and the method name" + System.getProperty("line.separator")
							+ "// Example: int add(int num1, int num2) { ... }" + System.getProperty("line.separator") + System.getProperty("line.separator"));
		methodsScrollPane = new RTextScrollPane(methodsInput);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 5, 5);
		add(methodsScrollPane, c);
		
		// Native Swing implementation
//		codeInput = new JEditorPane();
//		codeInput.setEditable(true);
//		codeInput.setPreferredSize(new Dimension((int)(screenWidth*0.5), (int)(screenHeight*0.3)));
//		codeInput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
//		codeInput.setText("// Write your code here" + System.getProperty("line.separator")
//						+ "// Note that only stdout (default System.out) will appear in the terminal on the right" + System.getProperty("line.separator")
//						+ "// Example: System.out.println(\"Hello, World!\");");
//		doc = codeInput.getDocument();
//		if (doc instanceof PlainDocument) {
//			doc.putProperty(PlainDocument.tabSizeAttribute, 4);
//		}
//		codeScrollPane = new JScrollPane(codeInput);
//		codeScrollPane.setPreferredSize(codeInput.getPreferredSize());
//		c = new GridBagConstraints();
//		c.gridx = 1;
//		c.gridy = 7;
//		c.gridwidth = 1;
//		c.gridheight = 3;
//		c.fill = GridBagConstraints.BOTH;
//		c.insets = new Insets(5, 10, 10, 5);
//		add(codeScrollPane, c);
		
		// External source code highlighting library implementation
		codeInput = new RSyntaxTextArea((int)(linesPerScreenHeight*0.3), (int)(charsPerScreenWidth*0.5));
		codeInput.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		
		codeInput.setText("// Write your code here" + System.getProperty("line.separator")
				+ "// Note that only stdout (default System.out) will appear in the terminal on the right" + System.getProperty("line.separator")
				+ "// Example: System.out.println(\"Hello, World!\");" + System.getProperty("line.separator") + System.getProperty("line.separator"));
		codeScrollPane = new RTextScrollPane(codeInput);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 10, 5);
		add(codeScrollPane, c);
		
		terminalOutput = new JTextPane();
		terminalOutput.setEditable(false);
		terminalOutput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		terminalOutput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		terminalScrollPane = new JScrollPane(terminalOutput);
		terminalScrollPane.setPreferredSize(new Dimension((int)(screenWidth*0.35), (int)(screenHeight*0.2)));
		terminalScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 10;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 5, 10, 10);
		add(terminalScrollPane, c);
		
		// Set the current theme to the default
		currentTheme = DEFAULT_THEME;
		terminalTextColor = DEFAULT_FG;
		terminalOutput.setBackground(DEFAULT_BG);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(runButton)) {
			String output = "";
			try {
				String imports = importsInput.getText() == null ? "" : importsInput.getText();
				String methods = methodsInput.getText() == null ? "" : methodsInput.getText();
				String code = codeInput.getText() == null ? "" : codeInput.getText();
				output = CodeRunner.run(imports, methods, code);
			} catch (Exception e1) {
				output = "Error: Unknown compilation error.\n\n" + e1.getStackTrace()[0].toString();
			} finally {
				if (terminalOutput.getText().length() == 0) {
					appendToPane(terminalOutput, output, terminalTextColor);
				}
				else {
					appendToPane(terminalOutput, System.getProperty("line.separator"), terminalTextColor);
					appendToPane(terminalOutput, TERMINAL_SEPARATOR, terminalTextColor);
					appendToPane(terminalOutput, System.getProperty("line.separator"), terminalTextColor);
					appendToPane(terminalOutput, output, terminalTextColor);
				}
			}
		}
		else if (e.getSource().equals(clearTerminalButton)) {
			terminalOutput.setText("");
		}
		else if (e.getSource().equals(toggleThemeButton)) {
			toggleTheme();
		}
		else if (e.getSource().equals(saveButton)) {
			JFileChooser saveDialog = new JFileChooser();
			saveDialog.setCurrentDirectory(SAVE_DIRECTORY);
			saveDialog.addChoosableFileFilter(new FileNameExtensionFilter("Sandbox files", "sndbx"));
			saveDialog.setAcceptAllFileFilterUsed(false);
			int returnVal = saveDialog.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String fileName = saveDialog.getSelectedFile().getName().endsWith(".sndbx") ? 
									saveDialog.getSelectedFile().getAbsolutePath()
									: saveDialog.getSelectedFile()+".sndbx";
				try (FileWriter writer = new FileWriter(fileName)) {
					writer.write(serializeCode());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(this, 
							"Error: Unable to save." 
							+ System.getProperty("line.separator") 
							+ System.getProperty("line.separator") 
							+ e1.getMessage());
					e1.printStackTrace();
				}
			}
		}
		else if (e.getSource().equals(loadButton)) {
			JFileChooser loadDialog = new JFileChooser();
			loadDialog.setCurrentDirectory(SAVE_DIRECTORY);
			loadDialog.addChoosableFileFilter(new FileNameExtensionFilter("Sandbox files", "sndbx"));
			loadDialog.setAcceptAllFileFilterUsed(false);
			int returnVal = loadDialog.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				deserializeCode(loadDialog.getSelectedFile());
			}
		}
	}
	
	private void deserializeCode(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			StringBuffer[] segments = new StringBuffer[3];
			for (int i = 0; i < 3; i++) {
				segments[i] = new StringBuffer();
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.startsWith(TERMINAL_SEPARATOR)) {
						// Remove the last newline char(s)
						segments[i] = new StringBuffer(
								segments[i].substring(
										0, 
										segments[i].length() - System.getProperty("line.separator").length()
								)
						);
						break;
					}
					
					segments[i].append(line + System.getProperty("line.separator"));
				}
			}
			
			importsInput.setText(segments[0].toString());
			methodsInput.setText(segments[1].toString());
			// Remove a mysterious trailing "CR" char
			codeInput.setText(segments[2].toString().substring(0, segments[2].length()-1));
			
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(this, 
					"Error: File not found." 
					+ System.getProperty("line.separator") 
					+ System.getProperty("line.separator") 
					+ e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this, 
					"Error: Unable to load." 
					+ System.getProperty("line.separator") 
					+ System.getProperty("line.separator") 
					+ e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	private String serializeCode() {
		return importsInput.getText() + System.getProperty("line.separator") 
				+ TERMINAL_SEPARATOR + System.getProperty("line.separator")
				+ methodsInput.getText() + System.getProperty("line.separator")
				+ TERMINAL_SEPARATOR + System.getProperty("line.separator")
				+ codeInput.getText();
	}
	
	private void toggleTheme() {
		if (currentTheme != null) {
			// If running the default theme
			if (currentTheme.equals(DEFAULT_THEME)) {
				// Change current theme
				currentTheme = SUBLIME_THEME;
				
				// Apply sublime theme to inputs
				SUBLIME_THEME.apply(importsInput);
				SUBLIME_THEME.apply(methodsInput);
				SUBLIME_THEME.apply(codeInput);
				
				// Change terminal bg color
				terminalOutput.setBackground(SUBLIME_BG);
				// Change terminal fg color
				changeExistingTextColor(terminalOutput, SUBLIME_FG);
				terminalTextColor = SUBLIME_FG;
			}
			// Else if running the sublime theme
			else {
				// Change current theme
				currentTheme = DEFAULT_THEME;
				// Apply default theme to inputs
				DEFAULT_THEME.apply(importsInput);
				DEFAULT_THEME.apply(methodsInput);
				DEFAULT_THEME.apply(codeInput);
				
				// Change terminal bg color
				terminalOutput.setBackground(DEFAULT_BG);
				// Change terminal fg color
				changeExistingTextColor(terminalOutput, DEFAULT_FG);
				terminalTextColor = DEFAULT_FG;
			}
		}
	}
	
	private void changeExistingTextColor(JTextPane textPane, Color color) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        
        String existingText = textPane.getText().replace("\r\r", "\r");
        
        textPane.setCharacterAttributes(aset, false);
        textPane.setText(existingText);
	}
	
	// Shamelessly copied (and slightly modified for readability) from 
	// http://stackoverflow.com/questions/9650992/how-to-change-text-color-in-the-jtextarea
	private void appendToPane(JTextPane textPane, String msg, Color color) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(aset, false);
        textPane.setEditable(true);
        textPane.replaceSelection(msg);
        textPane.setEditable(false);
    }

	public static void createAndShowGUI() {
		JFrame frame = new JFrame("Java Sandbox");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setMinimumSize(SCREEN);
		frame.setLocationRelativeTo(null);

		JComponent newContentPane = new GUI();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
