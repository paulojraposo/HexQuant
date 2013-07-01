
/*
 * This class was written with peers for our class work in Geog497F (Fall 2010, PSU).
 */

public class Point {


	/**
	 * 		Properties
	 */
	
	private Double xCoord; 
	private Double yCoord;
	private double buffer;
	private boolean xTransformed;
	private boolean yTransformed;
	
	
	
	/**
	 * 		Methods
	 */
	
	// Constructor
	
	public Point(Double X, Double Y) {	

		this.xCoord = X;
		this.yCoord = Y;
		
		this.buffer = 0.0;

		this.xTransformed = false;
		this.yTransformed = false;
	}
	
	public Point(Double X, Double Y, double buffer) {	

		this.xCoord = X;
		this.yCoord = Y;
		
		this.buffer = buffer;
		
		this.xTransformed = false;
		this.yTransformed = false;
		
	}

	
	// Public methods
	
	public boolean isWithinBuffer(Point aPoint, Double buffer){
		boolean inside = false;
		
		if(this.euclideanDistance(aPoint.getX(), aPoint.getY(), this.getX(), this.getY()) <= buffer){
			inside = true;
		}
		
		return inside;
	}
	
	public double getX(){
		return xCoord;
	}
	
	public double getY(){
		return yCoord;
	}
	
	public void setX(double x) {	

		if (this.xTransformed == false) {
			this.xCoord = x;
			this.xTransformed = true;
		}
	}
	
	public void setY(double y) {	

		if (this.yTransformed == false) {
			this.yCoord = y;
			this.yTransformed = true;
		}
	}
	
	public double getBuffer(){
		return this.buffer;
	}
	
	public void setBuffer(double buffer){
		this.buffer = buffer;
	}	
	
	public void updatePoint(Double X, Double Y) {
		
		this.xCoord = X;
		this.yCoord = Y;
	}
	
	
	public String reportPoint(){

		String pointReport = String.format("The point is (%.4f, %.4f)", this.xCoord, this.yCoord);
				
		return pointReport;
	}
	
	public Double euclideanDistance(Double xCoords1, Double yCoords1,
			Double xCoords2, Double yCoords2){

		Double lineLength = 0.0;

		lineLength = Math.sqrt( Math.pow((xCoords1 - xCoords2), 2) + 
								Math.pow((yCoords1 - yCoords2), 2) );

		return lineLength;	
	}
	
}
