package ru.kma8794.athena.engine.math;

public class NormalizedHexagonCoordinates extends HexagonCoordinates{
	
	public NormalizedHexagonCoordinates(int x, int y, int z) {
		super(x, y, z);
		normalizeToXY();
	}
	
	private synchronized void normalizeToXY() {
		x = x + z;
		y = y + z;
		z = 0;
	}
}
