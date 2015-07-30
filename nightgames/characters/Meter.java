package nightgames.characters;

import java.io.Serializable;

public class Meter implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private int current;
	private int max;
	
	public Meter(int max){
		this.max=max;
		this.current=0;
	}
	public int get(){
		return current;
	}
	public int max(){
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
	public void restore(int i){
		current+=i;
		if(current>max){
			current=max;
		}
	}
	public boolean isEmpty(){
		return current<=0;
	}
	public boolean isFull(){
		return current==max;
	}
	public void empty(){
		current=0;
	}
	public void fill(){
		current=max;
	}
	public void set(int i){
		current=i;
		if(current>max){
			current=max;
		}
	}
	public void gain(int i){
		max+=i;
		if(current>max){
			current=max;
		}
	}
	public void setMax(int i){
		max=i;
		current=max;
	}
	public int percent(){
		return 100*current/max;
	}
	public Meter clone() throws CloneNotSupportedException {
		return (Meter) super.clone();
	}
}
