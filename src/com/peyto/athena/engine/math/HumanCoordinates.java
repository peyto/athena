package ru.kma8794.athena.engine.math;

import ru.kma8794.athena.engine.entity.Position;

public class HumanCoordinates {
	private Position position;
	
	public HumanCoordinates(int x, int y) {
		super();
		position = new Position(x, y);
	}
	
	public int getX() {
		return position.getX();
	}
	public int getY() {
		return position.getY();
	}
	
	public Position getPosition() {
		return position;
	}
	
	
}
