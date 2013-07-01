
/*
 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::
 * This class gets instantiated once every pass through a tessera.  It allows 
 * points to be added to a local ArrayList of type Point, and then calculates the 
 * spatial mean of these when asked of collapse()
 * 
 */

import java.util.ArrayList;

public class SpatialMean implements CollapseMethod {
	
	/*
	 *   Declarations
	 */
	
	private Point spatialMean;
	
	public ArrayList<Point> contributingPoints;
	
	/*
	 *   Methods
	 */
	
	// Constructor
	public SpatialMean(){  
		// instantiation of contributingPoints
		this.contributingPoints = new ArrayList(0);		
	}
	
	public void addPoint(Point aPoint){
		// adds points to the local ArrayList contributingPoints
		this.contributingPoints.add(aPoint);
	}
	
	public Point collapse(){
		// calculates a single point at the spatial mean of all points in contributingPoints
		
		// local variables
		Double sumX = 0.0;
		Double sumY = 0.0;
		
		for (int i=0; i<this.contributingPoints.size(); i++){
			// loop through all elements of contributingPoints and add X and Y values 
			// to sumX and sumY
			sumX = sumX + this.contributingPoints.get(i).getX();
			sumY = sumY + this.contributingPoints.get(i).getY();
		}
		
		// calculate means
		Double meanX = sumX / this.contributingPoints.size();
		Double meanY = sumY / this.contributingPoints.size();
		
		// create a new Point (local to method) using the means
		Point spMean = new Point(meanX, meanY);
		
		// equate this instance's class Point spatialMean to spMean from above
		this.spatialMean = spMean;
		
		// return spMean so calls to his function from other classes can get a Point.
		return spMean;
	}
	
	public Point getCollapse(){
		// getter method for getting this.spatialMean
		return this.spatialMean;
	}

}
