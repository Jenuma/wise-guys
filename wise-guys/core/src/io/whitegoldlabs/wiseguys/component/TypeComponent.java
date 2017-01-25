package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class TypeComponent implements Component
{
	public enum Type
	{
		PLAYER,
		OBSTACLE,
		PICKUP,
		EVENT,
		ENEMY,
		PLAYER_PROJECTILE,
		ENEMY_PROJECTILE
	}
	
	public Type type;
	
	public TypeComponent(Type type)
	{
		this.type = type;
	}
}
