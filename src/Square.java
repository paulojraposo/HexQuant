
import java.util.ArrayList;

public class Square {
	
	/**
	 * global variables
	 */
	
	int rowID;		// row number in a grid of squares that this square is in;
						// rows numbered top (N) to bottom (S)
	int columnID;	// column number in a grid of squares that this square is in; 
						// columns numbered left (W) to right (E)
	
	int squareID;	// a sequential int identifier, useful for checking whether one is on the same
						// square as the last one processed or not.
	
	Point centroid;  	// the centroid of the square
	Point ul;			// upper-left (NW) corner
	Point ur;			// upper-right (NE) corner
	Point lr;			// lower-right (SE) corner
	Point ll;			// lower-left (SW) corner
	
	
	Double width;	// length of one side; cell size
	
	/**
	 * constructors (4)
	 */
		
	// NOTE: would want to get this so there is only one constructor, and others that pass things onto it - possibly use
	// a factory class.  Would be a good fix for later.
	
	// four explicit points constructor
	public Square(Point topLeft, Point topRight, Point bottomRight, Point bottomLeft, int theRowID, int theColumnID, int theID){
		this.ul = topLeft;
		this.ur = topRight;
		this.lr = bottomRight;
		this.ll = bottomLeft;
		
		this.centroid = new Point( (ur.getX() - ul.getX() ) , (ul.getY() - ll.getY() ) );
		this.width = ur.getX() - ul.getX();
		
		this.rowID = theRowID;
		this.columnID = theColumnID;
		this.squareID = theID;
	}
	
	// four points in an array constructor --> REQUIRES UL,UR,LR,LL SEQUENCE!
	// The constructor I use is this one.
	public Square(Point[] fourPoints, int theRowID, int theColumnID, int theID){
		this.ul = fourPoints[0];
		this.ur = fourPoints[1];
		this.lr = fourPoints[2];
		this.ll = fourPoints[3];
		
		this.centroid = new Point( (ur.getX() - ul.getX() ) , (ul.getY() - ll.getY() ) ); 
		this.width = ur.getX() - ul.getX();
		
		this.rowID = theRowID;
		this.columnID = theColumnID;
		this.squareID = theID;
	}
		
	// four points in an ArrayList constructor --> REQUIRES UL,UR,LR,LL SEQUENCE!
	public Square(ArrayList<Point> fourPoints, int theRowID, int theColumnID, int theID){
		this.ul = fourPoints.get(0);
		this.ur = fourPoints.get(1);
		this.lr = fourPoints.get(2);
		this.ll = fourPoints.get(3);
		
		this.centroid = new Point( (ur.getX() - ul.getX() ) , (ul.getY() - ll.getY() ) ); 
		this.width = ur.getX() - ul.getX();
		
		this.rowID = theRowID;
		this.columnID = theColumnID;
		this.squareID = theID;
	}
	
	// centroid and width constructor
	public Square(Point theCentroid, Double theWidth, int theRowID, int theColumnID, int theID){
		this.centroid = theCentroid;
		this.width = theWidth;
		
		this.ul = new Point(theCentroid.getX() - 0.5 * theWidth, theCentroid.getY() + 0.5 * theWidth);
		this.ur = new Point(theCentroid.getX() + 0.5 * theWidth, theCentroid.getY() + 0.5 * theWidth);
		this.lr = new Point(theCentroid.getX() + 0.5 * theWidth, theCentroid.getY() - 0.5 * theWidth);
		this.ll = new Point(theCentroid.getX() - 0.5 * theWidth, theCentroid.getY() - 0.5 * theWidth);
		
		this.rowID = theRowID;
		this.columnID = theColumnID;
		this.squareID = theID;
	}
	
	
	/**
	 * public methods
	 */
	
	// 
	//  Getters
	//
	
	// point getters - each returns a Point object
	public Point getCentroid(){
		return this.centroid;
	}
	public Point getUpperLeft(){
		return this.ul;
	}
	public Point getUpperRight(){
		return this.ur;
	}
	public Point getLowerRight(){
		return this.lr;
	}
	public Point getLowerLeft(){
		return this.ll;
	}
		
	public ArrayList<Point> getCorners(){
		ArrayList<Point> corners = new ArrayList();
		corners.add(this.ul);
		corners.add(this.ur);
		corners.add(this.lr);
		corners.add(this.ll);
		
		return corners;
	}
	
	// ID getters
	
	public int getSquareID(){
		return this.squareID;
	}
	public int getRowID(){
		return this.rowID;
	}
	public int getColumnID(){
		return this.columnID;
	}

	
	//
	// Setters
	//
	
	// point setters
	public void setCentroid(Point theCentroid){
		this.centroid = theCentroid;
	}
	public void setUL(Point theUL){
		this.ul = theUL;
	}
	public void setUR(Point theUR){
		this.ur = theUR;
	}
	public void setLR(Point theLR){
		this.lr = theLR;
	}
	public void setLL(Point theLL){
		this.ll = theLL;
	}
	
	// row and column id setters
	
	public void setSquareID(int theID){
		this.squareID = theID;
	}
	public void setRowID(int theRow){
		this.rowID = theRow;
	}
	public void setColumnID(int theColumn){
		this.columnID = theColumn;
	}
	public void setRowAndColumn(int theRow, int theColumn){
		this.rowID = theRow;
		this.columnID = theColumn;
	}
	
	// width setter
	public void setWidth(Double theWidth){
		this.width = theWidth;
	}
	
}
