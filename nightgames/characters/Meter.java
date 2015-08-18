package nightgames.characters;

import java.io.Serializable;

public class Meter implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private int current;
	private float max;

	public Meter(int max){
		this.max=max;
		this.current=0;
	}
	public int get(){
		if (current > max()) {
			return max();
		} else {
			return current;
		}
	}
	public int getOverflow(){
		return Math.max(0, current - max());
	}
	public int max(){
		return (int)max;
	}
	public float maxFull(){
		return max;
	}
	public void reduce(int i){
		if(i>0){
			current-=i;
		}
		if(current<0){
			current=0;
		}
	}
	public void restore(int i) {
		if (current < max()) {
			current = Math.min(max(), current+i);
		}
	}
	public void restoreNoLimit(int i){
		current+=i;
	}
	public boolean isEmpty(){
		return current<=0;
	}
	public boolean isFull(){
		return current>=max;
	}
	public void empty(){
		current=0;
	}
	public void fill(){
		current= Math.max(max(), current);
	}
	public void set(int i){
		current=i;
		if(current<0){
			current=0;
		}
	}
	public void gain(float i){
		max+=i;
		if(current>max()){
			current=max();
		}
	}
	public void setMax(float i){
		max=i;
		current=max();
	}
	public int percent(){
		return Math.min(100, 100*current/max());
	}
	public Meter clone() throws CloneNotSupportedException {
		return (Meter) super.clone();
	}
}
