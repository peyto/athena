package com.peyto.athena.engine.entity;

public enum UnitType {
	
	// max is 20, all values are relative!
	//           speed  range    attack    def  morale units                                           
  //Clubs       (1, 7,  1, 1,   6,  3, 0,   3,  15, 4,  100, "club"),
	Rebels      (1, 7,  1,1,    6,  3, 0,   3,  16,  7, 100, UnitTypeHorse.Infantry, "rebel"),
	Pikes       (1, 6,  1,1,    7, 13, 0,   5,  10, 10, 100, UnitTypeHorse.Infantry, "pike"),
	Swords      (1, 6,  1,1,   10,  7, 0,   8,  10, 10, 100, UnitTypeHorse.Infantry, "sword"),
	Phalanx     (1, 4,  1,1,    8, 10, 0,   8,  15, 15, 100, UnitTypeHorse.Infantry, "phalanx"),
	HeavySwords (1, 5,  1,1,   14, 10, 0,  14,  15, 12,  80, UnitTypeHorse.Infantry, "hsword"),
	Archers     (1, 6,  12, 4,  3,  1, 9,   3,  10, 10, 100, UnitTypeHorse.Infantry, "archer"),
	HorseArchers(1, 14, 6, 3,   9,  5, 8,   6,  16, 12,  60, UnitTypeHorse.Cavalary, "horse_archer"),
	Horses      (1, 16, 1,1,   12, 10, 0,   8,  15, 13,  80, UnitTypeHorse.Cavalary, "horse"),
	Knights     (1, 12, 1,1,   16, 14, 0,  14,  18, 18,  60, UnitTypeHorse.Cavalary, "knight");
	
	
	
	// 1 - infantry, 2 - archer
	int type; 
	UnitTypeHorse unitTypeHorse;
	int speed; 
	
	int rangeMin;
	int rangeMax;
	
	int attackInfantry; 
	int attackHorse; 
	int attackBows;
	
	int defenseArmor; 
	
	int moraleBase; 
	int moraleEndurance;
	
	int units;
	String img;
	
	private UnitType(int type, int speed, int rangeMax, int rangeMin, int attackInfantry, int attackHorse, int attackBows, int defenseArmor, int moraleBase, int moraleEndurance, int units, UnitTypeHorse unitTypeHorse, String img) {
		this.type = type;
		this.speed = speed;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		
		this.attackInfantry = attackInfantry;
		this.attackHorse = attackHorse;
		this.attackBows = attackBows;
		
		this.defenseArmor = defenseArmor;
		
		this.moraleBase = moraleBase;
		this.moraleEndurance = moraleEndurance;
		
		this.units = units;
		this.unitTypeHorse = unitTypeHorse;
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

	public int getRangeMin() {
		return rangeMin;
	}
	
	public int getRangeMax() {
		return rangeMax;
	}

	public int getAttackInfantry() {
		return attackInfantry;
	}

	public int getAttackHorse() {
		return attackHorse;
	}
	
	public int getAttackBows() {
		return attackBows;
	}

	public int getDefenseArmor() {
		return defenseArmor;
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
	
	public UnitTypeHorse getUnitTypeHorse() {
		return unitTypeHorse;
	}
	
	
	
	
}
