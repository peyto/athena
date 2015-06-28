function UnitType(unitType, speed, attackInf, attackCav, attBow, defense, endurance) {
	this.unitType = unitType; 
	this.speed = speed;
	this.attackInf = attackInf; 
	this.attackCav = attackCav; 
	this.attBow = attBow; 
	this.defense = defense;
	this.endurance = endurance;
}

function UnitTypeCollection() {
	this.unitType = {};
	
	this.addUnitType = function (unitType, speed, attackInf, attackCav, attBow, defense, endurance) {
		this.unitType[unitType] = new UnitType(unitType, speed, attackInf, attackCav, attBow, defense, endurance);
	};
	
	this.getUnit = function (unitType) {
		return this.unitType[unitType];
	};
}

function Unit(id, team, unitType, units, morale, actions, canAttack, rangeMin, rangeMax, imgSrc, name) {
	this.id = id;
	this.team = team;
	this.unitType = unitType;
	this.units = units;
	this.actions = actions;
	this.canAttack = canAttack;
	this.morale = morale;
	this.rangeMin = rangeMin;
	this.rangeMax = rangeMax;
	this.imgSrc = imgSrc;
	this.name = name;
	
	this.update = function (currentUnits, currentMorale, curActions, canAttack) {
		this.units = currentUnits;
		this.actions = curActions;
		this.morale = currentMorale;
		this.canAttack = canAttack;
	};
	
	this.updateActions = function (curActions) {
		this.actions = curActions;
	};
	
}

function Team(id) {
	this.id = id;
	this.units = {};
	
	this.addUnit = function (unitId, unitType, units, morale, rangeMin, rangeMax, imgSrc, name) {
		this.units[unitId] = new Unit(unitId, this.id, unitType, units, morale, 0, false, rangeMin, rangeMax, imgSrc, name);
	};
	
	this.removeUnit = function (unitId) {
		this.units[unitId] = null;
	};
	
	this.getUnit = function (unitId) {
		return this.units[unitId];
	};
}

function calculateHexDistance(deltaX, deltaY) {
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