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
        if (amount < 0 && -amount > self.money) {
            return false;
        }
        self.modMoney(amount);
        return true;
    }

    @Override public String toString() {
        return "MoneyModEffect{" + "amount=" + amount + '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MoneyModEffect that = (MoneyModEffect) o;

        return amount == that.amount;

    }

    @Override public int hashCode() {
        return amount;
    }
    
    public int getAmount() {
        return amount;
    }
}
