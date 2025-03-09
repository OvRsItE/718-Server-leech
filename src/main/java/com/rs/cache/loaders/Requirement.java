/**
 * 
 */
package com.rs.cache.loaders;

import lombok.Getter;

/**
 * Represents an items skill requirement.
 * @author ReverendDread
 * Jul 30, 2018
 */
public class Requirement {
	
	@Getter private int skill;
	@Getter private int level;
	
	public Requirement(int skill, int level) {
		this.skill = skill;
		this.level = level;
	}
	
}
