
/*
 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::
 * This class gets instantiated once every pass through a tessera. It allows 
 * points to be added to a local ArrayList of type Point, and then determines
 * which point is last in the ArrayList.
 * 
 */

import java.util.ArrayList;

public class LastVertex implements CollapseMethod {
	
	/*
	 *   Declarations
	 */
	
	private Point LastVertex;
	
	public ArrayList<Point> contributingPoints;
	
	/*
	 *   Methods
	 */
	
	// Constructor
	public LastVertex(){
		
		// instantiation of contributingPoints
		this.contributingPoints = new ArrayList(0);		
	}
	
	public void addPoint(Point aPoint){
		// adds points to the local ArrayList contributingPoints
		this.contributingPoints.add(aPoint);
	}
	
	public Point collapse(){
		// get the last point, set that FirstVertex, and return it.
		this.LastVertex = this.contributingPoints.get(this.contributingPoints.size()-1);
		return this.LastVertex;
	}
	
	public Point getCollapse(){
		// getter method for getting this.vertexNearestMean
		return this.LastVertex;
	}

}
