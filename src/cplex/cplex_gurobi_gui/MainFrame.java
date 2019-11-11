package cplex.cplex_gurobi_gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.SubstanceConstants.ImageWatermarkKind;
import org.jvnet.substance.api.SubstanceSkin;
import org.jvnet.substance.skin.AutumnSkin;
import org.jvnet.substance.skin.CremeCoffeeSkin;
import org.jvnet.substance.skin.DustSkin;
import org.jvnet.substance.skin.OfficeBlue2007Skin;
import org.jvnet.substance.skin.SaharaSkin;
import org.jvnet.substance.skin.SubstanceAutumnLookAndFeel;
import org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel;
import org.jvnet.substance.skin.SubstanceDustLookAndFeel;
import org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel;
import org.jvnet.substance.skin.SubstanceSaharaLookAndFeel;
import org.jvnet.substance.watermark.SubstanceImageWatermark;

/**
 * @author:Dylan
 * @date:2017Äê10ÔÂ22ÈÕ ÏÂÎç2:00:56
 */
public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 9051617138106620571L;
	private List<Variable> variables = null;
	private Objective objective = null;
	private List<Constraint> constraints = null;
	private Algorithm algorithm = null;
	private OptResult optResult = null;
	private Set<Character> keyBoardRestriction = null;
	private double width = 0;
	private double[][] size = null;
	private double[][] demand = null;
	private int skinType = 0;
	private int cspSolverType = 0;
	private int backgroundType = 3;
	private JPanel contentPane = null;
	private JTextField objectiveField = null;
	private ButtonGroup minMaxButtonGroup = null;
	private ButtonGroup solverButtonGroup = null;
	private ButtonGroup rowColButtonGroup = null;
	private ButtonGroup cspSolverButtonGroup = null;
	private JCheckBox decimalCheckBox = null;
	private JCheckBox ceilingCheckBox = null;
	private JCheckBox integerCheckBox = null;
	private JCheckBox maxCheckBox = null;
	private JCheckBox minCheckBox = null;
	private JCheckBox cplexCheckBox = null;
	private JCheckBox gurobiCheckBox = null;
	private JCheckBox byRowCheckBox = null;
	private JCheckBox byColCheckBox = null;
	private JTextArea constraintsArea = null;
	private JTextArea variablesArea = null;
	private JTextArea resultsArea = null;
	private JTextArea resultsCSPArea = null;
	private JScrollPane jspResult = null;
	private JScrollPane jspVariables = null;
	private JScrollPane jspConstraints = null;
	private JScrollPane jspCSPResult = null;
	private JButton btnSolve = null;
	private JButton btnReset = null;
	private JButton btnLoad = null;
	private JButton btnSave = null;
	private JButton btnSolveCSP = null;
	private JLabel constraintsLabel = null;
	private JLabel variablesLabel = null;
	private JLabel resultsLabel = null;
	private JLabel objectiveLabel = null;
	private JLabel cspLabel = null;
	private JLabel resultsCSPLabel = null;
	private JMenuBar menuBar = null;
	private JMenu importFile = null;
	private JMenu changeSkin = null;
	private JMenu changeBackground = null;
	private JMenuItem importModelItem = null;
	private JMenuItem exportResultItem = null;
	private JMenuItem loadConfigItem = null;
	private JMenuItem loadModelItem = null;
	private ButtonGroup fileButtonGroup = null;
	private ButtonGroup skinButtonGroup = null;
	private ButtonGroup bgButtonGroup = null;
	private JFileChooser fileChooser = null;
	private String modelFilePath = null;
	private String configFilePath = null;
	private JSeparator separator = null;

	// Create the frame
	public MainFrame() {
		try {
			UIManager.setLookAndFeel(new SubstanceOfficeBlue2007LookAndFeel());
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		setIcon();
		changeBg(backgroundType);
		setAutoRequestFocus(false);
		setResizable(false);
		setTitle("Dylan's Solver");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "     Are you sure to leave£¿", "Attention£¡",
						JOptionPane.YES_NO_OPTION);
				if (result == 0) {
					System.exit(0);
				}
			}
		});

		setBounds(100, 100, 450, 830);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setContentPane(contentPane);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		importFile = new JMenu("File(F)");
		importFile.setMnemonic('F');
		importFile.setToolTipText("Import model or export result");
		importFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		importFile.setIcon(new ImageIcon(MainFrame.class.getResource("/imgs/file.png")));
		importFile.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		menuBar.add(importFile);
		changeSkin = new JMenu("Skin(E)");
		changeSkin.setMnemonic('E');
		changeSkin.setToolTipText("Change skin");
		changeSkin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		changeSkin.setIcon(new ImageIcon(MainFrame.class.getResource("/imgs/mSkin.png")));
		changeSkin.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		menuBar.add(changeSkin);
		changeBackground = new JMenu("Background(B)");
		changeBackground.setMnemonic('B');
		changeBackground.setToolTipText("Change background");
		changeBackground.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		changeBackground.setIcon(new ImageIcon(MainFrame.class.getResource("/imgs/mBg.png")));
		changeBackground.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 14));
		menuBar.add(changeBackground);
		skinButtonGroup = new ButtonGroup();
		initializeSkinMenu(changeSkin);
		bgButtonGroup = new ButtonGroup();
		initializeBgMenu(changeBackground);
		fileButtonGroup = new ButtonGroup();
		initializFileMenu(importFile);

		minMaxButtonGroup = new ButtonGroup();
		maxCheckBox = new JCheckBox("max");
		maxCheckBox.setBounds(37, 10, 57, 23);
		maxCheckBox.setToolTipText("Maximum problem");
		maxCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		maxCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(maxCheckBox);
		minCheckBox = new JCheckBox("min");
		minCheckBox.setBounds(96, 10, 57, 23);
		minCheckBox.setToolTipText("Minimum problem");
		minCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		minCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(minCheckBox);
		minMaxButtonGroup.add(maxCheckBox);
		minMaxButtonGroup.add(minCheckBox);

		solverButtonGroup = new ButtonGroup();
		cplexCheckBox = new JCheckBox("Cplex");
		cplexCheckBox.setToolTipText("Cplex solver");
		cplexCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		cplexCheckBox.setBounds(60, 296, 72, 23);
		cplexCheckBox.setSelected(true);
		cplexCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(cplexCheckBox);

		gurobiCheckBox = new JCheckBox("Gurobi");
		gurobiCheckBox.setToolTipText("Gurobi solver");
		gurobiCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		gurobiCheckBox.setBounds(150, 296, 80, 23);
		gurobiCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(gurobiCheckBox);
		solverButtonGroup.add(cplexCheckBox);
		solverButtonGroup.add(gurobiCheckBox);

		rowColButtonGroup = new ButtonGroup();
		byRowCheckBox = new JCheckBox("By Row");
		byRowCheckBox.setToolTipText("Model by row");
		byRowCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		byRowCheckBox.setBounds(60, 266, 88, 23);
		byRowCheckBox.setSelected(true);
		byRowCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(byRowCheckBox);

		byColCheckBox = new JCheckBox("By Col");
		byColCheckBox.setToolTipText("Model by column");
		byColCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		byColCheckBox.setBounds(150, 266, 103, 23);
		byColCheckBox.setSelected(true);
		byColCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(byColCheckBox);
		rowColButtonGroup.add(byRowCheckBox);
		rowColButtonGroup.add(byColCheckBox);

		objectiveField = new JTextField();
		objectiveField.setBounds(190, 11, 215, 21);
		objectiveField.setToolTipText("Input objective");
		objectiveField.setOpaque(false);
		objectiveField.setForeground(new Color(255, 69, 0));
		objectiveField.setFont(new Font("Monaco", Font.BOLD, 10));
		objectiveField.addKeyListener(new KeyListener() {
			// Can only input '0'...'9',' ','.','/','-'
			@Override
			public void keyTyped(KeyEvent e) {
				char keyCh = e.getKeyChar();
				keyBoardRestriction = new HashSet<Character>();
				char[] rule = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '/', ' ', '-'};
				for (int i = 0; i < rule.length; i++) {
					keyBoardRestriction.add(rule[i]);
				}
				if (!keyBoardRestriction.contains(keyCh)) {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		contentPane.add(objectiveField);

		constraintsArea = new JTextArea();
		constraintsArea.setBounds(37, 58, 204, 200);
		constraintsArea.setToolTipText("Input constraints");
		constraintsArea.setOpaque(false);
		constraintsArea.setForeground(new Color(0, 129, 245));
		constraintsArea.setFont(new Font("Monaco", Font.BOLD, 10));
		constraintsArea.addKeyListener(new KeyListener() {
			// Can only input '0'...'9',' ','.','/','<','=','>','\n','-'
			@Override
			public void keyTyped(KeyEvent e) {
				char keyCh = e.getKeyChar();
				keyBoardRestriction = new HashSet<Character>();
				char[] rule = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '/', ' ', '\n', '<', '=', '>','-' };
				for (int i = 0; i < rule.length; i++) {
					keyBoardRestriction.add(rule[i]);
				}
				if (!keyBoardRestriction.contains(keyCh)) {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		contentPane.add(constraintsArea);

		jspConstraints = new JScrollPane(constraintsArea);
		jspConstraints.setBounds(37, 58, 204, 200);
		contentPane.add(jspConstraints);

		variablesArea = new JTextArea();
		variablesArea.setBounds(261, 58, 144, 200);
		variablesArea.setToolTipText("Input variables");
		variablesArea.setOpaque(false);
		variablesArea.setFont(new Font("Monaco", Font.BOLD, 10));
		variablesArea.addKeyListener(new KeyListener() {
			// Can only input '0'...'9',' ','.','/','\n','i','n','t','u','m','b','o','l','f','-'
			@Override
			public void keyTyped(KeyEvent e) {
				char keyCh = e.getKeyChar();
				keyBoardRestriction = new HashSet<Character>();
				char[] rule = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '/', ' ', '\n', 'i', 'n', 't',
						'u', 'm', 'b', 'o', 'l', 'f' ,'-'};
				for (int i = 0; i < rule.length; i++) {
					keyBoardRestriction.add(rule[i]);
				}
				if (!keyBoardRestriction.contains(keyCh)) {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		contentPane.add(variablesArea);

		jspVariables = new JScrollPane(variablesArea);
		jspVariables.setBounds(261, 58, 144, 200);
		contentPane.add(jspVariables);

		resultsArea = new JTextArea();
		resultsArea.setBounds(37, 345, 368, 140);
		resultsArea.setEditable(false);
		resultsArea.setOpaque(false);
		resultsArea.setForeground(new Color(21, 114, 33));
		resultsArea.setFont(new Font("Monaco", Font.BOLD, 10));
		resultsArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		resultsArea.setToolTipText("Output results");
		contentPane.add(resultsArea);

		jspResult = new JScrollPane(resultsArea);
		jspResult.setBounds(37, 345, 368, 140);
		contentPane.add(jspResult);

		btnSolve = new JButton("Solve(Alt+S)");
		btnSolve.setBounds(286, 266, 94, 23);
		btnSolve.addActionListener(this);
		btnSolve.setToolTipText("Solve the model");
		btnSolve.setMnemonic('S');
		btnSolve.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnSolve);

		btnReset = new JButton("Reset(Alt+R)");
		btnReset.setBounds(286, 296, 94, 23);
		btnReset.addActionListener(this);
		btnReset.setToolTipText("Reset the model");
		btnReset.setMnemonic('R');
		btnReset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnReset);

		constraintsLabel = new JLabel("Constraints:");
		constraintsLabel.setBounds(37, 39, 100, 15);
		constraintsLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		contentPane.add(constraintsLabel);

		variablesLabel = new JLabel("Variables:");
		variablesLabel.setBounds(261, 39, 100, 15);
		variablesLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		contentPane.add(variablesLabel);

		resultsLabel = new JLabel("Results:");
		resultsLabel.setBounds(37, 326, 54, 15);
		resultsLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		contentPane.add(resultsLabel);

		objectiveLabel = new JLabel("Obj:");
		objectiveLabel.setBounds(160, 14, 80, 15);
		objectiveLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		contentPane.add(objectiveLabel);

		separator = new JSeparator();
		separator.setBounds(37, 500, 368, 1);
		separator.setBackground(new Color(0, 0, 0));
		contentPane.add(separator);

		cspLabel = new JLabel("CSPsolver:");
		cspLabel.setBounds(37, 510, 100, 15);
		cspLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		contentPane.add(cspLabel);

		btnLoad = new JButton("Load(Alt+L)");
		btnLoad.setBounds(174, 560, 94, 23);
		btnLoad.addActionListener(this);
		btnLoad.setToolTipText("Load the csp parameter file");
		btnLoad.setMnemonic('L');
		btnLoad.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnLoad);

		btnSave = new JButton("Save(Alt+A)");
		btnSave.setBounds(286, 560, 94, 23);
		btnSave.addActionListener(this);
		btnSave.setToolTipText("Save the csp result");
		btnSave.setMnemonic('A');
		btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnSave);

		btnSolveCSP = new JButton("Solve(Alt+O)");
		btnSolveCSP.setBounds(62, 560, 94, 23);
		btnSolveCSP.addActionListener(this);
		btnSolveCSP.setToolTipText("Solve CSP");
		btnSolveCSP.setMnemonic('O');
		btnSolveCSP.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnSolveCSP);
		
		cspSolverButtonGroup = new ButtonGroup();
		decimalCheckBox = new JCheckBox("Decimal");
		decimalCheckBox.setToolTipText("Decimal solution");
		decimalCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		decimalCheckBox.setBounds(100, 530, 88, 23);
		decimalCheckBox.setSelected(true);
		decimalCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(decimalCheckBox);
		
		ceilingCheckBox = new JCheckBox("Ceiling");
		ceilingCheckBox.setToolTipText("Ceiling solution");
		ceilingCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		ceilingCheckBox.setBounds(190, 530, 88, 23);
		ceilingCheckBox.setSelected(true);
		ceilingCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(ceilingCheckBox);
		
		integerCheckBox = new JCheckBox("Integer");
		integerCheckBox.setToolTipText("Integer solution");
		integerCheckBox.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		integerCheckBox.setBounds(280, 530, 88, 23);
		integerCheckBox.setSelected(true);
		integerCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(integerCheckBox);
		
		cspSolverButtonGroup.add(decimalCheckBox);
		cspSolverButtonGroup.add(ceilingCheckBox);
		cspSolverButtonGroup.add(integerCheckBox);

		
		resultsCSPLabel = new JLabel("Results:");
		resultsCSPLabel.setBounds(37, 590, 54, 15);
		resultsCSPLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		contentPane.add(resultsCSPLabel);

		resultsCSPArea = new JTextArea();
		resultsCSPArea.setBounds(37, 610, 368, 150);
		resultsCSPArea.setEditable(false);
		resultsCSPArea.setOpaque(false);
		resultsCSPArea.setForeground(new Color(21, 114, 33));
		resultsCSPArea.setFont(new Font("Monaco", Font.BOLD, 10));
		resultsCSPArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		resultsCSPArea.setToolTipText("CSP results");

		jspCSPResult = new JScrollPane(resultsCSPArea);
		jspCSPResult.setBounds(37, 610, 368, 140);
		contentPane.add(jspCSPResult);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		String constraintsStr = constraintsArea.getText();
		String variablesStr = variablesArea.getText();
		String objectiveStr = objectiveField.getText();
		if (obj == btnSolve) {
			if (modelFilePath != null) {
				variablesArea.setEditable(true);
				constraintsArea.setEditable(true);
				objectiveField.setEditable(true);
				maxCheckBox.setEnabled(true);
				minCheckBox.setEnabled(true);
				byRowCheckBox.setEnabled(true);
				byColCheckBox.setEnabled(true);
				byRowCheckBox.setSelected(true);
				if (cplexCheckBox.isSelected() == true) {
					solveViaCplexByRow();
				} else if (gurobiCheckBox.isSelected() == true) {
					solveViaGurobiByRow();
				}

			} else {
				resultsArea.setText("");
				if (maxCheckBox.isSelected() == true) {
					if (constraintsStr.length() * variablesStr.length() * objectiveStr.length() != 0) {
						convert(variablesStr, objectiveStr, maxCheckBox.getText(), constraintsStr);
						if (cplexCheckBox.isSelected() == true) {
							if (byRowCheckBox.isSelected() == true) {
								solveViaCplexByRow();
							} else if (byColCheckBox.isSelected() == true) {
								solveViaCplexByCol();
							}
						} else if (gurobiCheckBox.isSelected() == true) {
							if (byRowCheckBox.isSelected() == true) {
								solveViaGurobiByRow();
							} else if (byColCheckBox.isSelected() == true) {
								solveViaGurobiByCol();
							}
						}

					} else {
						JOptionPane.showMessageDialog(null, "Please ensure the model is complete!", "Warning£¡",
								JOptionPane.ERROR_MESSAGE);
					}

				} else if (minCheckBox.isSelected() == true) {
					if (constraintsStr.length() * variablesStr.length() * objectiveStr.length() != 0) {
						convert(variablesStr, objectiveStr, minCheckBox.getText(), constraintsStr);
						if (cplexCheckBox.isSelected() == true) {
							if (byRowCheckBox.isSelected() == true) {
								solveViaCplexByRow();
							} else if (byColCheckBox.isSelected() == true) {
								solveViaCplexByCol();
							}
						} else if (gurobiCheckBox.isSelected() == true) {
							if (byRowCheckBox.isSelected() == true) {
								solveViaGurobiByRow();
							} else if (byColCheckBox.isSelected() == true) {
								solveViaGurobiByCol();
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Please ensure the model is complete!", "Warning£¡",
								JOptionPane.ERROR_MESSAGE);
					}

				} else {
					JOptionPane.showMessageDialog(null, "Please choose the problem type from max and min!", "Warning£¡",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		} else if (obj == btnReset) {
			variablesArea.setEditable(true);
			constraintsArea.setEditable(true);
			objectiveField.setEditable(true);
			maxCheckBox.setEnabled(true);
			minCheckBox.setEnabled(true);
			byRowCheckBox.setEnabled(true);
			byColCheckBox.setEnabled(true);
			constraintsArea.setText("");
			variablesArea.setText("");
			objectiveField.setText("");
			resultsArea.setText("");
			resultsCSPArea.setText("");
			minMaxButtonGroup.clearSelection();
			cspSolverButtonGroup.clearSelection();
			decimalCheckBox.setSelected(true);
			// solverButtonGroup.clearSelection();
			cplexCheckBox.setSelected(true);
			byRowCheckBox.setSelected(true);
		} else if (obj == importModelItem) {
			File file = null;
			fileChooser = new JFileChooser("D:\\");
			fileChooser.setDialogTitle("Open txt file");
			fileChooser.addChoosableFileFilter(new MyFileFilter("txt"));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				if (file != null) {
					readFile(file);
				}
			}
		} else if (obj == exportResultItem) {
			if (resultsArea.getText().length() != 0) {
				File file = null;
				fileChooser = new JFileChooser("D:\\");
				fileChooser.setDialogTitle("Save as txt file");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("ÎÄ±¾ÎÄµµ(*.txt)", ".txt");
				fileChooser.setFileFilter(filter);
				int result = fileChooser.showSaveDialog(this);
				if (result == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					if (file != null) {
						String fname = fileChooser.getName(file);
						if (fname.indexOf(".txt") == -1) {
							file = new File(fileChooser.getCurrentDirectory(), fname + ".txt");
						}
						writeFile(file);
						JOptionPane.showMessageDialog(null, "Result has been exported successfully!", "Attention!",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please ensure the model has been solved!", "Warning!",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (obj == loadConfigItem) {
			File file = null;
			fileChooser = new JFileChooser("D:\\");
			fileChooser.setDialogTitle("Open prm file");
			fileChooser.addChoosableFileFilter(new MyFileFilter("prm"));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				if (file != null) {
					configFilePath = file.getPath();
					JOptionPane.showMessageDialog(null, "Parameter file has been loaded successfully!", "Attention!",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} else if (obj == loadModelItem) {
			File file = null;
			fileChooser = new JFileChooser("D:\\");
			fileChooser.setDialogTitle("Open lp or mps file");
			fileChooser.addChoosableFileFilter(new MyFileFilter("lp"));
			fileChooser.addChoosableFileFilter(new MyFileFilter("mps"));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				if (file != null) {
					modelFilePath = file.getPath();
					variablesArea.setEditable(false);
					constraintsArea.setEditable(false);
					objectiveField.setEditable(false);
					maxCheckBox.setEnabled(false);
					minCheckBox.setEnabled(false);
					byRowCheckBox.setEnabled(false);
					byColCheckBox.setEnabled(false);
					constraintsArea.setText("");
					variablesArea.setText("");
					objectiveField.setText("");
					resultsArea.setText("");
					rowColButtonGroup.clearSelection();
					minMaxButtonGroup.clearSelection();
					JOptionPane.showMessageDialog(null, "The model file has been loaded successfully!", "Attention!",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} else if (obj == btnSolveCSP) {
			if (width != 0) {
				if (decimalCheckBox.isSelected()) {
					cspSolverType = 0;
				} else if (ceilingCheckBox.isSelected()) {
					cspSolverType = 1;
				} else if (integerCheckBox.isSelected()) {
					cspSolverType = 2;
				} 
				solveCSP();
			} else {
				JOptionPane.showMessageDialog(null, "Please load the CSP parameter file first!", "Warning!",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (obj == btnLoad) {
			File file = null;
			fileChooser = new JFileChooser("D:\\");
			fileChooser.setDialogTitle("Open txt file");
			fileChooser.addChoosableFileFilter(new MyFileFilter("txt"));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				if (file != null) {
					readFileForCSP(file);
					JOptionPane.showMessageDialog(null, "CSP parameter file has been loaded successfully!",
							"Attention!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} else if (obj == btnSave) {
			if (resultsCSPArea.getText().length() != 0) {
				File file = null;
				fileChooser = new JFileChooser("D:\\");
				fileChooser.setDialogTitle("Save as txt file");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("ÎÄ±¾ÎÄµµ(*.txt)", ".txt");
				fileChooser.setFileFilter(filter);
				int result = fileChooser.showSaveDialog(this);
				if (result == JFileChooser.APPROVE_OPTION) {
					file = fileChooser.getSelectedFile();
					if (file != null) {
						String fname = fileChooser.getName(file);
						if (fname.indexOf(".txt") == -1) {
							file = new File(fileChooser.getCurrentDirectory(), fname + ".txt");
						}
						writeFileForCSP(file);
						JOptionPane.showMessageDialog(null, "Result has been exported successfully!", "Attention!",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please ensure the CSP model has been solved!", "Warning!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// Convert the inputs
	private void convert(String variablesStr, String objectiveStr, String objType, String constraintsStr) {
		variables = new ArrayList<Variable>();
		objective = new Objective();
		constraints = new ArrayList<Constraint>();
		String[] splits;
		double[] coef = null;
		// Set variables
		for (String str : variablesStr.split("\\n")) {
			splits = str.trim().split("\\s+");
			Variable variale = new Variable();
			if (splits[0].equals("bool")) {
				variale.setType("bool");
				variale.setLb(0);
				variale.setUb(1);
				variables.add(variale);
			} else if (splits[0].equals("int")) {
				variale.setType("int");
				variale.setLb(stringToDouble(splits[1]));
				if (splits[2].equals("inf")) {
					variale.setUb(Integer.MAX_VALUE);
				} else {
					variale.setUb(stringToDouble(splits[2]));
				}
				variables.add(variale);
			} else if (splits[0].equals("num")) {
				variale.setType("num");
				variale.setLb(stringToDouble(splits[1]));
				if (splits[2].equals("inf")) {
					variale.setUb(Double.MAX_VALUE);
				} else {
					variale.setUb(stringToDouble(splits[2]));
				}
				variables.add(variale);
			}
		}
		// Set objective
		splits = objectiveStr.trim().split("\\s+");
		if (objType.equals("max"))
			objective.setType("max");
		else if (objType.equals("min"))
			objective.setType("min");
		coef = new double[splits.length];
		for (int i = 0; i < splits.length; i++) {
			coef[i] = stringToDouble(splits[i]);
		}
		objective.setCoef(coef);
		// Set constraints
		for (String str : constraintsStr.split("\\n")) {
			splits = str.trim().split("\\s+");
			Constraint constraint = new Constraint();
			coef = new double[variables.size()];
			for (int i = 0; i < variables.size(); i++) {
				coef[i] = stringToDouble(splits[i]);
			}
			constraint.setCoef(coef);
			if (splits[variables.size()].equals("<=")) {
				constraint.setRelation(Constants.relation_LE);
			} else if (splits[variables.size()].equals("=")) {
				constraint.setRelation(Constants.relation_EQ);
			} else if (splits[variables.size()].equals(">=")) {
				constraint.setRelation(Constants.relation_GE);
			}
			constraint.setRhs(stringToDouble(splits[variables.size() + 1]));
			constraints.add(constraint);
		}
	}

	// Solve model via Cplex by row
	private void solveViaCplexByRow() {
		algorithm = new CplexImplementation();
		optResult = algorithm.tryToSolve(variables, objective, constraints, modelFilePath, configFilePath,
				"Cplex(Row)");
		if (optResult != null) {
			if (optResult.isSolved()) {
				resultsArea.setText(optResult.showOptResults());
			} else {
				resultsArea.setText(optResult.showErrors());
			}
		} else {
			JOptionPane.showMessageDialog(null, "The model has been solved(details are displayed in console).",
					"Attention!", JOptionPane.INFORMATION_MESSAGE);
		}
		modelFilePath = null;
		configFilePath = null;
		variables = null;
		objective = null;
		constraints = null;
	}

	// Solve model via Cplex by column
	private void solveViaCplexByCol() {
		algorithm = new CplexImplementationByColumn();
		optResult = algorithm.tryToSolve(variables, objective, constraints, modelFilePath, configFilePath,
				"Cplex(Col)");
		if (optResult != null) {
			if (optResult.isSolved()) {
				resultsArea.setText(optResult.showOptResults());
			} else {
				resultsArea.setText(optResult.showErrors());
			}
		} else {
			JOptionPane.showMessageDialog(null, "The model has been solved(details are displayed in console).",
					"Attention!", JOptionPane.INFORMATION_MESSAGE);
		}
		modelFilePath = null;
		configFilePath = null;
		variables = null;
		objective = null;
		constraints = null;
	}

	// Solve model via Gurobi by row
	private void solveViaGurobiByRow() {
		algorithm = new GurobiImplementation();
		optResult = algorithm.tryToSolve(variables, objective, constraints, modelFilePath, configFilePath,
				"Gurobi(Row)");
		if (optResult != null) {
			if (optResult.isSolved()) {
				resultsArea.setText(optResult.showOptResults());
			} else {
				resultsArea.setText(optResult.showErrors());
			}
		} else {
			JOptionPane.showMessageDialog(null, "The model has been solved(details are displayed in console).",
					"Attention!", JOptionPane.INFORMATION_MESSAGE);
		}
		modelFilePath = null;
		configFilePath = null;
		variables = null;
		objective = null;
		constraints = null;
	}

	// Solve model via Gurobi by column
	private void solveViaGurobiByCol() {
		algorithm = new GurobiImplementationByColumn();
		optResult = algorithm.tryToSolve(variables, objective, constraints, modelFilePath, configFilePath,
				"Gurobi(Col)");
		if (optResult != null) {
			if (optResult.isSolved()) {
				resultsArea.setText(optResult.showOptResults());
			} else {
				resultsArea.setText(optResult.showErrors());
			}
		} else {
			JOptionPane.showMessageDialog(null, "The model has been solved(details are displayed in console).",
					"Attention!", JOptionPane.INFORMATION_MESSAGE);
		}
		modelFilePath = null;
		configFilePath = null;
		variables = null;
		objective = null;
		constraints = null;
	}

	// String to double
	private double stringToDouble(String s) {
		String regex = "-?\\d+/\\d+";
		if (s.matches(regex)) {
			String temp[] = s.split("/");
			return Double.parseDouble(temp[0]) / Double.parseDouble(temp[1]);
		} else {
			return Double.parseDouble(s);
		}

	}

	// Read file for LP/MIP
	private void readFile(File file) {
		FileReader fr = null;
		BufferedReader br = null;
		constraintsArea.setText("");
		variablesArea.setText("");
		objectiveField.setText("");
		resultsArea.setText("");
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String str;
			String[] splits;
			while (!(str = br.readLine()).equals("#")) {
				variablesArea.append(str + "\n");
			}
			str = br.readLine();
			splits = str.trim().split("\\s+");
			if (splits[0].equals(Constants.preference_Maximize)) {
				maxCheckBox.setSelected(true);
			} else {
				minCheckBox.setSelected(true);
			}
			for (int i = 1; i < splits.length; i++) {
				objectiveField.setText(objectiveField.getText() + splits[i] + " ");
			}
			while ((str = br.readLine()) != null) {
				constraintsArea.append(str + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// Write file for LP/MIP
	private void writeFile(File file) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		String result = resultsArea.getText();
		String[] splits = result.split("\n");
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for (int i = 1; i < splits.length; i++) {
				bw.write(splits[i]);
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Solve CSP
	private void solveCSP() {
		CSPsolver csPsolver = new CSPsolver(width, size, demand, cspSolverType);
		CSPResult cspResult = csPsolver.solveCSP();
		resultsCSPArea.setText(cspResult.showCSPResults());
	}

	// Read file for CSP
	private void readFileForCSP(File file) {
		FileReader fr = null;
		BufferedReader br = null;
		resultsCSPArea.setText("");
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String str;
			String[] splits;
			int count = 1;
			while ((str = br.readLine()) != null) {
				splits = str.trim().split("\\s+");
				if (count == 1) {
					width = Double.parseDouble(splits[0]);
				} else if (count == 2) {
					size = new double[1][splits.length];
					for (int i = 0; i < splits.length; i++) {
						size[0][i] = Double.parseDouble(splits[i]);
					}
				} else if (count == 3) {
					demand = new double[1][splits.length];
					for (int i = 0; i < splits.length; i++) {
						demand[0][i] = Double.parseDouble(splits[i]);
					}
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Write file for CSP
	private void writeFileForCSP(File file) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		String result = resultsCSPArea.getText();
		String[] splits = result.split("\n");
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for (int i = 0; i < splits.length; i++) {
				bw.write(splits[i]);
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Change background
	private void changeBg(int number) {
		switch (skinType) {
		case 0: {
			SubstanceImageWatermark watermark = new SubstanceImageWatermark(
					MainFrame.class.getResourceAsStream("/imgs/" + "bg" + number + ".png"));
			watermark.setKind(ImageWatermarkKind.APP_CENTER);
			SubstanceSkin skin = new DustSkin().withWatermark(watermark);
			SubstanceLookAndFeel.setSkin(skin);
		}
			break;
		case 1: {
			SubstanceImageWatermark watermark = new SubstanceImageWatermark(
					MainFrame.class.getResourceAsStream("/imgs/" + "bg" + number + ".png"));
			watermark.setKind(ImageWatermarkKind.APP_CENTER);
			SubstanceSkin skin = new OfficeBlue2007Skin().withWatermark(watermark);
			SubstanceLookAndFeel.setSkin(skin);
		}
			break;
		case 2: {
			SubstanceImageWatermark watermark = new SubstanceImageWatermark(
					MainFrame.class.getResourceAsStream("/imgs/" + "bg" + number + ".png"));
			watermark.setKind(ImageWatermarkKind.APP_CENTER);
			SubstanceSkin skin = new AutumnSkin().withWatermark(watermark);
			SubstanceLookAndFeel.setSkin(skin);
			break;
		}
		case 3: {
			SubstanceImageWatermark watermark = new SubstanceImageWatermark(
					MainFrame.class.getResourceAsStream("/imgs/" + "bg" + number + ".png"));
			watermark.setKind(ImageWatermarkKind.APP_CENTER);
			SubstanceSkin skin = new SaharaSkin().withWatermark(watermark);
			SubstanceLookAndFeel.setSkin(skin);
			break;
		}
		case 4: {
			SubstanceImageWatermark watermark = new SubstanceImageWatermark(
					MainFrame.class.getResourceAsStream("/imgs/" + "bg" + number + ".png"));
			watermark.setKind(ImageWatermarkKind.APP_CENTER);
			SubstanceSkin skin = new CremeCoffeeSkin().withWatermark(watermark);
			SubstanceLookAndFeel.setSkin(skin);
			break;
		}
		}
	}

	// Initialize file menu
	private void initializFileMenu(JMenu menu) {
		importModelItem = new JMenuItem("Import model");
		importModelItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		importModelItem.setForeground(Color.DARK_GRAY);
		importModelItem.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		importModelItem.addActionListener(this);
		fileButtonGroup.add(importModelItem);
		menu.add(importModelItem);

		exportResultItem = new JMenuItem("Export result");
		exportResultItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		exportResultItem.setForeground(Color.DARK_GRAY);
		exportResultItem.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		exportResultItem.addActionListener(this);
		fileButtonGroup.add(exportResultItem);
		menu.add(exportResultItem);

		loadModelItem = new JMenuItem("Load model");
		loadModelItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loadModelItem.setForeground(Color.DARK_GRAY);
		loadModelItem.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		loadModelItem.addActionListener(this);
		fileButtonGroup.add(loadModelItem);
		menu.add(loadModelItem);

		loadConfigItem = new JMenuItem("Load config");
		loadConfigItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loadConfigItem.setForeground(Color.DARK_GRAY);
		loadConfigItem.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		loadConfigItem.addActionListener(this);
		fileButtonGroup.add(loadConfigItem);
		menu.add(loadConfigItem);
	}

	// Initialize skin menu
	private void initializeSkinMenu(JMenu menu) {
		JRadioButtonMenuItem item1 = new JRadioButtonMenuItem("Coffee");
		item1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item1.setForeground(new Color(104, 138, 118));
		item1.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel(new SubstanceCremeCoffeeLookAndFeel());
					skinType = 4;
					changeBg(backgroundType);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});
		JRadioButtonMenuItem item2 = new JRadioButtonMenuItem("Sahara");
		item2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item2.setForeground(new Color(31, 165, 60));
		item2.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel());
					skinType = 3;
					changeBg(backgroundType);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});
		JRadioButtonMenuItem item3 = new JRadioButtonMenuItem("Autumn");
		item3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item3.setForeground(new Color(244, 189, 130));
		item3.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel(new SubstanceAutumnLookAndFeel());
					skinType = 2;
					changeBg(backgroundType);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});

		JRadioButtonMenuItem item4 = new JRadioButtonMenuItem("Blue");
		item4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item4.setForeground(new Color(0, 191, 255));
		item4.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel(new SubstanceOfficeBlue2007LookAndFeel());
					skinType = 1;
					changeBg(backgroundType);
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});
		JRadioButtonMenuItem item5 = new JRadioButtonMenuItem("Dust");
		item5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item5.setForeground(new Color(0, 0, 0));
		item5.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel(new SubstanceDustLookAndFeel());
					skinType = 0;
					changeBg(backgroundType);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});
		skinButtonGroup.add(item1);
		skinButtonGroup.add(item2);
		skinButtonGroup.add(item3);
		skinButtonGroup.add(item4);
		skinButtonGroup.add(item5);
		menu.add(item1);
		menu.add(item2);
		menu.add(item3);
		menu.add(item4);
		menu.add(item5);
	}

	// Initialize background menu
	private void initializeBgMenu(JMenu menu) {
		JRadioButtonMenuItem item5 = new JRadioButtonMenuItem("Slogan");
		item5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item5.setForeground(new Color(229, 165, 55));
		item5.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backgroundType = 0;
				changeBg(backgroundType);
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});

		JRadioButtonMenuItem item6 = new JRadioButtonMenuItem("Splash");
		item6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item6.setForeground(new Color(0, 210, 140));
		item6.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backgroundType = 1;
				changeBg(backgroundType);
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});

		JRadioButtonMenuItem item7 = new JRadioButtonMenuItem("Apple");
		item7.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item7.setForeground(new Color(65, 42, 113));
		item7.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backgroundType = 2;
				changeBg(backgroundType);
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});

		JRadioButtonMenuItem item8 = new JRadioButtonMenuItem("Highway");
		item8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item8.setForeground(new Color(1, 108, 194));
		item8.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backgroundType = 3;
				changeBg(backgroundType);
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});
		JRadioButtonMenuItem item9 = new JRadioButtonMenuItem("Dragon");
		item9.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		item9.setForeground(new Color(219, 13, 0));
		item9.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		item9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backgroundType = 4;
				changeBg(backgroundType);
				SwingUtilities.updateComponentTreeUI(MainFrame.this);
			}
		});
		bgButtonGroup.add(item5);
		bgButtonGroup.add(item6);
		bgButtonGroup.add(item7);
		bgButtonGroup.add(item8);
		bgButtonGroup.add(item9);
		menu.add(item5);
		menu.add(item6);
		menu.add(item7);
		menu.add(item8);
		menu.add(item9);
	}

	// Change icon
	public void setIcon() {
		setIconImage(new ImageIcon(MainFrame.class.getResource("/imgs/ico.png")).getImage());
	}
}

// MyFileFilter
class MyFileFilter extends FileFilter {
	String type;

	public MyFileFilter(String type) {
		this.type = type;
	}

	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String fileName = f.getName();
		int index = fileName.lastIndexOf('.');
		// If the file extension name matches the input type then display it
		if (index > 0 && index < fileName.length() - 1) {
			String extension = fileName.substring(index + 1).toLowerCase();
			if (extension.equals(type))
				return true;
		}
		return false;
	}

	public String getDescription() {
		if (type.equals("txt"))
			return "ÎÄ±¾ÎÄµµ(*.txt)";
		if (type.equals("dat"))
			return "Êý¾ÝÎÄ¼þ(*.dat)";
		if (type.equals("prm"))
			return "Ä£ÐÍ²ÎÊýÎÄ¼þ(*.prm)";
		if (type.equals("lp"))
			return "lpÄ£ÐÍÎÄ¼þ(*.lp)";
		if (type.equals("mps"))
			return "mpsÄ£ÐÍÎÄ¼þ(*.mps)";
		return "";
	}
}
