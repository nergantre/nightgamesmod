package nightgames.creator.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import nightgames.items.Item;

public class ItemAmount {

	private ObjectProperty<Item> item;
	private IntegerProperty amount;

	public ItemAmount() {
		this(null, 0);
	}

	public ItemAmount(nightgames.items.ItemAmount base) {
		this(base.item, base.amount);
	}
	
	public ItemAmount(Item item, int amount) {
		this.item = new SimpleObjectProperty<>(item);
		this.amount = new SimpleIntegerProperty(amount);
	}

	public ObjectProperty<Item> itemProperty() {
		return this.item;
	}

	public nightgames.items.Item getItem() {
		return this.itemProperty().get();
	}

	public void setItem(final nightgames.items.Item item) {
		this.itemProperty().set(item);
	}

	public IntegerProperty amountProperty() {
		return this.amount;
	}

	public int getAmount() {
		return this.amountProperty().get();
	}

	public void setAmount(final int amount) {
		this.amountProperty().set(amount);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemAmount other = (ItemAmount) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return item.get() + ": " + amount.get();
	}

}
