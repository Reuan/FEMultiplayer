package net.fe.fightStage;

import net.fe.RNG;
import net.fe.unit.Unit;

public class Miracle extends CombatTrigger {
	
	public Miracle() {
		super(APPEND_NAME_AFTER_MOD, ENEMY_TURN_MOD);
	}

	@Override
	public boolean attempt(Unit user) {
		return RNG.get() < user.get("Lck");
	}
	
	@Override
	public int runDamageMod(Unit a, Unit d, int damage){
		if(d.getHp() - damage <= 0){
			return d.getHp() - 1;
		} else {
			return damage;
		}
	}

}
