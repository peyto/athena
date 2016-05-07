package com.peyto.athena.engine.math;

public class HexUtils {
	public static HumanCoordinates convert(OptimalHexagonCoordinates h) {
		return null;
	}
	
	public static NormalizedHexagonCoordinates convertToNormalized(HumanCoordinates h) {
		if (h.getY()%2==0) {
			return new NormalizedHexagonCoordinates(h.getX(), h.getY()/2, h.getY()/2);
		} else {
			return new NormalizedHexagonCoordinates(h.getX(), h.getY()/2 , h.getY()/2 + 1);
		}
	}
	
	public static NormalizedHexagonCoordinates convertToNormalized(int x, int y) {
		return convertToNormalized(new HumanCoordinates(x, y));
	}
	
	public static OptimalHexagonCoordinates convertToOptimal(HumanCoordinates h) {
		if (h.getY()%2==0) {
			return new OptimalHexagonCoordinates(h.getX(), h.getY()/2, h.getY()/2);
		} else {
			return new OptimalHexagonCoordinates(h.getX(), h.getY()/2 , h.getY()/2 + 1);
		}
	}
	
	public static int calculateDistance(NormalizedHexagonCoordinates p1, NormalizedHexagonCoordinates p2) {
		OptimalHexagonCoordinates distance = new OptimalHexagonCoordinates(p2.getX()-p1.getX(), 
				p2.getY() - p1.getY(),
				p2.getZ() - p1.getZ());
		return Math.abs(distance.getX()) + Math.abs(distance.getY()) + Math.abs(distance.getZ());
	}
	
	public static int calculateHexDistance(int deltaX, int deltaY) {
		// -1*0 == 0 => we have different sign
		// In if we ask if it's the same sign
		if ((deltaX > 0 && deltaY > 0) || (deltaX < 0 && deltaY < 0)) {
			// x = x-z, y = y - z, result = |x|+|y|+|z| = |x-z| + |y-z| + |z|
			if (deltaX > 0) {
				return Math.abs(deltaX) + Math.abs(deltaY) - Math.min(deltaX, deltaY);
			} else {
				return Math.abs(deltaX) + Math.abs(deltaY) + Math.max(deltaX, deltaY);
			}
		}
		return Math.abs(deltaX) + Math.abs(deltaY);
	}
	
	public static int sgn(int i) {
		if (i>0) return 1;
		else if (i<0) return -1;
		else return 0;
	}
	
	public static int incrementToZero(int i) {
		if (i>0) return i-1;
		else if (i<0) return i+1;
		else return 0;
		
	}
}
