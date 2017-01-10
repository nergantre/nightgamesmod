package nightgames.creator.model;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import nightgames.global.Global;
import nightgames.skills.Wait;
import nightgames.stance.Stance;
import nightgames.status.Stsflag;

public abstract class AiMod<T> {

	protected final ReadOnlyStringProperty name;
	private ObjectProperty<T> value;
	private DoubleProperty weight;

	protected AiMod(String name, T initial) {
		this.name = new ReadOnlyStringWrapper(name);
		value = new SimpleObjectProperty<>(initial);
		weight = new SimpleDoubleProperty();
	}

	public abstract Set<T> getUniverse();

	public abstract T fromString(String str);
	
	public abstract String valueAsString();

	public ReadOnlyStringProperty nameProperty() {
		return this.name;
	}

	public java.lang.String getName() {
		return this.nameProperty().get();
	}

	public ObjectProperty<T> valueProperty() {
		return this.value;
	}

	public T getValue() {
		return this.valueProperty().get();
	}

	public void setValue(final T value) {
		this.valueProperty().set(value);
	}

	public DoubleProperty weightProperty() {
		return this.weight;
	}

	public double getWeight() {
		return this.weightProperty().get();
	}

	public void setWeight(final double weight) {
		this.weightProperty().set(weight);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
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
		AiMod<?> other = (AiMod<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Type: " + name.get() + " // Value: " + valueAsString() + " // Weight: " + weight.get();
	}

	public enum Type {
		SKILL(Skill::new), POSITION(Position::new), SELF_STAT(SelfStatus::new), OPP_STAT(OpponentStatus::new);

		private final Supplier<AiMod<?>> ctor;

		private Type(Supplier<AiMod<?>> ctor) {
			this.ctor = ctor;
		}

		public AiMod<?> build() {
			return ctor.get();
		}
	}

	public static class Skill extends AiMod<Class<? extends nightgames.skills.Skill>> {

		public Skill() {
			super("skill", Wait.class);
		}

		public Skill(Class<? extends nightgames.skills.Skill> skill, double weight) {
			this();
			setValue(skill);
			setWeight(weight);
		}

		@Override
		public Set<Class<? extends nightgames.skills.Skill>> getUniverse() {
			return Global.getSkillPool().stream().map(o -> (Class<? extends nightgames.skills.Skill>) o.getClass())
					.collect(Collectors.toSet());
		}

		@SuppressWarnings("unchecked")
		@Override
		public Class<? extends nightgames.skills.Skill> fromString(String str) {
			str = str.replaceAll("class ", "");
			try {
				return (Class<? extends nightgames.skills.Skill>) Class.forName(str);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}

		@Override
		public String valueAsString() {
			return getValue().getCanonicalName();
		}

	}

	public static class Position extends AiMod<Stance> {

		public Position() {
			super("position", Stance.neutral);
		}

		public Position(Stance stance, double weight) {
			this();
			setValue(stance);
			setWeight(weight);
		}

		@Override
		public Set<Stance> getUniverse() {
			return EnumSet.allOf(Stance.class);
		}

		@Override
		public Stance fromString(String str) {
			return Stance.valueOf(str);
		}

		@Override
		public String valueAsString() {
			return getValue().name();
		}

	}

	public static class SelfStatus extends AiMod<Stsflag> {

		public SelfStatus() {
			super("self-status", Stsflag.braced);
		}

		public SelfStatus(Stsflag flag, double weight) {
			this();
			setValue(flag);
			setWeight(weight);
		}

		@Override
		public Set<Stsflag> getUniverse() {
			return EnumSet.allOf(Stsflag.class);
		}

		@Override
		public Stsflag fromString(String str) {
			return Stsflag.valueOf(str);
		}

		@Override
		public String valueAsString() {
			return getValue().name();
		}

	}

	public static class OpponentStatus extends AiMod<Stsflag> {

		public OpponentStatus() {
			super("opponent-status", Stsflag.braced);
		}

		public OpponentStatus(Stsflag flag, double weight) {
			this();
			setValue(flag);
			setWeight(weight);
		}

		@Override
		public Set<Stsflag> getUniverse() {
			return EnumSet.allOf(Stsflag.class);
		}

		@Override
		public Stsflag fromString(String str) {
			return Stsflag.valueOf(str);
		}

		@Override
		public String valueAsString() {
			return getValue().name();
		}

	}
}
