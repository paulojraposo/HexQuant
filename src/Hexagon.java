
/*
 * This class written with peers as part of our project for Geog497F (Fall 2010, PSU).
 * 
 * Hexagonal tessellation set up by first calculating centroids for hexagons (in class HexGrid).  
 * Then, each hexagon constructs its vertices with this class using x and y shift values from the centroid coordinates.
 * 
 */

public class Hexagon {
	  
	  // Coordinates in points are stored as doubles.
	  // intX and intY provide integer conversion facilities.
	  
	  private double xCoord;        // location of the center of hexagon
	  private double yCoord;
	  
	  private double hexRadius;     // radius measured center to vertex
	  private double inRadius;      // radius measured center to side (inscribed)
	  
	  private double[] xShift;      // = new double[6];
	  private double[] yShift;      // = new double[6];
	  
	  private Point[] vertices;     // vertices of the hexagon
	  
	  private double snapDistance;  // distance buffer for topological operations
	  
	  private int id;				
	  
	  // Constructor
	  Hexagon (double x, double y, double hexRadius, double[] xShift, double[] yShift, int id) {
	    
	    this.xCoord = x;
	    this.yCoord = y;
	    
	    this.id = id;
	    
	    // Calculate the inscribed radius
	    this.hexRadius = hexRadius;
	    this.inRadius  = this.hexRadius * Math.sqrt(3) / 2;
	    
	    // Calculate the coordinate shifts for vertices
	    this.xShift = xShift;
	    this.yShift = yShift;                           // this is unsafe: no checks for validity performed at all
	    
	    // Update the distance buffer
	    this.snapDistance = this.hexRadius * 0.01;      // one percent of the hexagon radius
	    
	    // Generate points
	    this.vertices = new Point[6];
	    this.generatePoints();
	    
	    //println("Hexagon  " + this.id + " at " + this.intX() + ", " + this.intY());
	  }
	  
	  
	  void generatePoints() {
	    
	    for (int i = 0; i < 6; i++) {

	      this.vertices[i] = new Point(this.xCoord + this.xShift[i], this.yCoord + this.yShift[i]);
	    }
	  }
	  
	  
	  // Topology-related methods
	  
	  Point getPt(int i) {
	    
	    return this.vertices[i];
	  }
	  
	  
	  boolean setPt(int i, Point pt) {
	    
	    boolean ptUpdated = false;
	    
	    double ptDistance = this.euclideanDistance(this.vertices[i].getX(), this.vertices[i].getY(), pt.getX(), pt.getY());
	    
	    // If the proposed point is within buffer distance from the original, ...
	    if (ptDistance < this.snapDistance) {
	      
	      // ... replace the original with the proposed one.
	      this.vertices[i] = pt;
	      ptUpdated = true;
	    } else {
	      
	      //System.out.println("Topology flaw found");
	    }
	    
	    return ptUpdated;
	  }
	  
	  int getId() {
	   
	    return this.id;
	  }
	  
	  
	  // Get/set
	  
	  double getX () {
	    return this.xCoord;
	  }
	  
	  double getY () {
	    return this.yCoord;
	  }
	  
	  void setX (double x) {
	    
	    // WARNING - updating the center of hexagon 
	    // regenerates the points, breaking the topology
	    
	    this.xCoord = x;
	    //this.generatePoints();
	  }
	  
	  void setY (double y) {
	    
	    // WARNING - updating the center of hexagon 
	    // regenerates the points, breaking the topology
	    
	    this.yCoord = y;
	    //this.generatePoints();
	  }
	  
	  public double euclideanDistance(double xCoords1, double yCoords1, double xCoords2, double yCoords2) {

			Double lineLength = 0.0;

			lineLength = Math.sqrt( Math.pow((xCoords1 - xCoords2), 2) + 
									Math.pow((yCoords1 - yCoords2), 2) );

			return lineLength;	
		}
	  
}

