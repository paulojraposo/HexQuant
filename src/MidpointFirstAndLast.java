
/*
 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::
 * This class gets instantiated once every pass through a tessera.  It allows 
 * points to be added to a local ArrayList of type Point, and then calculates the 
 * the midpoint between the first and last points when asked of collapse().
 * 
 */

import java.util.ArrayList;

public class MidpointFirstAndLast implements CollapseMethod {
	
	/*
	 *   Declarations
	 */
	
	private Point midpointFirstAndLast;
	
	public ArrayList<Point> contributingPoints;
	
	/*
	 *   Methods
	 */
	
	// Constructor
	public MidpointFirstAndLast(){  
		// instantiation of contributingPoints
		this.contributingPoints = new ArrayList(0);
	}
	
	public void addPoint(Point aPoint){
		// adds points to the local ArrayList contributingPoints
		this.contributingPoints.add(aPoint);
	}
	
	public Point collapse(){
		// calculates a single point at the spatial mean of all points in contributingPoints
		
		Double midpointX = 0.0;
		Double midpointY = 0.0;
		
		// calculate X and Y of midpoint ( (first + last) / 2 )
		midpointX = (	((this.contributingPoints.get(0).getX()) + 
						(this.contributingPoints.get(this.contributingPoints.size()-1).getX()))  / 2.0);
		
		midpointY = (	((this.contributingPoints.get(0).getY()) + 
						(this.contributingPoints.get(this.contributingPoints.size()-1).getY()))  / 2.0);
		
		// create a new Point using the means
		Point theMidpoint = new Point(midpointX, midpointY);
		
		// equate this instance's class Point midpointFirstAndLast to theMidpoint from above, thereby instantiating it
		this.midpointFirstAndLast = theMidpoint;
		
		// return theMidpoint so calls to his function from other classes can get a Point.
		return theMidpoint;
	}
	
	public Point getCollapse(){
		// getter method for getting this.midpointFirstAndLast
		return this.midpointFirstAndLast;
	}

}
