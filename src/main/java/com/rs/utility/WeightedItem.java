/**
 * 
 */
package com.rs.utility;

import lombok.Getter;

/**
 * Represents a weighted object.
 * @author ReverendDread
 * Sep 29, 2018
 */
public class WeightedItem<T> implements Comparable<WeightedItem<T>> {
	
	@Getter private final T representation;
	@Getter private double weight;

	public WeightedItem(double weight, T representation) {
		if (weight <= 0)
			throw new IllegalArgumentException("The weight of an item must be larger than zero!");
		this.weight = weight;
		this.representation = representation;
	}
	
	@Override
	public String toString() {
		return "[Object: " + getRepresentation() + ", " + "Weight: " + getWeight() + "]";
	}
	
	@Override
	public int compareTo(WeightedItem<T> o) {
		return (int) Math.signum(getWeight() - o.getWeight());
	}
	
}
