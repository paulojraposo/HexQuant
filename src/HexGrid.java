
/*
 * Hexagonal tessellation set up by first calculating centroids for hexagons.  
 * Then, each hexagon constructs its vertices with the class Hexagon 
 * using x and y shift values from the centroid coordinates.
 * 
 */

import java.util.ArrayList;


public class HexGrid {
 
  double hexRadius;                  // radius measured center to vertex
  double inRadius;                   // radius measured center to side (inscribed)
  
  Point upperLeft;                   // bounding box for the underlying data
  Point bottomRight;                 //
  
  int gridRows;                      // number of rows in the grid
  int gridCols;                      // number of columns in the grid
  
  Hexagon[] hexagons;
  
  
  // Constructor
  HexGrid (Point uLeft, Point bRight, double hexagonInscribedDiameter) {
    
    // Calculate the bounding box
    
    double deltaX = bRight.getX() - uLeft.getX();
    double deltaY = uLeft.getY() - bRight.getY();
    
    // Building in computer coordinates, not Cartesian - transformed later.
    this.upperLeft   = new Point(0.0, 0.0);
    this.bottomRight = new Point(deltaX, deltaY);
    
    // Calculate the hexagon radii
    this.inRadius  = hexagonInscribedDiameter / 2;
    this.hexRadius = hexagonInscribedDiameter / Math.sqrt(3);
    
    //println("Hexagon radius is " + this.hexRadius);
    //println("Inscribed radius is " + this.inRadius);
    
    // Calculate the number of rows and columns in the grid
    double boxWidth  = this.bottomRight.getX() - this.upperLeft.getX();
    double boxHeight = this.bottomRight.getY() - this.upperLeft.getY();
    
    double hexWidth  = this.hexRadius * 3 / 2;
    double hexHeight = this.inRadius * 2;
    
    double xMod = (boxWidth) % (hexWidth);
    double yMod = (boxHeight) % (hexHeight);
    
    int xExtra  = (xMod < this.hexRadius) ? 2 : 3;
    int yExtra  = (yMod < this.inRadius) ? 1 : 2;
    
    Double xFloor = Math.floor(boxWidth / hexWidth);
    Double yFloor = Math.floor(boxHeight / hexHeight);
    
    this.gridCols = xFloor.intValue() + xExtra;
    this.gridRows = yFloor.intValue() + yExtra;
    
    //println("HexGrid has " + this.gridRows + " rows and " + this.gridCols + " columns");
    
    
    
    // Calculate and lay out the hexagons
    this.addHexagons();
    
    
    
    
    // Transform coordinates
    
    int hexCount  = this.gridRows * this.gridCols;
    
    double xShift = uLeft.getX();
    double yShift = uLeft.getY();
    
    // Transform points
    for (int i = 0; i < hexCount; i++) {
      for (int j = 0; j < 6; j++) {
      
        double xCoord = this.hexagons[i].getPt(j).getX();
        double yCoord = this.hexagons[i].getPt(j).getY();

        this.hexagons[i].getPt(j).setX(xCoord + xShift);
        this.hexagons[i].getPt(j).setY(-yCoord + yShift);
      }
    }
    
    // Transform centroids
    for (int i = 0; i < hexCount; i++) {
        
          double xCoord = this.hexagons[i].getX();
          double yCoord = this.hexagons[i].getY();

          this.hexagons[i].setX(xCoord + xShift);
          this.hexagons[i].setY(-yCoord + yShift);
    }
    
  }
  
  
  public ArrayList<Polygon> getPolygonList() {
  
  	// Convert the hexagons to polygons
  	
	ArrayList<Polygon> polygonList = new ArrayList();
	
  	int hexCount = this.gridRows * this.gridCols;
  	
  	for (int i = 0; i < hexCount; i++) {
  		
  		// Create a new polygon
  		Polygon aPolygon = new Polygon();
  		
  		for (int j = 0; j < 6; j++) {
  		
  			// Copy points from hexagon to the new polygon
  			aPolygon.addPoint(this.hexagons[i].getPt(j));
  		}
  		
  		// Close the polygon
  		aPolygon.addPoint(this.hexagons[i].getPt(0));
  		
  		// Store the new polygon
  		polygonList.add(aPolygon);
  	}
  	
  	return polygonList;
  }
  
  public Point[] getCentroidList() {
	    	
	  	int hexCount = this.gridRows * this.gridCols;
	  	Point[] centroidList = new Point[hexCount];
	  	
	  	for (int i = 0; i < hexCount; i++) {
	  		  		
	  		centroidList[i] = new Point(this.hexagons[i].getX(), this.hexagons[i].getY());
	  	}
	  	
	  	return centroidList;
  }
  
  
  void addHexagons () {
    
    
    // Calculate the coordinate shifts for vertices
    
      // Axis x is pointing to the right, axis y is pointing straight down.
      // Counting points clockwise starting from the rightmost one, 
      // store the x and y coordinate shifts for each point.
    
    double[] xShift = new double[6];
    double[] yShift = new double[6];
    
    xShift[0] = this.hexRadius;
    xShift[1] = this.hexRadius / 2;
    xShift[2] = - this.hexRadius / 2;
    xShift[3] = - this.hexRadius;
    xShift[4] = - this.hexRadius / 2;
    xShift[5] = this.hexRadius / 2;
    
    yShift[0] = 0.0;
    yShift[1] = this.inRadius;
    yShift[2] = this.inRadius;
    yShift[3] = 0.0;
    yShift[4] = - this.inRadius;
    yShift[5] = - this.inRadius;
    
    
    
    
    // Generate and lay out the hexagons
    
    double currentX;
    double currentY;
    
    double xStep = this.hexRadius * 3 / 2;
    double yStep = this.inRadius * 2;
    
    Hexagon[] currentColumn;
    Hexagon[] previousColumn;
    
    this.hexagons = new Hexagon[this.gridRows * this.gridCols];
    
    int currentId = 0;
    
    
    
    
    // 1. Create the first column
    
    // Initialize the current column
    currentColumn = new Hexagon[this.gridRows];

    // Set the x coordinate of the current column
    currentX      = this.upperLeft.getX();
    
    for (int i = 0; i < this.gridRows; i++) {
      
      // Set the y coordinate of the current hexagon
      currentY = this.upperLeft.getY() + yStep * i;
      
      // Create a new hexagon
      Hexagon currentHex = new Hexagon(currentX, currentY, this.hexRadius, xShift, yShift, currentId);

      if (i > 0) {
        
        // borrow points from the hexagon above, if any
        currentHex.setPt(4, currentColumn[i - 1].getPt(2));
        currentHex.setPt(5, currentColumn[i - 1].getPt(1));
      }
      
      // Update the hexagon arrays
      currentColumn[i] = currentHex;
      this.hexagons[i] = currentHex;
      
      currentId++;
      
    }
    
    // Store the current column
    previousColumn = currentColumn;
    
    
    
    
    // 2. Create the rest of the columns
    
    for (int j = 1; j < this.gridCols; j++) {
      
      // Initialize the current column
      currentColumn = new Hexagon[this.gridRows];
      
      // Determine if the column is even or odd
      boolean odd = ((j % 2) > 0) ? true : false ;
      
      // Set the x coordinate of the current column
      currentX = this.upperLeft.getX() + xStep * j;
      
      // For each row of the current column, ..
      for (int i = 0; i < this.gridRows; i++) {
        
        // Set the y coordinate of the current hexagon, shift odd lines down
        currentY = (odd) ? (this.upperLeft.getY() + yStep * i + this.inRadius) : (this.upperLeft.getY() + yStep * i);
        
        // Create a new hexagon
        Hexagon currentHex = new Hexagon(currentX, currentY, this.hexRadius, xShift, yShift, currentId);
        
        // If this is an odd column, ...
        if (odd) {
          
          // ... borrow points from the hexagon on the lower left, if any
          if (i < this.gridRows - 1) {
            currentHex.setPt(2, previousColumn[i + 1].getPt(0));
          }
          
          // ... borrow points from the hexagon on the upper left
          currentHex.setPt(3, previousColumn[i].getPt(1));
          currentHex.setPt(4, previousColumn[i].getPt(0));
          
          // ... borrow points from the hexagon above, if any
          if (i > 0) {
            currentHex.setPt(5, currentColumn[i - 1].getPt(1));
          }
          
          // The last condition can be combined with the first if the hexagons 
          // are constructed in an upwards direction for odd columns.
          
        } else {
          
          // else, if it's an even column, ...
          
          // ... borrow points from the hexagon on the lower left
          currentHex.setPt(2, previousColumn[i].getPt(0));
          currentHex.setPt(3, previousColumn[i].getPt(5));
          
          // ... borrow points from the hexagons on the upper left and above, if any
          if (i > 0) {
            
            currentHex.setPt(4, previousColumn[i - 1].getPt(0));  // upper left
            currentHex.setPt(5, currentColumn[i - 1].getPt(1));   // above
          }          
          
        }
        
        // Update the hexagon arrays
        currentColumn[i]                     = currentHex;
        this.hexagons[this.gridRows * j + i] = currentHex;
        
        currentId++;
        
      }
      
      // Store the current column
      previousColumn = currentColumn;
      
    }
    
    // end of addHexagons()
  }

  
  
  // end of HexGrid
}

