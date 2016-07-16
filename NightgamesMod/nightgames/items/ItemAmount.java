package nightgames.items;

public class ItemAmount {
    public Item item;
    public int amount;

    public ItemAmount(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ItemAmount that = (ItemAmount) o;

        return amount == that.amount && item == that.item;

    }

    @Override public int hashCode() {
        int result = item.hashCode();
        result = 31 * result + amount;
        return result;
    }
}
