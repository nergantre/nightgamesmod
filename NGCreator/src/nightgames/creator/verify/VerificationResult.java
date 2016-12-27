package nightgames.creator.verify;

public class VerificationResult implements Comparable<VerificationResult> {

	private final MessageLevel level;
	private final String message;
	
	VerificationResult(MessageLevel level, String message) {
		this.level = level;
		this.message = message;
	}

	public MessageLevel getLevel() {
		return level;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		VerificationResult other = (VerificationResult) obj;
		if (level != other.level)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VerificationResult [level=" + level + ", message=" + message + "]";
	}

	@Override
	public int compareTo(VerificationResult o) {
		return -Integer.compare(level.ordinal(), o.level.ordinal());
	}
}
