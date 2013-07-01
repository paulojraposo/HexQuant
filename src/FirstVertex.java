
/*
 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::
 * This class gets instantiated once every pass through a tessera.  It allows 
 * points to be added to a local ArrayList of type Point, and then determines
 * which point is first in the ArrayList.
 * 
 */

import java.util.ArrayList;

public class FirstVertex implements CollapseMethod {
	
	/*
	 *   Declarations
	 */
	
	private Point FirstVertex;
	
	public ArrayList<Point> contributingPoints;
	
	/*
	 *   Methods
	 */
	
	// Constructor
	public FirstVertex(){
		
		// instantiation of contributingPoints, that's all
		this.contributingPoints = new ArrayList(0);		
	}
	
	public void addPoint(Point aPoint){
		// adds points to the local ArrayList contributingPoints
		this.contributingPoints.add(aPoint);
	}
	
	public Point collapse(){
		// get the first point, set that FirstVertex, and return it.
		this.FirstVertex = this.contributingPoints.get(0);
		return this.FirstVertex;
	}
	
	public Point getCollapse(){
		// getter method for getting this.vertexNearestMean
		return this.FirstVertex;
	}

}
