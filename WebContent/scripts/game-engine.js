function Unit(id, team, units, morale, actions, canAttack, range) {
	this.id = id;
	this.team = team;
	this.units = units;
	this.actions = actions;
	this.canAttack = canAttack;
	this.morale = morale;
	this.range = range;
	
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
	
	this.addUnit = function (unitId, range) {
		this.units[unitId] = new Unit(unitId, this.id, 100, 0, 0, false, range);
	};
	
	this.removeUnit = function (unitId) {
		this.units[unitId] = null;
	};
	
	this.getUnit = function (unitId) {
		return this.units[unitId];
	};
}