package nightgames.characters.custom.effect;

import nightgames.characters.Character;
import nightgames.combat.Combat;

public class MoneyModEffect implements CustomEffect {
	int amount;

	public MoneyModEffect(int amount) {
		this.amount = amount;
	}

	@Override
	public boolean execute(Combat c, Character self, Character other) {
		if (self.money < amount) {
			return false;
		}
		self.modMoney(amount);
		return true;
	}
}
