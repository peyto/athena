package com.peyto.athena.engine.entity;

public enum UnitType {
	
	// max is 20, all values are relative!
	
	Clubs(1, 9, 1,  6, 5,  4, 3, 5,  15, 4, 100, "images/club.png"),
	Rebels(1, 8, 1,  5, 4,  5, 5, 4,  18, 8, 100, "images/rebel.png"),
	
	Swords(1, 7, 1,  12, 8,  9, 7, 7,  12, 10, 100, "images/sword.png"),
	Pikes (1, 7, 1,  8, 11,  7, 13, 7,  12, 10, 100, "images/pike.png"),
	
	HeavySwords(1, 5, 1,  16, 14,  12, 10, 15,  16, 12, 100, "images/hsword1.png"),
	Phalanx    (1, 4, 1,  10, 12,  15, 18, 18,  15, 15, 100, "images/phalanx.png"),
	
	Archers    (1, 7, 12,  8, 8,  3, 1, 4,  10, 10, 100, "images/bow1.png"),
	HorseArchers(1, 18, 5,  9, 9,  8, 12, 6,  10, 10, 100, "images/1"),
	
	Horses(1, 20, 1,  12, 10,  8, 10, 8,  15, 12, 100, "images/kazak2.png"),
	
	Knights(1, 14, 1,  16, 14,  12, 10, 15,  20, 18, 100, "images/knight1.png");
	
	
	
	// 1 - infantry, 2 - archer
	int type; 
	int speed; 
	int range;
	
	int attackInfantry; 
	int attackHorse; 
	
	int defenseInfantry; 
	int defHorse;
	int defArchers;
	
	int moraleBase; 
	int moraleEndurance;
	
	int units;
	String img;
	
	private UnitType(int type, int speed, int range, int attackInfantry, int attackHorse, int defenseInfantry, int defHorse, int defArchers, int moraleBase, int moraleEndurance, int units, String img) {
		this.type = type;
		this.speed = speed;
		this.range = range;
		this.attackInfantry = attackInfantry;
		this.attackHorse = attackHorse;
		this.defenseInfantry = defenseInfantry;
		this.defHorse = defHorse;
		this.defArchers = defArchers;
		this.moraleBase = moraleBase;
		this.moraleEndurance = moraleEndurance;
		this.units = units;
		this.img = img;
	} 
	
	public String getImg() {
		return img;
	}

	public int getType() {
		return type;
	}

	public int getSpeed() {
		return speed;
	}

	public int getRange() {
		return range;
	}

	public int getAttackInfantry() {
		return attackInfantry;
	}

	public int getAttackHorse() {
		return attackHorse;
	}

	public int getDefenseInfantry() {
		return defenseInfantry;
	}

	public int getDefHorse() {
		return defHorse;
	}

	public int getDefArchers() {
		return defArchers;
	}

	public int getMoraleBase() {
		return moraleBase;
	}

	public int getMoraleEndurance() {
		return moraleEndurance;
	}

	public int getUnits() {
		return units;
	}
	
	
	
	
	
}
