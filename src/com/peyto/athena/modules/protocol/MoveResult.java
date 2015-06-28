package com.peyto.athena.modules.protocol;

import java.util.Map;

import com.peyto.athena.engine.entity.Position;

public class MoveResult extends ActiveAction{
	
	int unit_id;
	int old_x;
	int old_y;	
	int new_x;
	int new_y;
	
	Map<Integer, Position> animation;
	
	double actions_left;

	public MoveResult(String result, int unit_id, int old_x, int old_y, int new_x, int new_y, double actions_left, Map<Integer, Position> animation) {
		super(result);
		this.unit_id = unit_id;
		this.old_x = old_x;
		this.old_y = old_y;
		this.new_x = new_x;
		this.new_y = new_y;
		this.actions_left = actions_left;
		this.animation = animation;
	}
	
	public MoveResult(String result) {
		super(result);
	}
}
