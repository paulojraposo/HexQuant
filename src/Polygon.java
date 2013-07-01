
/*
 * This class was written with peers for our class work in Geog497F (Fall 2010, PSU).
 * Was updated so that the point-in-polygon ray-casting algorithm can operate on vertical line
 * segments too.
 */

import java.util.ArrayList;


public class Polygon /*implements Geometry*/{

	/**
	 * 		Properties
	 *
	 * 		Note: polygon is designed to close and finish when the last added point snaps to the first.
	 */
	
	private ArrayList<Double> xCoords; 
	private ArrayList<Double> yCoords;

	private Double perimeterLength;
	
	private boolean polygonClosed;
	
	private Double tolerance = 0.01;
	
	
	/**
	 * 		Methods
	 */
	
	// Constructor
	
	public Polygon() {	

		this.xCoords = new ArrayList<Double>();
		this.yCoords = new ArrayList<Double>();
	
		this.perimeterLength = 0.0;
		this.polygonClosed = false;
		
	}

	
	// Public methods
	
	public boolean addPoint(Double addedX, Double addedY) {
	
		boolean successfulAddition = false;
		
		Point aPoint = new Point(addedX, addedY);
		
		successfulAddition = this.addPoint(aPoint);
		
		return successfulAddition;
	}
	
	public boolean addPoint(Point addedPoint){
		
		boolean successfulAddition = false;
		
		if( (xCoords.size()>=3) && (polygonClosed == false) ){
			
			Point firstPoint = new Point(this.xCoords.get(0), this.yCoords.get(0));
			
			if(firstPoint.isWithinBuffer(addedPoint, this.tolerance) == true){
				this.xCoords.add(this.xCoords.get(0));
				this.yCoords.add(this.yCoords.get(0));
				
				this.polygonClosed = true;
				successfulAddition = true;
				
			} else {
				this.xCoords.add(addedPoint.getX());
				this.yCoords.add(addedPoint.getY());
				
				successfulAddition = true;
			}
		} 
		
		if( xCoords.size()<3) {
			this.xCoords.add(addedPoint.getX());
			this.yCoords.add(addedPoint.getY());
			
			successfulAddition = true;
		}
		
		this.perimeterLength = updateLength();
		
		//return perimeterLength;
		
		return successfulAddition;
		
	}
		
	public Double getPerimeterLength(){
		
		return this.perimeterLength;
	}
	
	public String listPoints(){

		String listOfPoints = "\nThis polygon is defined by the following points: \n";
		
		for (int i=0; i < this.xCoords.size() -2; i++){
			
			listOfPoints += String.format("(%.3f, %.3f), ", this.xCoords.get(i), this.yCoords.get(i)); 
						
		}
		
		listOfPoints += String.format("(%.3f, %.3f)\n", this.xCoords.get(this.xCoords.size() -1), 
				this.yCoords.get(this.yCoords.size() -1));
		
		return listOfPoints;
	}
	
	public Point[] convertToPoints(){
		
		Point[] points = new Point[this.xCoords.size()];
		
		for (int i=0; i < this.xCoords.size(); i++){
			
			Point aPoint = new Point(this.xCoords.get(i), this.yCoords.get(i));
			points[i] = aPoint;
		}
		
		return points;
		
	}
	
	
	public int getSize(){
		
		return this.xCoords.size();
	}
	
	public double getXofPoint(int i){
		
		return this.xCoords.get(i);
	}
	
	public double getYofPoint(int i){
		
		return this.yCoords.get(i);
	}
	
	public boolean isWithinPolygon(Point aPoint){
		
		boolean within = false;
		
		int intersections = 0;
		
		for(int i=0; i<this.xCoords.size() -1; i++){
			
			Boolean intersects = segmentIntersectsRay(aPoint, 
								new Point(xCoords.get(i), yCoords.get(i)), 
								new Point(xCoords.get(i+1), yCoords.get(i+1))); 
						
			if(intersects){
				 intersections++;
			}
			
		}
		
		if((intersections % 2) != 0){
			within = true;
		}
		
		return within;
		
	}
	
	public boolean isWithinBuffer(Point aPoint, Double buffer){
		
		System.out.println("This method is not implemented yet...");
		
		boolean inside = false;
		
		return inside;
		
	}
	
	public double calculateArea(){
		return 0.0;
	}
	
	public boolean getClosedStatus(){
		return polygonClosed;
	}
	
	// Private methods
	
	private Boolean segmentIntersectsRay(Point rayBasePoint, Point segmentPoint1, Point segmentPoint2){
		
		boolean intersects = false;
		
		/**
		 * This implements the ray casting solution, using a horizontal ray (slope = 0) from the point passed to the method,
		 * extending "eastward" toward x = infinity.
		 */
		// if segment is horizontal, no intersection.
		if(segmentPoint1.getY() == segmentPoint2.getY()){
			//return intersects;
			
		// if segment is vertical:	
		}else if(segmentPoint1.getX() == segmentPoint2.getX()){
			
			if(		// if base point is "west" of segment, and it's y coordinate is within the segment endpoint y coordinates:
					(rayBasePoint.getX() < segmentPoint1.getX() ) && 
					( (rayBasePoint.getY() < segmentPoint1.getY() && rayBasePoint.getY() > segmentPoint2.getY())
							||
					  (rayBasePoint.getY() > segmentPoint1.getY() && rayBasePoint.getY() < segmentPoint2.getY()))	){
				
				intersects = true;
				//return intersects;
				
			}
			
			
		}else{	// or most cases, where segment has a slope:
			
			double segmentSlope = (segmentPoint2.getY() - segmentPoint1.getY()) / 
									(segmentPoint2.getX() - segmentPoint1.getX());
			
			// Determine the y-intercept of the segment line (of which the segment is a subset)
			double segmentYIntercept = segmentPoint1.getY() - (segmentSlope * segmentPoint1.getX());
			
			// calculate x coord of intersection point of ray and segment line (of which the segment is a subset)
			double IntersectionPointxCoord = (rayBasePoint.getY() - segmentYIntercept) / segmentSlope;
			
			// establish intersection point of ray and segment line (of which the segment is a subset), using known x and y values
			Point intersectionPoint = new Point(IntersectionPointxCoord, rayBasePoint.getY());
			
			// Check whether the intersection point is on the segment using concept of orthogonal bounding box around segment.
			BoundingBox segmentBoundingBox = new BoundingBox();
			
			segmentBoundingBox.setBox(segmentPoint1, segmentPoint2);
			
			
			intersects = segmentBoundingBox.containsPoint(intersectionPoint);
					
			
			// Check whether the intersection point is on the "eastward"-extending ray.
			if(intersectionPoint.getX() < rayBasePoint.getX()){
				intersects = false;
			}
			
			
		}
		
		return intersects;
	}
	
	private Double updateLength() {
		
		Double lineLength = 0.0;
		
		for (int i=0; i < this.xCoords.size() -1; i++){
				
			lineLength += euclideanDistance(this.xCoords.get(i), this.yCoords.get(i), 
					this.xCoords.get(i+1), this.yCoords.get(i+1)); 
			
		}
		
		return lineLength;
	}
	
	private Double euclideanDistance(Double xCoords1, Double yCoords1,
											Double xCoords2, Double yCoords2){
		
		Double lineLength = 0.0;
		
		lineLength = Math.sqrt( Math.pow((xCoords1 - xCoords2), 2) + 
					 			Math.pow((yCoords1 - yCoords2), 2) );
				
		return lineLength;	
	}
	
	
}