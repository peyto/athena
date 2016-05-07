package com.peyto.athena.engine.math;

public class OptimalHexagonCoordinates extends HexagonCoordinates {
	
	public OptimalHexagonCoordinates(int x, int y, int z) {
		super(x, y, z);
		normalizeToXY();
		optimize();
	}
	
	private synchronized void normalizeToXY() {
		x = x + z;
		y = y + z;
		z = 0;
	}
	
	private synchronized void optimize() {
		// If we can make z = x + y
		if (HexUtils.sgn(x) * HexUtils.sgn(y) > 0) {
			if (HexUtils.sgn(x) > 0) {
				z = Math.min(x, y);
				x = x - z;
				y = y - z;
			} else {
				z = Math.max(x, y);
				x = x - z;
				y = y - z;
			}
		}
	}
}
