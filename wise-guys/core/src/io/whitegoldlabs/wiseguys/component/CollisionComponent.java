package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class CollisionComponent implements Component
{
	public Entity collidingWith;
	
	public CollisionComponent(Entity collidingWith)
	{
		this.collidingWith = collidingWith;
	}
}
