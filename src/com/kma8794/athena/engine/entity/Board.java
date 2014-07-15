package com.kma8794.athena.engine.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
	int size;
	Unit[][] unitsOnBoard;
	Map<Integer, int[]> unitPossitions = new HashMap<Integer, int[]>();
	
	public Board(int dimension) {
		this.size = dimension;
		unitsOnBoard = new Unit[dimension][dimension];
	}
	
	public void setUnitOnBoard(Unit unit, int x, int y) {
		unitsOnBoard[x][y] = unit;
		unitPossitions.put(unit.getId(), new int[] {x,y});
	}
	
	public int getSize() {
		return size;
	}
	
	
	public Unit getUnit(int x, int y) {
		return unitsOnBoard[x][y];
	}
	
	public int[] getCoordinates(int unitId) {
		return unitPossitions.get(unitId);
	}
	
	public Unit getUnit(int unitId) {
		int[] a = getCoordinates(unitId);
		return unitsOnBoard[a[0]][a[1]];
	}
	
	/*
	class PathPoint {}
	
	class SimplePoint extends PathPoint {
		int x;
		int y;
		public SimplePoint(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
	}
	
	class MiddlePoint extends PathPoint {
		int fromX;
		int fromY;
		
		int toX;
		int toY;
	}
	
	private List<PathPoint> generatePath(int x0, int y0, int x1, int y1) {
		List<PathPoint> result = new ArrayList<Board.PathPoint>();
		if (x1==x0) {
			for (int j = 0; j<=Math.abs(y1-y0); j++) {
				result.add(new SimplePoint(x0, y0+j*(int)Math.signum(y1-y0)));
			}
			return result;
		} else {
			double tg = ((double)(y1-y0))/(x1-x0);
			int xprev = x0;
			for (int i = 0; i<=Math.abs(x1-x0); i++) {
				double xnext = xright
				double xright = x0 + (double)1/2 + i*(int)Math.signum(x1-x0);
				double yi = tg * (i-x0 + 1/2) + y0;
				
			}
		}
		
	}
	*/
	
	/**
	 * 1. Move step by step
	 * 2. If everything is free - move diagonal
	 * 
	 * @param unit
	 * @param x
	 * @param y
	 * @return
	 */
	public void moveUnit(Unit unit, int x, int y) {
		// TODO Check that it's free and throw Exception
		
		
		
		
		
		int[] old = getCoordinates(unit.getId());
		unitsOnBoard[old[0]][old[1]] = null;
		
		unitsOnBoard[x][y] = unit;
		unitPossitions.put(unit.getId(), new int[] {x,y});
		
	}
	
	public void removeDeadUnit(Unit unit) {
		int[] old = getCoordinates(unit.getId());
		unitsOnBoard[old[0]][old[1]] = null;
		unitPossitions.remove(unit.getId());
	}
	
	@Deprecated
	public int flangsAttacked(Unit unit) {
		int[] coord = getCoordinates(unit.getId());
		Unit left = (coord[0]>0?unitsOnBoard[coord[0]-1][coord[1]]:null);
		Unit right = (coord[0]<size-1?unitsOnBoard[coord[0]+1][coord[1]]:null);
		Unit top = (coord[1]>0?unitsOnBoard[coord[0]][coord[1]-1]:null);
		Unit bottom = (coord[1]<size-1?unitsOnBoard[coord[0]][coord[1]+1]:null);
		
		int flangs = 0;
		if (left!=null && left.team!=unit.team) flangs++;
		if (right!=null && right.team!=unit.team) flangs++;
		if (top!=null && top.team!=unit.team) flangs++;
		if (bottom!=null && bottom.team!=unit.team) flangs++;
		
		return flangs++;
	}
	
	public double flangsDefended(Unit unit) {
		int[] coord = getCoordinates(unit.getId());
		Unit left = (coord[0]>0?unitsOnBoard[coord[0]-1][coord[1]]:null);
		Unit right = (coord[0]<size-1?unitsOnBoard[coord[0]+1][coord[1]]:null);
		Unit top = (coord[1]>0?unitsOnBoard[coord[0]][coord[1]-1]:null);
		Unit bottom = (coord[1]<size-1?unitsOnBoard[coord[0]][coord[1]+1]:null);
		
		double flangs = 0;
		if (left!=null && left.team==unit.team) flangs+=left.getCurrentUnits();
		if (right!=null && right.team==unit.team) flangs+=right.getCurrentUnits();
		if (top!=null && top.team==unit.team) flangs+=top.getCurrentUnits();
		if (bottom!=null && bottom.team==unit.team) flangs+=bottom.getCurrentUnits();
		
		return flangs/100;
	}
	
	public int getAttackingFlank(Unit attack, Unit defense) {
		int[] coord = getCoordinates(defense.getId());
		Unit left = (coord[0]>0?unitsOnBoard[coord[0]-1][coord[1]]:null);
		Unit right = (coord[0]<size-1?unitsOnBoard[coord[0]+1][coord[1]]:null);
		Unit top = (coord[1]>0?unitsOnBoard[coord[0]][coord[1]-1]:null);
		Unit bottom = (coord[1]<size-1?unitsOnBoard[coord[0]][coord[1]+1]:null);
		
		if (top!=null && top==attack) return 0;
		if (right!=null && right==attack) return 1;
		if (bottom!=null && bottom==attack) return 2;
		if (left!=null && left==attack) return 3;
		
		return 0;
	}
	
	
	public boolean isTouchedByOpponents(Unit unit) {
		return flangsAttacked(unit)>0;
	}
}
