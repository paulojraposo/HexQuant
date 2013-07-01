
/*
 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 * 
 * This class will be the main 'program'; from here all other classes
 * will be called to implement the software, and the GUI is generated from
 * this code.  Running the whole software package will be a matter of 
 * running this class. 
 * 
 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 * 
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

//import sun.security.timestamp.Timestamper;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.*;

public class ThesisSoft implements ActionListener {
	
	/*
	 * Declarations
	 */
	
	// input and output files
	
	File input;	
	File hexOutput;
	File sqOutput;
	
	// interface components
	
	JPanel mainPanel;		// the main panel, others to go inside.
	JPanel filesButtonPanel;
	JPanel inputPathPanel;
	JPanel hexOutputPathPanel;
	JPanel sqOutputPathPanel;
	JPanel routinesPanel;
	JPanel vertexClusterOptionsPanel;
	JPanel vertexClusterOptionsPanel2;
	JPanel parametersPanel;
	JPanel parametersPanel2;
	JPanel buttonsPanel;
	//JPanel progressPanel;
	JPanel indicatorPanel;
	
	JFileChooser open_FC;
	JFileChooser saveHex_FC;
	JFileChooser saveSq_FC;
	JFileChooser saveTxt_FC;
	
	ButtonGroup vertexClusterOptions_BG;
	
	JRadioButton spatialMean_RB;
	JRadioButton firstAndLastMidpoint_RB;
	JRadioButton vertexNearestMean_RB;
	JRadioButton firstVertex_RB;
	JRadioButton lastVertex_RB;
	JRadioButton vertexFurthestCentroid_RB;
	
	JButton loadCSV;
	JButton saveHexCSV;
	JButton saveSqCSV;
	JButton runThesisSoft;
	//JButton resetThesisSoft;
	JButton writeTxt_B;	
	
	JCheckBox hex_CB;
	JCheckBox sq_CB;
	JCheckBox hausdorff_CB;
	
	JLabel inputFilePath_L;
	JLabel hexOutputFilePath_L;
	JLabel sqOutputFilePath_L;
	JLabel routinesPrompt_L;
	JLabel vertexClusteringPrompt_L;
	JLabel inputScale_L;
	JLabel targetScale_L;
	JLabel lineWeight_L;
	JLabel tesseraWidth_L;
	//static JLabel progress_L;
	
	JTextField inputScale_TF;
	JTextField targetScale_TF;
	JTextField lineWeight_TF;
	JTextField tesseraWidth_TF;
	
	static JTextArea indicator_TA;		// Made this static to get messages from other classes to it.
										// Given receiveMessage(), the static quality may no longer be necessary... should check.
	
	JScrollPane indicator_SP;
	
	// variables, for passing to algorithm classes when user has set parameters and hits run
	
	// String 	inputFilePath;
	String 	hexOutputFilePath;
	String 	sqOutputFilePath;
	boolean doHausdorff;
	int 	vertexClusteringOption;
	Double	tesseraWidth;
	ArrayList<Point> inputVertices;	// the ArrayList where input data is destined.  This gets passed to algorithm classes later.

	boolean tesseraWidthOverriden = false;	// a way of keeping track whether the tessera width is an override or not.
	
	// ArrayLists, for use in reading input file
	private static ArrayList<String> eastingsStrings = new ArrayList(0);
	private static ArrayList<String> northingsStrings = new ArrayList(0);
	
		
	/*
	 * Constructor
	 */
	
	public ThesisSoft(){	
		
		// creating interface panels and populating them with instances of interface components
		
		filesButtonPanel = new JPanel();
		//filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.Y_AXIS));
		loadCSV = new JButton("Select input CSV");
		loadCSV.addActionListener(this);
		saveHexCSV = new JButton("Specify Hexagonal CSV to save");
		saveHexCSV.addActionListener(this);
		saveSqCSV = new JButton("Specify Square CSV to save");
		saveSqCSV.addActionListener(this);
		filesButtonPanel.add(loadCSV);
		filesButtonPanel.add(saveHexCSV);
		filesButtonPanel.add(saveSqCSV);
		
		inputPathPanel = new JPanel();
		inputFilePath_L = new JLabel(". . . input file . . .");
		inputPathPanel.add(inputFilePath_L);
		
		hexOutputPathPanel = new JPanel();
		hexOutputFilePath_L = new JLabel(". . . hexagonal output file . . .");
		hexOutputPathPanel.add(hexOutputFilePath_L);
		
		sqOutputPathPanel = new JPanel();
		sqOutputFilePath_L = new JLabel(". . . square output file . . .");
		sqOutputPathPanel.add(sqOutputFilePath_L);
		
		routinesPanel = new JPanel();
		routinesPrompt_L = new JLabel("Routines to run:");
		hex_CB = new JCheckBox("Hexagons");								// three check boxes, starting disabled and "off",
		hex_CB.setEnabled(false);										// to be enabled and toggled "on" when output files are
		sq_CB = new JCheckBox("Squares");								// successfully specified
		sq_CB.setEnabled(false);										//
		hausdorff_CB = new JCheckBox("Calculate Hausdorff distances");	//
		hausdorff_CB.setEnabled(false);									//
		routinesPanel.add(routinesPrompt_L);
		routinesPanel.add(hex_CB);
		routinesPanel.add(sq_CB);
		routinesPanel.add(hausdorff_CB);
		
		vertexClusterOptionsPanel = new JPanel();
		vertexClusteringPrompt_L = new JLabel("Vertex clustering:");
		spatialMean_RB = new JRadioButton("spatial mean", true);
		vertexNearestMean_RB = new JRadioButton("vertex nearest spatial mean");
		firstAndLastMidpoint_RB = new JRadioButton("midpoint, 1st & last points");
		vertexClusterOptions_BG = new ButtonGroup();
		vertexClusterOptions_BG.add(spatialMean_RB);
		vertexClusterOptions_BG.add(firstAndLastMidpoint_RB);
		vertexClusterOptions_BG.add(vertexNearestMean_RB);
		vertexClusterOptionsPanel.add(vertexClusteringPrompt_L);
		vertexClusterOptionsPanel.add(spatialMean_RB);
		vertexClusterOptionsPanel.add(vertexNearestMean_RB);
		vertexClusterOptionsPanel.add(firstAndLastMidpoint_RB);
		
		vertexClusterOptionsPanel2 = new JPanel();
		firstVertex_RB = new JRadioButton("first vertex");
		lastVertex_RB = new JRadioButton("last vertex");
		vertexFurthestCentroid_RB = new JRadioButton("vertex furthest centroid");
		vertexClusterOptions_BG.add(firstVertex_RB);
		vertexClusterOptions_BG.add(lastVertex_RB);
		vertexClusterOptions_BG.add(vertexFurthestCentroid_RB);
		vertexClusterOptionsPanel2.add(firstVertex_RB);
		vertexClusterOptionsPanel2.add(lastVertex_RB);
		vertexClusterOptionsPanel2.add(vertexFurthestCentroid_RB);
		
		
		parametersPanel = new JPanel();
		
		inputScale_L = new JLabel("Input Scale:");
		inputScale_TF = new JTextField(10);
		inputScale_TF.setText("24000");
		//inputScale_TF.setActionCommand("inputScaleEntered");		// doesn't need to trigger an
		//inputScale_TF.addActionListener(this);					// action, just for printing to report text files
		targetScale_L = new JLabel("Target Scale:");
		targetScale_TF = new JTextField(10);
		targetScale_TF.setActionCommand("calcTesseraWidth"); 		//set actions for targetScale_TF
		targetScale_TF.addActionListener(this);
		parametersPanel.add(inputScale_L);
		parametersPanel.add(inputScale_TF);
		parametersPanel.add(targetScale_L);
		parametersPanel.add(targetScale_TF);
		
		parametersPanel2 = new JPanel();
		lineWeight_L = new JLabel("Line Weight (mm):");		 		// gets converted mm -> m in calculateTesseraWidth() below
		lineWeight_TF = new JTextField(7);
		lineWeight_TF.setText("0.25");
		lineWeight_TF.setActionCommand("calcTesseraWidth");	 		//set actions for lineWeight_TF
		lineWeight_TF.addActionListener(this);
		tesseraWidth_L = new JLabel("Tessera Width (m):");
		tesseraWidth_TF = new JTextField(10);
		tesseraWidth_TF.setActionCommand("overrideTesseraWidth");
		tesseraWidth_TF.addActionListener(this);			 		//set actions for targetScale_TF
		parametersPanel2.add(lineWeight_L);
		parametersPanel2.add(lineWeight_TF);
		parametersPanel2.add(tesseraWidth_L);
		parametersPanel2.add(tesseraWidth_TF);
		
		buttonsPanel = new JPanel();
		runThesisSoft = new JButton("Run");
		runThesisSoft.addActionListener(this);
		//resetThesisSoft = new JButton("Reset All");
		//resetThesisSoft.addActionListener(this);
		writeTxt_B = new JButton("Notes to text");
		writeTxt_B.addActionListener(this);
		buttonsPanel.add(runThesisSoft);
		//buttonsPanel.add(resetThesisSoft);
		buttonsPanel.add(writeTxt_B);
		
//		progressPanel = new JPanel();								// won't function as I'd like without good threading
//		progress_L = new JLabel("{~!~}");
//		progressPanel.add(progress_L);
		
		indicatorPanel = new JPanel();
		indicator_TA = new JTextArea(13, 42);
		indicator_TA.setFont(new Font("Serif", Font.ITALIC, 16));
		indicator_TA.setLineWrap(true);
		indicator_TA.setWrapStyleWord(true);
		indicator_TA.setEditable(false);
		indicator_TA.setBorder(BorderFactory.createEtchedBorder());
		indicator_SP = new JScrollPane(indicator_TA);
		indicator_SP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		indicatorPanel.add(indicator_SP);
		
		//Create the mainPanel and add the other panels to the mainPanel
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(720,690));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(filesButtonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(inputPathPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(hexOutputPathPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(sqOutputPathPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(routinesPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(vertexClusterOptionsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(vertexClusterOptionsPanel2);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(parametersPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(parametersPanel2);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
//      mainPanel.add(progressPanel);
//      mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(indicatorPanel);
        mainPanel.add(Box.createGlue());
  
	}

	private static void createAndShowGUI(){
		// create and set up the window.
		JFrame frame = new JFrame("ThesisSoft v68");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create and set up the content pane.
		ThesisSoft aThesisSoft = new ThesisSoft();
		aThesisSoft.mainPanel.setOpaque(true);
		frame.setContentPane(aThesisSoft.mainPanel);
		
		//display the window
		frame.pack();
		frame.setVisible(true);
		
	}
	
	private double calculateTesseraWidth(String lWeight, String tScale){
		// This method calculates tessera width with respect to line weight and target scale.
		// Reflects rule of thumb sampling @ 1/5 size of object for detection (Tobler 1987)...
		double aTesseraWidth = 0.0;
		
		Double aLineWeight = 0.0;
		aLineWeight = aLineWeight.valueOf(lWeight) * 0.001;  // read String to Double, multiply by .001 for mm --> m
		Double aTargetScale = 0.0;
		aTargetScale = aTargetScale.valueOf(tScale);		 // read String to Double
		
		aTesseraWidth = (aLineWeight * aTargetScale) * 5.0;  
		
		tesseraWidth = 0.0;									 // initialize
		tesseraWidth = aTesseraWidth;						 // set to value calculated here
		return tesseraWidth;								 // return for sake of the ActionCommand "calcTesseraWidth"
	}

	
	public void actionPerformed(ActionEvent e){
		
		if(e.getSource() == this.loadCSV){
			// identifies the input file, for later use in directing FileReader (in this class, not written yet) on pressing Run button
			open_FC = new JFileChooser("C:\\Courses\\Thesis\\ThesisData\\SampleLines2csv");		
			FileNameExtensionFilter inFilter = new FileNameExtensionFilter("csv files", "csv");		// want only csv files
			open_FC.setFileFilter(inFilter);
			int returnVal = open_FC.showOpenDialog(this.mainPanel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            input = open_FC.getSelectedFile();
				//indicator_TA.append("Specified an input csv file\n");
				inputFilePath_L.setText("Input File:   " + input.getAbsolutePath());
			} else {
				indicator_TA.append("Opening input file command cancelled by user\n");
	        }
		}
		
		if(e.getSource() == this.saveHexCSV){
			// identifies the hexOutput file, for later passing to HexagonAlgorithm class
			saveHex_FC = new JFileChooser("C:\\Courses\\Thesis\\ThesisData\\SampleLines2csvSimplified");
			FileNameExtensionFilter hexOutFilter = new FileNameExtensionFilter("csv files", "csv");
			saveHex_FC.setFileFilter(hexOutFilter);
			int returnVal = saveHex_FC.showSaveDialog(this.mainPanel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				hexOutput = saveHex_FC.getSelectedFile();
				hexOutputFilePath_L.setText("Hexagon output file:   " + hexOutput.getAbsolutePath());
				//indicator_TA.append("Specified hexagon output path. IMPORTANT: wrote csv extension?\n");
				hex_CB.setEnabled(true);		// enable checkbox and select make it toggled "on"
				hex_CB.setSelected(true);
				hausdorff_CB.setEnabled(true);
				hausdorff_CB.setSelected(true);
	        } else {
	            indicator_TA.append("Save hexagon ouput file command canceled by user.\n");
	        }
		}
		
		if(e.getSource() == this.saveSqCSV){
			// identifies the sqOutput file, for later passing to SquareAlgorithm class
			saveSq_FC = new JFileChooser("C:\\Courses\\Thesis\\ThesisData\\SampleLines2csvSimplified");
			FileNameExtensionFilter sqOutFilter = new FileNameExtensionFilter("csv files", "csv");
			saveSq_FC.setFileFilter(sqOutFilter);
			int returnVal = saveSq_FC.showSaveDialog(this.mainPanel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				sqOutput = saveSq_FC.getSelectedFile();
				sqOutputFilePath_L.setText("Square ouput file:   " + sqOutput.getAbsolutePath());
				//indicator_TA.append("Specified square output path. IMPORTANT: wrote csv extension?\n");
				sq_CB.setEnabled(true);		// enable checkbox and select make it toggled "on"
				sq_CB.setSelected(true);
				hausdorff_CB.setEnabled(true);
				hausdorff_CB.setSelected(true);
	       } else {
	        	indicator_TA.append("Save square output file command canceled by user.\n");
	       }
		}
		
		// on pressing enter on either the line width or target scale text fields:
		if(e.getActionCommand().equals("calcTesseraWidth")){
			Double tWidth = calculateTesseraWidth(lineWeight_TF.getText(), targetScale_TF.getText());
			tesseraWidth_TF.setText(tWidth.toString());
			//indicator_TA.append("Tessera width calculated\n");
			scrollIndicator_TA();
			tesseraWidthOverriden = false;

		}
		// on pressing enter on tessera override text field:
		if(e.getActionCommand().equals("overrideTesseraWidth")){
			tesseraWidth = tesseraWidth.valueOf(tesseraWidth_TF.getText());		//update tesseraWidth
			indicator_TA.append("Tessera width override: " + tesseraWidth + " m\n");
			scrollIndicator_TA();
			tesseraWidthOverriden = true;
		}
		
		if(e.getSource() == this.runThesisSoft){
			// first calling this function to make sure all is in order for running algorithms;
			// chechParametersBeforeRun() will then call runThesisSoft() if all's in order.
			checkParametersBeforeRun();
			
		}
		
		if(e.getSource() == writeTxt_B){
			writeIndicator_TAToText();
		}
	}
	
	
	public static void receiveMessage(String aMessage){
		// a method to receive messages from other classes in the software and post them to indicator_TA.
		// Made indicator_TA static to get messages from other classes - working thus far.
		indicator_TA.append(aMessage);
		scrollIndicator_TA();
	}
	
	
	
	private void checkParametersBeforeRun(){
		// this method will check that all parameters are satisfactorily set before calling the
		// runThesisSoft() method, which gets the show on the road.
		
		if(	(input != null)																				&&		// Don't run unless one or both of 
			((hex_CB.isSelected() && hexOutput != null) || (sq_CB.isSelected() && sqOutput != null) ) 	&&		// hex and sq routines are checked WITH output
			(tesseraWidth != null)																		&&		// files specified, AND tesseraWidth is neither
			(tesseraWidth != 0.0)){																				// null NOR zero.
			// Also implemented it such that routine buttons toggle to enabled and "on" only after output files have been successfully 
			// chosen.  Not water-tight error checking, mainly because there's not good file-type checking for specifying output files,
			// but okay for now.
			
			runThesisSoft();
			
		}else{
			indicator_TA.append("Not all necessary parameters set, or tessera width is zero!\n");
		}
		
	}
	
	private void runThesisSoft(){
		/*
		 *  This method will first use the GUI components to define variables for 
		 *  passing to the algorithm classes. It then passes them.
		 *  
		 */
		indicator_TA.append("\n::: Starting Program :::::::::::::::::::::::::::::::::::::::::::::\n");
		//TODO: get a timestamp in this output somehow, beginning and end.
		
		// Instantiate and define variables
		
		// the strings for file paths out output, only if the appropriate boxes are checked
		if(hex_CB.isSelected() && hexOutput != null){
			
			hexOutputFilePath = hexOutput.getAbsolutePath();
			//indicator_TA.append("Got a hex output file\n");
		}
		if(sq_CB.isSelected() && sqOutput !=null ){
			
			sqOutputFilePath = sqOutput.getAbsolutePath();
			//indicator_TA.append("Got a sq output file\n");
		}
					
		// Hausdorff calculation: whether or not to do it.
		doHausdorff = hausdorff_CB.isSelected();			// equated to state of the button
		
		// vertex clustering method, from the mutually-exclusive radio buttons
		// 0 for spatial mean
		// 1 for vertex nearest mean
		// 2 for midpoint between first and last points of length of line
		String vCollapseMethod = new String();
		if(spatialMean_RB.isSelected()){
			vertexClusteringOption = 0;
			vCollapseMethod = "spatial mean";
		}
		if(vertexNearestMean_RB.isSelected()){
			vertexClusteringOption = 1;
			vCollapseMethod = "vertex nearest mean";
		}
		if(firstAndLastMidpoint_RB.isSelected()){
			vertexClusteringOption = 2;
			vCollapseMethod = "midpoint 1st & last";
		}
		if(firstVertex_RB.isSelected()){
			vertexClusteringOption = 3;
			vCollapseMethod = "first vertex";
		}
		if(lastVertex_RB.isSelected()){
			vertexClusteringOption = 4;
			vCollapseMethod = "last vertex";
		}
		if(vertexFurthestCentroid_RB.isSelected()){
			vertexClusteringOption = 5;
			vCollapseMethod = "vertex furthest centroid";
		}
		
			
		// tesseraWidth has been set by calculateTesseraWidth() by this point.
		
		
		// Read input lines and write them to vertices.  Do this in readInput(), 
		// with the call here, so that it happens at at user's pressing of "Run" button.
		inputVertices = new ArrayList(0);		// instantiate the inputVertices ArrayList
		indicator_TA.append("Reading input file:\n" + "     " + input + "\n\n");
		readInput();							// call readInput(), which will add points to ArrayList with each iteration
		//indicator_TA.append(" Done.\n");
		
		
		// print important parameters, for benefit of later text file reports
		indicator_TA.append("Input scale:                      " + inputScale_TF.getText() + "\n");
		indicator_TA.append("Target scale:                     " + targetScale_TF.getText() + "\n");
		indicator_TA.append("Tessera width:                    " + tesseraWidth + " m");
		if(tesseraWidthOverriden == true){
			indicator_TA.append(" (override)");
		}
		indicator_TA.append("\n");
		indicator_TA.append("Vertex collapse method:           " + vCollapseMethod + "\n");
		indicator_TA.append("Calculating Hausdorff distances:  " + doHausdorff + "\n");
		
		// Making appropriate algorithm class calls, and passing variables to them
		if(hex_CB.isSelected()){
			indicator_TA.append("\n...HEXAGONS.......................................\n");
			// Calling the HexagonAlgorithm class's constructor, and passing all relevant variables and inputVertices
			
			// Send message to indicator_TA with input and output file names being used for hexagons
			//indicator_TA.append("using input file:\n " + "     " + input.getName() + "\n");
			indicator_TA.append("using output file:\n " + "     " + hexOutput + "\n");
			
			// create instance of HexagonAlgorithm and pass variables to constructor
			HexagonAlgorithm aHexagonAlgorithm = new HexagonAlgorithm(	hexOutputFilePath,
																		doHausdorff,
																		vertexClusteringOption,
																		tesseraWidth,
																		inputVertices);
			
			indicator_TA.append("... Hexagons done! ..................................\n\n");
		}
		
		if(sq_CB.isSelected()){
			indicator_TA.append("\n...SQUARES.......................................\n");
			// Calling the SquareAlgorithm class's constructor, and passing all relevant variables and inputVertices
			
			// Send message to indicator_TA with input and output file names being used for squares
			//indicator_TA.append("using input file:\n " + "     " + input.getName() + "\n");
			indicator_TA.append("using output file:\n " + "     " + sqOutput + "\n");
			
			// create instance of SquareAlgorithm and pass variables to constructor
			SquareAlgorithm aSquareAlgorithm = new SquareAlgorithm(		sqOutputFilePath,
																		doHausdorff,
																		vertexClusteringOption,
																		tesseraWidth,
																		inputVertices);
			
			indicator_TA.append("... Squares done! ................................\n\n");
		}
		
		indicator_TA.append("\n::: Ending Program :::::::::::::::::::::::::::::::::::::::::::::::\n\n\n\n\n");
	}
		
	private void readInput(){
		
		try {

			// accept file path identified by File input
			FileReader input_FR = new FileReader(input.getAbsolutePath());
		
			BufferedReader bufRead = new BufferedReader(input_FR);
			
			// Read first line to a string; not retaining this first line, since it is spreadsheet headers
			String aLine = bufRead.readLine();
			
			// read first meaningful line
			aLine = bufRead.readLine();
	
			// Read through the file one line at time; null means end of file  
			// int count used as loop index
			int count = 0;
			
			// calculate the correct index position in the split strings (see below) when
			// x and y coordinates are given as the second-last and last columns, respectively, in the csv
			int eastingsIndex = aLine.split(",").length - 2;
			int northingsIndex = aLine.split(",").length -1;
			
			while (aLine != null) {
								
				eastingsStrings.add(aLine.split(",")[eastingsIndex]);
				northingsStrings.add(aLine.split(",")[northingsIndex]);
				
				// write each pair of eastings and northings into an array of type Point
				// convert values to double first
				Double e = 0.0;
				Double n = 0.0;
				
				e = e.valueOf(eastingsStrings.get(count));
				n = n.valueOf(northingsStrings.get(count));
				
				Point aPoint = new Point(e,n);
				
				inputVertices.add(aPoint);
				
				// should format this output so that it puts all the decimal zeroes in...
				
				count++;
				
				aLine = bufRead.readLine();
				
			}
			
			// Close the buffer and the file.
			bufRead.close();
			input_FR.close();
			
		} 
		
		// Specify what to do if things go wrong trying to read the input file.
		catch (IOException e) {	
			indicator_TA.append("Couldn't find the input file at that path...");
			e.printStackTrace();
		}
	
	}
	
	
	private static void scrollIndicator_TA(){
		
		indicator_TA.selectAll();		
	}
	
	private void writeIndicator_TAToText(){
		// Handles taking everything in indicator_TA at the time of call and saving to a text file
		saveTxt_FC = new JFileChooser("C:\\Courses\\Thesis\\ThesisData\\SampleLines2csvSimplified");
		FileNameExtensionFilter txtOutFilter = new FileNameExtensionFilter("txt files", "txt");
		saveTxt_FC.setFileFilter(txtOutFilter);
		
		int returnVal = saveTxt_FC.showSaveDialog(this.mainPanel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// still doesn't add file extensions on it's own, so need to say .txt manually... argh.
			// JFileChooser seems to also allow you to replace a file without a built-in warning too.
			File daReportFile = saveTxt_FC.getSelectedFile();
			Writer myTextWriter = null;
			
			try {
				myTextWriter = new BufferedWriter(new FileWriter(daReportFile));
				indicator_TA.write(myTextWriter);
				myTextWriter.close();
				indicator_TA.append("\nReport text file saved!\n");
			} catch (IOException except) {	
				indicator_TA.append("Create report txt file failed.");
				//except.printStackTrace();
			}
			
       } else {
    	   // indicator_TA.append("Save report text file command canceled by user.\n");
       }
		
	}
	
	public static void main(String[] args) {
		// schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

}
