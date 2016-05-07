package com.peyto.athena.engine.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.peyto.athena.engine.math.HexUtils;
import com.peyto.athena.engine.math.OptimalHexagonCoordinates;

public class Board {
	private final FieldMap fieldMap;
	Map<Position, Unit> unitsOnBoard;
	Map<Integer, Position> unitPossitions = new HashMap<Integer, Position>();
	
	/**
	 * Creates simple map with all grass
	 * @param dimension
	 */
	public Board(int dimensionX, int dimensionY) {
		this.fieldMap = FieldMap.getEmptyFieldMap(dimensionX, dimensionY);
		FieldMap.serialize(fieldMap, "test.map");
		unitsOnBoard = new HashMap<Position, Unit>();
	}
	
	public Board(String fieldMapName) {
		this.fieldMap = FieldMap.deSerialize(fieldMapName);
		unitsOnBoard = new HashMap<Position, Unit>();
	}
	
	/**
	 * Set unit on the board in normalized coordinates
	 * @param unit
	 * @param x
	 * @param y
	 */
	public void setUnitOnBoard(Unit unit, int x, int y) {
		Position pos = new Position(x, y);
		unitsOnBoard.put(pos, unit);
		unitPossitions.put(unit.getId(), pos);
	}
	
	public int getSizeX() {
		return fieldMap.getSizeX();
	}
	
	public int getSizeY() {
		return fieldMap.getSizeY();
	}
	
	public String getFieldTypeWithPermission(int x, int y, String user) {
		// TODO Permission user to see
		FieldType ft = fieldMap.getFieldType(x, y);
		return ft.name();
	}
	
	public Unit getUnit(Position pos) {
		return unitsOnBoard.get(pos);
	}
	
	public Unit getUnit(int x, int y) {
		return getUnit(new Position(x, y));
	}
	
	public Position getCoordinates(int unitId) {
		return unitPossitions.get(unitId);
	}
	
	public Unit getUnit(int unitId) {
		Position p = getCoordinates(unitId);
		return getUnit(p);
	}
	
	private boolean checkFieldAvailableToMove(Position curPosition) {
		return getUnit(curPosition)==null;
	}
	
	private boolean checkOpponentTouchedOnMove(Unit unit, Position curPosition) {
		return isTouchedByOpponents(unit.getTeam(), curPosition);
	}
	

	private List<Position> stepRecursively(Unit unit, Position curPosition, int x, int y, int z) {
		List<Position> res = new ArrayList<Position>();
		
		// Check it's not initial
		// Otherwise we are not checking Cell
		Position initial = getCoordinates(unit.getId());
		if (curPosition.equals(initial)) {
			// Just add to path
			res.add(curPosition);
		} else {
			// Check condition - field is unavailable/occupied
			if (!checkFieldAvailableToMove(curPosition)) {
				return new ArrayList<Position>();
			}
		
			// If field is available - adding record
			res.add(curPosition);
		
			// Check conditions - TOUCH
			if (checkOpponentTouchedOnMove(unit, curPosition)) {
				return res;
			}
		} 
		
		// Check it's destination
		if (x==0 && y==0 && z==0) {
			// We found it
			return res;
		}
		
		int distanceToGo = Math.abs(x) + Math.abs(y) + Math.abs(z);
		
		List<Position> curSubPath = null;
		if (x!=0) {
			curSubPath = stepRecursively(unit, new Position(curPosition.getX() + HexUtils.sgn(x), curPosition.getY()), HexUtils.incrementToZero(x), y, z);
			if (curSubPath.size()==distanceToGo) {
				res.addAll(curSubPath);
				return res;
			}
		}
		List<Position> bestSubPath = curSubPath;
		if (y!=0) {
			curSubPath = stepRecursively(unit, new Position(curPosition.getX(), curPosition.getY()+HexUtils.sgn(y)), x, HexUtils.incrementToZero(y), z);
			if (curSubPath.size()==distanceToGo) {
				res.addAll(curSubPath);
				return res;
			} else if (bestSubPath==null || curSubPath.size()> bestSubPath.size()) {
				bestSubPath = curSubPath;
			}
		}
		if (z!=0) {
			curSubPath = stepRecursively(unit, new Position(curPosition.getX()+HexUtils.sgn(z), curPosition.getY() + HexUtils.sgn(z)), x, y, HexUtils.incrementToZero(z));
			if (curSubPath.size()==distanceToGo) {
				res.addAll(curSubPath);
				return res;
			} else if (bestSubPath==null || curSubPath.size()> bestSubPath.size()) {
				bestSubPath = curSubPath;
			}
		}
		if (bestSubPath!=null) {
			res.addAll(bestSubPath);
		}
		return res;
	}
	
	/**
	 * Make move step-by-step
	 * We always go in two directions, never in three
	 * We will be searching recursively: if any solution is success - we take it.
	 * If no solution is success - we take the longest
	 * Returns map of step - position
	 * @return Map of step -> Position
	 * First step is starting point, last - end point (until done or some 
	 * problem occured on the way)
	 */
	private Map<Integer, Position> makeMoveStepByStep(Unit unit, int x, int y) {
		Position initialPosition = getCoordinates(unit.getId());

		// We are looking for optimal path (x, y, z) => only two of three can be non-zero
		OptimalHexagonCoordinates opt = new OptimalHexagonCoordinates(x-initialPosition.getX(), y-initialPosition.getY(), 0);
		List<Position> resultPath = stepRecursively(unit, initialPosition, opt.getX(), opt.getY(), opt.getZ());
		
		// Repack to map
		Map<Integer, Position> result = new HashMap<Integer, Position>();
		for (int i=1; i<=resultPath.size(); i++) {
			result.put(i, resultPath.get(i-1));
		}
		return result;
	}
	
	
	/**
	 * As a result of step-by-step
	 * @param unit
	 * @param x
	 * @param y
	 */
	public Map<Integer, Position> moveUnit(Unit unit, int x, int y) {
		// Actual move
		Map<Integer, Position> animation = makeMoveStepByStep(unit, x, y);

		// Move on board
		Position old = getCoordinates(unit.getId());
		unitsOnBoard.remove(old);
		
		Position last = animation.get(animation.size());
		setUnitOnBoard(unit, last.getX(), last.getY());
		return animation;
	}
	
	public void removeDeadUnit(Unit unit) {
		Position old = getCoordinates(unit.getId());
		unitsOnBoard.remove(old);
		unitPossitions.remove(unit.getId());
	}
	
	@Deprecated
	public int flangsAttacked(Unit unit) {
		/*
		Position p = getCoordinates(unit.getId())
		int[] coord = new int[]{p.getX(), p.getY()};
		Unit left = (coord[0]>0?getUnit()[coord[0]-1][coord[1]]:null);
		Unit right = (coord[0]<size-1?unitsOnBoard[coord[0]+1][coord[1]]:null);
		Unit top = (coord[1]>0?unitsOnBoard[coord[0]][coord[1]-1]:null);
		Unit bottom = (coord[1]<size-1?unitsOnBoard[coord[0]][coord[1]+1]:null);
		
		int flangs = 0;
		if (left!=null && left.team!=unit.team) flangs++;
		if (right!=null && right.team!=unit.team) flangs++;
		if (top!=null && top.team!=unit.team) flangs++;
		if (bottom!=null && bottom.team!=unit.team) flangs++;
		
		return flangs++;
		*/
		return 0;
	}
	
	@Deprecated
	public double flangsDefended(Unit unit) {
		/*
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
		*/
		return 0;
	}
	
	@Deprecated
	/**
	 * Return number, corresponding to flank
	 * Acts as normal radius
	 * 0 = from x
	 * 1 = from -y
	 * 2 = from -z
	 * 3 = from -x
	 * 4 = from y
	 * 5 = from z 
	 * 
	 * -1 for archers
	 */
	public int getAttackingFlank(Unit attack, Unit defense) {
		Position cDef = getCoordinates(defense.getId());
		Position cAtt = getCoordinates(attack.getId());
		
		int normX = cAtt.getX() - cDef.getX();
		int normY = cAtt.getY() - cDef.getY();
		
		if (normX==1 && normY==0) return 0;
		else if (normX==-1 && normY==0) return 3;
		else if (normX==0 && normY==1) return 4;
		else if (normX==0 && normY==-1) return 1;
		else if (normX==1 && normY==1) return 5;
		else if (normX==-1 && normY==-1) return 2;
		else return -1;
	}
	
	private boolean opponentHere(Team ownTeam, int x, int y) {
		Unit u = getUnit(new Position(x, y));
		if (u!=null) return u.getTeam()!= ownTeam;
		else return false;
	}
	
	public boolean isTouchedByOpponents(Team ownTeam, Position p) {
		int touched = 0;
		if (opponentHere(ownTeam, p.getX()+1, p.getY())) touched++;
		if (opponentHere(ownTeam, p.getX(), p.getY()+1)) touched++;
		if (opponentHere(ownTeam, p.getX()+1, p.getY()+1)) touched++;
		if (opponentHere(ownTeam, p.getX()-1, p.getY())) touched++;
		if (opponentHere(ownTeam, p.getX(), p.getY()-1)) touched++;
		if (opponentHere(ownTeam, p.getX()-1, p.getY()-1)) touched++;
		return touched>0;
	}
	
	public boolean isTouchedByOpponents(Unit unit) {
		return isTouchedByOpponents(unit.getTeam(), getCoordinates(unit.getId()));

	}
}
