
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

public class HexagonAlgorithm {
	
	/*
	 * Declarations
	 */
	
	ArrayList<Point> inputVertices;
	
	/*
	 * Constructor - the only method in the class
	 */
	
	public HexagonAlgorithm(	String anOutputPath, 
								boolean willDoHausdorff, 
								int vClusterOption, 
								Double aTesseraWidth,
								ArrayList<Point> theInputVertices	){
				
		this.inputVertices = new ArrayList(0);	// instantiate
		
		// Loop through the ArrayList of points passed from ThesisSoft and write 
		// to local ArrayList inputVertices.
		for(int i=0; i < theInputVertices.size(); i++){
			this.inputVertices.add(theInputVertices.get(i));
		}
		
		
		// Calculating upper left and bottom right points for creation of bounding box as per Sasha's method. 				
		 
		// instantiate these values to the first point in the inputVertices array - will get changed as necessary in following loop
		Double xUpperLeft = this.inputVertices.get(0).getX();
		Double yUpperLeft = this.inputVertices.get(0).getY();
		Double xBottomRight = this.inputVertices.get(0).getX();
		Double yBottomRight = this.inputVertices.get(0).getY();
		
		// loop through all the remaining points in inputVertices to find infinum and extremum values for x and y
		for (int i=1; i < this.inputVertices.size(); i++){
			
			xUpperLeft = Math.min(this.inputVertices.get(i).getX(), xUpperLeft);
			yUpperLeft = Math.max(this.inputVertices.get(i).getY(), yUpperLeft);
			xBottomRight = Math.max(this.inputVertices.get(i).getX(), xBottomRight);
			yBottomRight = Math.min(this.inputVertices.get(i).getY(), yBottomRight);
			
		}
		
		
		// create two new points for passing to code, which will use them to create 
		// a bounding box and then the hexagonal grid.
		Point upperLeft = new Point(xUpperLeft, yUpperLeft);
		Point bottomRight = new Point(xBottomRight, yBottomRight);
	
		// Create an instance of HexGrid;
		// pass to its constructor the values in the line below:
		//ThesisSoft.receiveProgressMessage("Constructing hexagonal grid...");
		HexGrid aHexGrid = new HexGrid(upperLeft, bottomRight, aTesseraWidth);		// using aTesseraWidth, as passed to the class
		
		// Instantiate an array list of polygons, and call function as per below, 
		// which will return an ArrayList
		ArrayList<Polygon> hexagonArray		= aHexGrid.getPolygonList();
		//Point[] centroidList				= aHexGrid.getCentroidList();
				
		/*
		 *  :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		 *  Calculating point and hexagon intersections
		 * 
		 * 
		 *  Making each point check for intersection with each polygon
		 *  (converted hexagons) by two nested for loops.  
		 *  A currentHexagon loop variable is used; checking ahead to see if 
		 *  next point is in the same hexagon.  While currentHexagon is 
		 *  the same, write each point to same SpatialMean instance.  When 
		 *  it is not, move to a new SpatialMean instance.
		 * 
		 * 
		 */
		
		
		/// declare currentHexagon variable, leave blank to start
		Polygon currentHexagon = null;
		
		
		// declare variable for currentSpatial Mean, leave blank to start
		// The following code calls for various kinds of collapses, depending on vClusterOption; 
		// this allows for different possible collapse methods.
		
		
		// instantiate a currentCollapse, and an ArrayList<CollapseMethod>
		CollapseMethod currentCollapse = null;
		ArrayList<CollapseMethod> arrayOfCollapses = new ArrayList<CollapseMethod>(0);
		
		// Tested knocking out these here.  Introduction later in loop works sufficiently alone.
//		if(vClusterOption == 0){
//			currentCollapse = new SpatialMean();
//		}
//		if(vClusterOption == 1){
//			currentCollapse = new VertexNearestMean();
//		}
//		if(vClusterOption == 2){
//			currentCollapse = new MidpointFirstAndLast();
//		}
		
		
		for (int i=0; i<this.inputVertices.size(); i++){
			
			for(int j=0; j < hexagonArray.size(); j++){
				
				Polygon processingHexagon = hexagonArray.get(j);
				
				boolean intersection = processingHexagon.isWithinPolygon(this.inputVertices.get(i));
				
//				// ::::
//				if ((i % 1000) == 0) {
//					ThesisSoft.receiveProgressMessage("Hx: Processing vertex " + i);
//				}
//				// ::::
				
				if (intersection){
						
					// check to see if currentHexagon is the same as last one
					if(processingHexagon == currentHexagon){
						
						// if so, add this point to previous collapse
						currentCollapse.addPoint(this.inputVertices.get(i));
					
					} else {
						
						// if not, define currentHexagon as processingHexagon, 
						// create a new CollapseMethod, add a point to it, 
						// store it to arrayOfCollapses, 
						// and set this collapse as the currentCollapse.
						// First, some code to define which type of collapse to use:
						if(vClusterOption == 0){
							currentCollapse = new SpatialMean();
						}
						if(vClusterOption == 1){
							currentCollapse = new VertexNearestMean();
						}
						if(vClusterOption == 2){
							currentCollapse = new MidpointFirstAndLast();
						}
						if(vClusterOption == 3){
							currentCollapse = new FirstVertex();
						}
						if(vClusterOption == 4){
							currentCollapse = new LastVertex();
						}
						if(vClusterOption == 5){
							currentCollapse = new VertexFurthestCentroid(processingHexagon);
						}

//						// First, add this point to currentCollapse (i.e., the previous, old one)
//						currentCollapse.addPoint(this.inputVertices.get(i));
//						// Then, make a new one, passing constructor the point before current one
//						currentCollapse = new LiOpenshawMidpoint(processingHexagon, this.inputVertices.get(i-1));
//						}
						
						
						currentCollapse.addPoint(this.inputVertices.get(i));
						
						arrayOfCollapses.add(currentCollapse);
						
						currentHexagon = processingHexagon;	
					}					
				}		
			}
		}
		
		
		// Instantiate an array of output vertices.
		// Calculate collapses for each pass of the line in
		// each tessera, and write each resulting Point array of outputVertices.
		ArrayList<Point> outputVertices = new ArrayList(0);
		
		
		// Add first input Point as first output Point
		outputVertices.add(inputVertices.get(0));
		// loop through collapsed points and add each of them
		for(int i=0; i<arrayOfCollapses.size(); i++){
			outputVertices.add(arrayOfCollapses.get(i).collapse());
		}
		// Add last input Point as last output Point
		outputVertices.add(inputVertices.get(inputVertices.size()-1));
		
		/*
		 * By this point, output vertices exist as the ArrayList outputVertices.
		 * Can write ThesisSoft class to recieve this ArrayList and pass it to new "Resequence" class,
		 * or can send to Resequence from here in HexagonAlgorithm, the way the H dist is calculated now.  Latter involves
		 * sending HexagonAlgorithm another argument on instantiation, regarding whether or not to perform 
		 * a Resequence.
		 * 
		 */
		
		//  publish output to a csv file
		FileWriter outFile = null;
		PrintWriter out = null;
		
		try {
			outFile = new FileWriter(anOutputPath);		// updated to take the file path passed to the class
			out = new PrintWriter(outFile);
		} catch (IOException e) {	
			ThesisSoft.receiveMessage("Couldn't create/open the output file at that path.\n");
			e.printStackTrace();
		}
		
		// writing to output file
		//ThesisSoft.receiveMessage("Writing to output file...");
		// csv header first
		out.println("Index,X,Y");
		
		for(int i=0; i<outputVertices.size(); i++){
//			// ::::
//			if ((i % 1000) == 0) {
//				ThesisSoft.receiveProgressMessage("Hx: Writing vertex " + i);
//			}
//			// ::::
			out.format("%d,%.6f,%.6f\r\n", i, outputVertices.get(i).getX(), outputVertices.get(i).getY());
		}
		
		
		out.flush();
		out.close();
		
		//ThesisSoft.receiveMessage(" Done!\n");
		
		// Output to GUI #'s of input and output vertices, and what % reduction
		// they represent.
		double percentOfInput = ((outputVertices.size()) * 100.0) / inputVertices.size();		
		double percentDecrease = 100.0 - percentOfInput;
		ThesisSoft.receiveMessage("\nInput vertices:  " + inputVertices.size() + "\n");
		ThesisSoft.receiveMessage("Output vertices: " + (outputVertices.size()) + "\n");
		ThesisSoft.receiveMessage(" Output vertices are " + roundThreeDecimals(percentOfInput) + "% of input.\n");
		ThesisSoft.receiveMessage(" (" + roundThreeDecimals(percentDecrease) + "% decrease)\n");
		

		
		if(willDoHausdorff){
			// Instantiate a HausdorffCalculator; it will report to ThesisSoft on its own.
			HausdorffCalculator aHausdorffCalculator = new HausdorffCalculator(inputVertices, outputVertices);
			
		}else{
			ThesisSoft.receiveMessage("\nNo Hausdorff calculation.\n\n");
		}
		
		
	}
	
	double roundThreeDecimals(double d) {
		// some borrowed code for rounding to two decimals
    	DecimalFormat threeDForm = new DecimalFormat("#.###");
    	return Double.valueOf(threeDForm.format(d));
	}
}
