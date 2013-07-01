
/*
 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::
 * This class gets instantiated once every pass through a tessera.  It allows 
 * points to be added to a local ArrayList of type Point, and then determines
 * which point among the input points is closest to the spatial mean, taking its
 * x,y coordinates for the collapsed point.
 * 
 */

import java.util.ArrayList;

public class VertexNearestMean implements CollapseMethod {
	
	/*
	 *   Declarations
	 */
	
	private Point vertexNearestMean;
	
	public ArrayList<Point> contributingPoints;
	
	/*
	 *   Methods
	 */
	
	// Constructor
	public VertexNearestMean(){
		
		// instantiation of contributingPoints
		this.contributingPoints = new ArrayList(0);		
	}
	
	public void addPoint(Point aPoint){
		// adds points to the local ArrayList contributingPoints
		this.contributingPoints.add(aPoint);
	}
	
	public Point collapse(){
		// calculates a single point at the spatial mean of all points in contributingPoints, measures
		// distance from each input Point to that mean, and sets this.vertexNearestMean to be the 
		// Point at least distance.
		
		Double sumX = 0.0;
		Double sumY = 0.0;
		
		for (int i=0; i < this.contributingPoints.size(); i++){
			// loop through all elements of contributingPoints and add X and Y values 
			// to sumX and sumY
			sumX = sumX + this.contributingPoints.get(i).getX();
			sumY = sumY + this.contributingPoints.get(i).getY();
		}
		
		// calculate means
		Double meanX = sumX / this.contributingPoints.size();
		Double meanY = sumY / this.contributingPoints.size();
		
		// create a new Point using the means
		Point spMean = new Point(meanX, meanY);
		
		double comparisonDistance = 10000000.0;			// starting very, very far away :)
		
		// loop through this.contributingPoints and measure distance from spMean
		for(int i=0; i < this.contributingPoints.size(); i++){
			double aDistance = this.contributingPoints.get(i).euclideanDistance(
															this.contributingPoints.get(i).getX(),
															this.contributingPoints.get(i).getY(), 
															spMean.getX(), 
															spMean.getY());
			// if aDistance is less than comparisonDistance, set this.vertexNearestMean
			// to that Point, and update comparisonDistance for next loop.
			if(aDistance < comparisonDistance){
				this.vertexNearestMean = this.contributingPoints.get(i);
				comparisonDistance = aDistance;
			}
		}
		
		// return this.vertexNearestMean so calls to this function from other classes can get a Point at calculation.
		return this.vertexNearestMean;
	}
	
	public Point getCollapse(){
		// getter method for getting this.vertexNearestMean
		return this.vertexNearestMean;
	}

}
