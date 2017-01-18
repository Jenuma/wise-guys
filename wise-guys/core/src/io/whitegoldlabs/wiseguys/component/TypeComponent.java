package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class TypeComponent implements Component
{
	public enum Type
	{
		PLAYER,
		OBSTACLE,
		ENEMY,
		PLAYER_PROJECTILE,
		ENEMY_PROJECTILE,
		PICKUP,
		EVENT
	}
	
	public Type type;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public TypeComponent(Type type)
	{
		this.type = type;
	}
}
