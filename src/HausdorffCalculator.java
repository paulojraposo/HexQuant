
import java.text.DecimalFormat;
import java.util.ArrayList;

/*
 * This calculates the Hausdorff distances between any two sets of points, passed to the class
 * as ArrayList<Point>.  
 * 
 * As written, the points need to be within 10,000,000 units of distance from each other for this code to work
 * (see below).
 * 
 * Written for measuring distance across UTM R^2 space, so coordinates already are units in meters, or feet.
 */


public class HausdorffCalculator {
	
	/*
	 * constructor - accepts two ArrayLists of type Point
	 */ 
	public HausdorffCalculator (ArrayList<Point> theInputVertices,
								ArrayList<Point> theSimplifiedVertices){
		
		// Loop through all points in theInputVertices, and calculate distance to each point in 
		// theSimplifiedVertices.
		// Keep a running variable (initialized to zero) that is the greatest distance yet seen.
		// This calculates directional Hausdorff distance from input line to generalized line.
			
		double hausInputToGen = 0.0;	
		
		for(int i=0; i < theInputVertices.size(); i++){
			
			double measure = 10000000.0;		// starting at ten million, since no UTM zone gets bigger
												// than 1/4 the polar circumference of earth :)
			
			for(int j=0; j < theSimplifiedVertices.size(); j++){
				
				double measurePrime = 0.0;
				
				measurePrime = theInputVertices.get(i).euclideanDistance(theInputVertices.get(i).getX(), theInputVertices.get(i).getY(),
						theSimplifiedVertices.get(j).getX(), theSimplifiedVertices.get(j).getY());
				
				if(measurePrime <= measure){
					measure = measurePrime;	
				}

			}
			
			if(measure >= hausInputToGen){
				hausInputToGen = measure;
			}
			
		}
		
		// loop through all points in generalized line, and calculate distance to each point in original line
		// ... as per loop above
		// This calculates directional Hausdorff distance from generalized line to input line
		
		double hausGenToInput = 0.0;
		
		for(int i=0; i < theSimplifiedVertices.size(); i++){
			
			double measure = 10000000.0;		// starting at ten million, since no UTM zone gets bigger
												// than 1/4 the polar circumference of earth :)
			
			for(int j=0; j < theInputVertices.size(); j++){
				
				double measurePrime = 0.0;
				
				measurePrime = theSimplifiedVertices.get(i).euclideanDistance(theSimplifiedVertices.get(i).getX(), theSimplifiedVertices.get(i).getY(),
						theInputVertices.get(j).getX(), theInputVertices.get(j).getY());
				
				if(measurePrime <= measure){
					measure = measurePrime;
				}
				
			}
			
			if(measure >= hausGenToInput){
				hausGenToInput = measure;
			}
			
		}
		
		// Compare two directional Hausdorff distances and take the greatest for absolute
		double hausdorffDist = 0.0;
		hausdorffDist = Math.max(hausInputToGen, hausGenToInput);
		
		
		// report to user in GUI 
		ThesisSoft.receiveMessage("\n~~ Hausdorff Report ~~~~~~~~~~~~~~~\n");
		ThesisSoft.receiveMessage("h(input to simplified) = " + roundTwoDecimals(hausInputToGen) + " m \n");
		ThesisSoft.receiveMessage("h(simplified to input) = " + roundTwoDecimals(hausGenToInput) + " m \n");
		ThesisSoft.receiveMessage("* H(input, simplified) = " + roundTwoDecimals(hausdorffDist) + " m \n");
		ThesisSoft.receiveMessage("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		
		
	}
	
	double roundTwoDecimals(double d) {
		// some borrowed code for rounding to two decimals
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	return Double.valueOf(twoDForm.format(d));
	}
	
}
