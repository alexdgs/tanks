package game;

import java.util.Comparator;

public class UnitComparator implements Comparator<Unit> {

	@Override
	public int compare(Unit u1, Unit u2) {
		if(u1.isFlagOn(Unit.Flag.CAN_FIRE)) {
			if(u2.isFlagOn(Unit.Flag.CAN_FIRE)) {
				if(u1.hitPoints > u2.hitPoints) return -1;
				else return 1;
			} else return 1;
		} else if(u2.isFlagOn(Unit.Flag.CAN_FIRE)) return -1;
		else {
			if(u1.hitPoints > u2.hitPoints) return -1;
			else return 1;
		}
	}

}
