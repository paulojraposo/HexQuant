
public class BoundingBox {
	
	/**
	 * 		Properties
	 */
	
	private Point northwestPoint; 
	private Point southeastPoint;
	
	
	
	/**
	 * 		Methods
	 */
	
	
	// Public methods
	
	public void setBox(Point firstPoint, Point secondPoint){
				
		Point[] mangledPoints = manglePoints(firstPoint, secondPoint);
		
		this.northwestPoint = mangledPoints[0];
		this.southeastPoint = mangledPoints[1];
		
	}
	
	
	public void setBox(Double nwX, Double nwY, Double seX, Double seY){
		
		Point firstPoint = new Point(nwX, nwY);
		Point secondPoint = new Point(seX, seY);
		
		setBox(firstPoint, secondPoint);
	}
		

	public Boolean containsPoint(Point subjectPoint){
		
		Boolean contains = false;
		
		if( 	   (subjectPoint.getX() > this.northwestPoint.getX() )
				&& (subjectPoint.getX() < this.southeastPoint.getX() )
				&& (subjectPoint.getY() < this.northwestPoint.getY() )
				&& (subjectPoint.getY() > this.southeastPoint.getY() ) ) {
			
			contains = true;
		}
		
		return contains;
		
	}
	
	public Boolean completelyContainsPolyline(Polyline subjectPolyline){
		
		Boolean contains = true;
		
		Point[] points = subjectPolyline.convertToPoints();
		
		if (points.length == 0){
			contains = false;
			
		}else{
		
			for(int i=0; i < points.length -1; i++){
				
				Boolean containsThisPoint = containsPoint(points[i]);
				
				if (containsThisPoint == false){
					contains = false;
					break;
				}
			}
		}
		
		return contains;
	}
	
	private Point[] manglePoints(Point firstPoint, Point secondPoint){
		
		double minX = Math.min(firstPoint.getX(), secondPoint.getX());
		double maxX = Math.max(firstPoint.getX(), secondPoint.getX());
		double minY = Math.min(firstPoint.getY(), secondPoint.getY());
		double maxY = Math.max(firstPoint.getY(), secondPoint.getY());
		
		
		Point[] mangledPoints = {new Point(minX, maxY), new Point(maxX, minY)};
		
		return mangledPoints;
	}
	
}
