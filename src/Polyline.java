
/*
 * This class was written with peers for our class work in Geog497F (Fall 2010, PSU).
 */

import java.util.ArrayList;


public class Polyline /*implements Geometry*/{

	/**
	 * 		Properties
	 */
	
	private ArrayList<Double> xCoords; 
	private ArrayList<Double> yCoords;

	private double lineLength;
	
	
	
	
	/**
	 * 		Methods
	 */
	
	// Constructor
	
	public Polyline() {	

		this.xCoords = new ArrayList<Double>();
		this.yCoords = new ArrayList<Double>();
	
		this.lineLength	= 0.0;
	
	}

	
	// Public methods
	
	public double addPoint(Double addedX, Double addedY) {
		
		this.xCoords.add(addedX);
		this.yCoords.add(addedY);
			
		this.lineLength = updateLength();
		
		return lineLength;
	}
	
	public double addPoint(Point addedPoint){
		
		this.xCoords.add(addedPoint.getX());
		this.yCoords.add(addedPoint.getY());
		
		this.lineLength = updateLength();
		
		return lineLength;
		
	}
		
	public double getLength(){
		
		return this.lineLength;
	}
	
	public double getXofPoint(int i){
		
		return this.xCoords.get(i);
	}
	
	public double getYofPoint(int i){
		
		return this.yCoords.get(i);
	}
	
	public String listPoints(){

		String listOfPoints = "\nThis polyline is defined by the following points: \n";
		
		for (int i=0; i < this.xCoords.size() -2; i++){
			
			listOfPoints += String.format("(%.3f, %.3f), ", this.xCoords.get(i), this.yCoords.get(i)); 
						
		}
		
		listOfPoints += String.format("(%.3f, %.3f)\n", this.xCoords.get(this.xCoords.size() -1), 
				this.yCoords.get(this.yCoords.size() -1));
		
		return listOfPoints;
	}
	
	public Point[] convertToPoints(){
		
		Point[] points = new Point[this.xCoords.size()];
		
		for (int i=0; i < this.xCoords.size() - 1; i++){
			
			points[i] = new Point(this.xCoords.get(i), this.yCoords.get(i));
		}
		
		return points;
		
	}
	
	
	public Point getPoint(int i){
		return new Point(this.xCoords.get(i), this.yCoords.get(i));
	}
	
	public int pointNum(){
		return this.xCoords.size();
	}
	

	
	
	// Private methods
	
	private double updateLength() {
		
		double lineLength = 0.0;
		
		for (int i=0; i < this.xCoords.size() -1; i++){
				
			lineLength += euclideanDistance(this.xCoords.get(i), this.yCoords.get(i), 
					this.xCoords.get(i+1), this.yCoords.get(i+1)); 
			
		}
		
		return lineLength;
	}
	
	private double euclideanDistance(Double xCoords1, Double yCoords1,
											Double xCoords2, Double yCoords2){
		
		double lineLength = 0.0;
		
		lineLength = Math.sqrt( Math.pow((xCoords1 - xCoords2), 2) + 
					 			Math.pow((yCoords1 - yCoords2), 2) );
				
		return lineLength;	
	}
	
}