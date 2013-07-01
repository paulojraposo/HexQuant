
import java.util.ArrayList;

public class SquareGrid {
	
	/**
	 * global variables
	 */
	
	double gridResolution;              // side to side diameter of squares in grid
	  
	Point upperLeft;                 	// bounding box for the underlying data
	Point bottomRight;               	//
	  
	Integer gridRows;                   // number of rows in the grid
	Integer gridColumns;                // number of columns in the grid
	  
	ArrayList<Square> squares;			// array list of Square instances
	
	Point[] preSquare;					// an array to hold and index points of a square under point-by-point construction
	
	/**
	 * Constructor
	 * 
	 * Note: this allows a number of global variables to get set, which get used by buildSquareGrid() afterward.
	 */
	
	SquareGrid(Point uLeft, Point bRight, double squareDiameter){
		
	    // assign grid resolution
	    this.gridResolution = squareDiameter;
		
		// set Points upperLeft and bottomRight
	    this.upperLeft = 	uLeft;
	    this.bottomRight = 	bRight;
		
		// calculate dimensions of input line bounding box
		double deltaX = bRight.getX() - uLeft.getX();
	    double deltaY = uLeft.getY() - bRight.getY();
	    
	    // calculate number of rows and columns needed for grid;
	    // using integer quotient plus 2, to get two extra rows/columns of cells to ensure whole
	    // bounding box is covered after shifting cells over.
	    Double columnsNeededQuotient = 	deltaX / squareDiameter;
	    Double rowsNeededQuotient = 	deltaY / squareDiameter;
	    
	    this.gridColumns = 	columnsNeededQuotient.intValue() + 2;
	    this.gridRows = 	rowsNeededQuotient.intValue() + 2;
	    
	    //initialize ArrayList squares
	    this.squares = new ArrayList(0);
	    	    
	    // buildSquareGrid() will build the tessellation using global variables just set
	    this.buildSquareGrid();
	    
	}
	
	void buildSquareGrid(){

		/*
		 * ::: Notes :::::::::::::::::::::::::::::::::::::::::::::::::
		 * 
		 * Approaching column by column.  First building the left-most (western) column, then each subsequent column moving right (east).
		 * 
		 * Incrementing ints are used for assigning ID's to a Square.  These are three: currentRow, currentColumn, and currentID.
		 * 
		 *   currentID: incremented BEFORE making a new Square, then stamped on that square
		 *   
		 *   currentColumn: incremented when making a new column, BEFORE making a new Square in that column, 
		 *   			    then stamped on that square and all others of that column.
		 *   
		 *   currentRow: begins at zero when a new column is started (reset to zero at the beginning of a new column).
		 *   			 incremented BEFORE making a new Square in a new row, then stamped on that square
		 * 
		 * 
		 * Four Point's defined for each cell, always in ul, ur, lr, ll sequence, in a recycled array called this.preSquare.  
		 * With four points defined and populating the array, this.preSquare is passed, along with 
		 * currentRow, currentColumn, and currentID, to a function (buildSquare) to make a Square.  That Square is 
		 * immediately stored to an array list of Square's called "squares".
		 * 
		 * There is point-borrowing going on between cells being constructed and pre-existing Square's (see code below 
		 * for details), getting those pre-existing Square's (or rather, their vertices) based on their index position 
		 * in array list "squares" and what the current currentID value is.
		 * 
		 * After whole grid of squares is built, with each cell stored in "squares", each Square is used to build a 
		 * Polygon; we do this to use the isWithinPolygon() function, which checks whether a point is in a polygon. 
		 * This is done with a provided public function, getPolygonList().
		 * 
		 * Taking x and y in Cartesian coordinates, not computer coordinates.
		 * 
		 */
		
		/*
		 * COLUMN 1 (extreme left, "west")
		 *
		 */
		
		// initializing all row, column and id iterators
		int currentRow 		= 0;
		int currentColumn 	= 0;
		int currentID 		= 0;
		
		// Initializing this.preSquare array, which will store points of a cell before they are made into a Square.
		// Size is four for the four points of the square.
		this.preSquare = new Point[4];
		
		//System.out.println("upper left BB point: " + this.upperLeft.getX() + " " + this.upperLeft.getY());
		//System.out.println("bottom right BB point: " + this.bottomRight.getX() + " " + this.bottomRight.getY());
		
		/*
		 * FIRST SQUARE
		 * 
		 */
		
		// Calculating the very first point, top-left of the first square and the whole grid - first point in this.preSquare
		this.preSquare[0] = new Point(this.upperLeft.getX() - (0.5 * this.gridResolution) , this.upperLeft.getY() + (0.5 * this.gridResolution) );
		// next points, by offsets
		this.preSquare[1] = new Point(this.preSquare[0].getX() + this.gridResolution  , this.preSquare[0].getY() );
		this.preSquare[2] = new Point(this.preSquare[1].getX() , this.preSquare[1].getY() - this.gridResolution );
		this.preSquare[3] = new Point(this.preSquare[0].getX() , this.preSquare[2].getY() );
				
		// call buildSquare() to get a Square object, the first in this case, and add it
		// as the first element to array list "squares"
		
		//:::::
		this.squares.add( buildSquare( this.preSquare, currentRow, currentColumn, currentID ) );

		
		//:::::
		
		/*
		 * REST OF Column 1 SQUARES
		 * 
		 */
		
		for(int r = 1; r < this.gridRows; r++){
			
			// increment currentRow and currentID
			currentRow ++;
			currentID ++;
			
			// define the four points of the square, borrowing from the Square in squares array list before
			this.preSquare[0] = new Point(this.squares.get(currentID-1).getLowerLeft().getX()  , 
										this.squares.get(currentID-1).getLowerLeft().getY() );
			this.preSquare[1] = new Point(this.squares.get(currentID-1).getLowerRight().getX() , 
										this.squares.get(currentID-1).getLowerRight().getY() );
			this.preSquare[2] = new Point(this.squares.get(currentID-1).getLowerRight().getX() , 
										this.squares.get(currentID-1).getLowerRight().getY() - this.gridResolution );
			this.preSquare[3] = new Point(this.squares.get(currentID-1).getLowerLeft().getX()  , 
										this.squares.get(currentID-1).getLowerLeft().getY() - this.gridResolution );
			
			// call buildSquare() to get a Square object, and add it
			// as the next element to array list "squares"
			this.squares.add( buildSquare( this.preSquare, currentRow, currentColumn, currentID) );
	
		}
		
		/*
		 * NEXT AND ALL OTHER COLUMNS
		 * 
		 */
		
		for(int c = 1; c < this.gridColumns; c++){
			
			// Restart currentRow at 0...
			currentRow = 0;
			// and set currentColumn to c (from loop iterator), and increment currentID.
			currentColumn = c;
			currentID++;
			
			/*
			 * FIRST CELL IN NEW COLUMN
			 */
			
			this.preSquare[0] = new Point(this.squares.get(currentID - this.gridRows).getUpperRight().getX() , 
										this.squares.get(currentID - this.gridRows).getUpperRight().getY() );
			this.preSquare[1] = new Point(this.squares.get(currentID - this.gridRows).getUpperRight().getX() + this.gridResolution , 
										this.squares.get(currentID - this.gridRows).getUpperRight().getY() );
			this.preSquare[2] = new Point(squares.get(currentID - this.gridRows).getLowerRight().getX() + this.gridResolution , 
										this.squares.get(currentID - this.gridRows).getLowerRight().getY() );
			this.preSquare[3] = new Point(this.squares.get(currentID - this.gridRows).getLowerRight().getX() , 
										this.squares.get(currentID - this.gridRows).getLowerRight().getY() );
			
			// Call buildSquare() to get a Square object, and add it
			// as the next element to array list "squares".
			this.squares.add( buildSquare( this.preSquare, currentRow, currentColumn, currentID) );
			
			/*
			 * REST OF CELLS IN NEW COLUMN
			 * 
			 */
			
			
			for(int r = 1; r < gridRows; r++){
				
				// increment currentRow and currentID numbers for each square...
				currentRow++;
				currentID++;
				
				this.preSquare[0] = new Point(this.squares.get(currentID-1).getLowerLeft().getX() ,
											this.squares.get(currentID -1).getLowerLeft().getY());
				this.preSquare[1] = new Point(this.squares.get(currentID-1).getLowerRight().getX() ,
											this.squares.get(currentID -1).getLowerRight().getY());
				this.preSquare[2] = new Point(this.squares.get(currentID-1).getLowerRight().getX() ,
											this.squares.get(currentID -1).getLowerRight().getY() - this.gridResolution);
				this.preSquare[3] = new Point(this.squares.get(currentID - this.gridRows).getLowerRight().getX() ,
											this.squares.get(currentID -this.gridRows).getLowerRight().getY());

				// Call buildSquare() to get a Square object, and add it
				// as the next element to array list "squares".
				this.squares.add( buildSquare( this.preSquare, currentRow, currentColumn, currentID) );
				

			}
			
		}
		
	// end of buildSquareGrid()	
		
	}

	
    
	/*
	 * All squares of the grid created by this point!
	 * 
	 */
	
	
	// Providing a method to get the array list "squares" converted to an array list of polygons. 
	// We want this to use the Polygon class' isWithinPolygon() method (point-in-polygon test).
	// Loop through "squares", define a polygon for each Square, and add to a local array list of polygons, which the function returns. 
	
	public ArrayList<Polygon> getPolygonList() {
		  	  	
		ArrayList<Polygon> polygonList = new ArrayList();
	  	
	  	for (int i = 0; i < this.squares.size(); i++) {
	  		
	  		// Create a new polygon
	  		Polygon aPolygon = new Polygon();
	  		
	  		aPolygon.addPoint(this.squares.get(i).getUpperLeft());
	  		aPolygon.addPoint(this.squares.get(i).getUpperRight());
	  		aPolygon.addPoint(this.squares.get(i).getLowerRight());
	  		aPolygon.addPoint(this.squares.get(i).getLowerLeft());
	  		// closing point, same as first
	  		aPolygon.addPoint(this.squares.get(i).getUpperLeft());

	  		// Store the new polygon in the array list
	  		polygonList.add(aPolygon);
	  	}
	  	
	  	return polygonList;
	  }
	
		
	// calls Square class to build Square objects
	private Square buildSquare( Point[] pointArray, int theCurrentRow, int theCurrentColumn, int theCurrentID ) {
		
		Square aSquare = new Square(pointArray, theCurrentRow, theCurrentColumn, theCurrentID);
		
		return aSquare;
		
	}
	
	
}
