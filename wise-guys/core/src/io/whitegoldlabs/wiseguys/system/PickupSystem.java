package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;

import io.whitegoldlabs.wiseguys.component.PickupComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent.EnabledState;
import io.whitegoldlabs.wiseguys.util.Mappers;
import io.whitegoldlabs.wiseguys.util.ScriptManager;

public class PickupSystem extends EntitySystem
{
	private Entity player;
	private ImmutableArray<Entity> pickups;
	
	ScriptManager script;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public PickupSystem(Entity player)
	{
		this.player = player;
		
		script = new ScriptManager("hello.lua"); // Load scripts with world in game screen constructor?
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		pickups = engine.getEntitiesFor(Family.all
		(
			PickupComponent.class,
			StateComponent.class
		).get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		Rectangle playerHitbox = Mappers.hitbox.get(player).hitbox;
		
		for(Entity pickup : pickups)
		{
			if(playerHitbox.overlaps(Mappers.hitbox.get(pickup).hitbox))
			{
				script.execute(Mappers.script.get(pickup).arguments);
				Mappers.state.get(pickup).enabledState = EnabledState.DISABLED;
			}
		}
	}
}
