
/*
 * :::::::::::::::::::::::::::::::::::::::::::::::::::::::
 * This class gets instantiated once every pass through a tessera.  It allows 
 * points to be added to a local ArrayList of type Point, and then determines
 * which point among the input points is furthest from the tessera centroid.
 * 
 * NOTE: This collapse method is different from the others in that it requires that the tessera 
 * polygon be passed to it's constructor.  (Could re-write so that algorithm classes calculate
 * centroids and pass just the point, but this seems better.)
 * 
 */

import java.util.ArrayList;

public class VertexFurthestCentroid implements CollapseMethod {
	
	/*
	 *   Declarations
	 */
	
	private Point vertexFurthestFromCentroid;
	
	private Point centroid;
	
	public ArrayList<Point> contributingPoints;
	
	/*
	 *   Methods
	 */
	
	// Constructor - requires a polygon passed to it.
	public VertexFurthestCentroid(Polygon theTessera){
		
		// instantiation of contributingPoints.
		this.contributingPoints = new ArrayList(0);
		
		// establish tessera centroid
		// note: doing this crudely, getting centroid of tessera's orthogonal bounding box; okay for equilateral polygons.
		// A more sophisticated approach would use line intersection calculation from polygon vertices.
		
		// set each of these to the values of the first vertex
		// TODO: check that I'm calling the various members of this array properly.
		double xExtremum =  theTessera.convertToPoints()[0].getX();
		double xInfimum =   theTessera.convertToPoints()[0].getX();
		double yExtremum  = theTessera.convertToPoints()[0].getY();
		double yInfimum  =  theTessera.convertToPoints()[0].getY();
		
		// loop through vertices of given polygon/tessera and update four variables above as higher/lower values found.
		// note: leaving loop iterator to start at zero so that this can handle ArrayLists of size 1 as well.
		for(int i=0; i<this.contributingPoints.size(); i++){
			xExtremum = Math.max(xExtremum, theTessera.convertToPoints()[i].getX());
			xInfimum =  Math.min(xExtremum, theTessera.convertToPoints()[i].getX());
			yExtremum = Math.max(yExtremum, theTessera.convertToPoints()[i].getY());
			yInfimum =  Math.min(yInfimum,  theTessera.convertToPoints()[i].getY());
			
		}
		
		// establish the centroid at the point defined by midpoints between x and y extrema and infima
		this.centroid = new Point( (xExtremum - xInfimum) , (yExtremum - yInfimum) );
	}
	
	public void addPoint(Point aPoint){
		// adds points to the local ArrayList contributingPoints
		this.contributingPoints.add(aPoint);
	}
	
	public Point collapse(){
		
		// Start at the centroid itself.
		double aDistance = 0.0;
		
		// loop through all points in contributingPoints, and measure their distance from the centroid.
		// Update aDistance (starting at zero) and this.vertexFurthestFromCentroid as further distances are found.
		for(int i=0; i<this.contributingPoints.size(); i++){
			
			double anotherDistance = this.centroid.euclideanDistance(	this.contributingPoints.get(i).getX(), 
																		this.contributingPoints.get(i).getY(), 
																		this.centroid.getX(), 
																		this.centroid.getY()	); 
			
			if(	anotherDistance	> aDistance ) {
				aDistance = anotherDistance;		// update for next iteration
				this.vertexFurthestFromCentroid = this.contributingPoints.get(i);	// set vertexFurthestFromCentroid to this further point.	
			}	
		}
		
		// return this.vertexFurthestFromCentroid so calls to this function from other classes can get a Point at calculation.
		return this.vertexFurthestFromCentroid;
	}
	
	public Point getCollapse(){
		// getter method for getting this.vertexFurthestFromCentroid.
		return this.vertexFurthestFromCentroid;
	}

}
