package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

import io.whitegoldlabs.wiseguys.component.CollectboxComponent;
import io.whitegoldlabs.wiseguys.component.InventoryComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class PickupSystem extends EntitySystem
{
	private Engine engine;
	private Entity player;
	private ImmutableArray<Entity> pickups;
	
	private Sound sfxCoin;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public PickupSystem(Engine engine, Entity player)
	{
		this.engine = engine;
		this.player = player;
		
		sfxCoin = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		pickups = engine.getEntitiesFor(Family.all
		(
			CollectboxComponent.class
		).get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		Rectangle playerHitbox = Mappers.hitbox.get(player).hitbox;
		InventoryComponent playerInventory = Mappers.inventory.get(player);
		
		for(Entity pickup : pickups)
		{
			if(playerHitbox.overlaps(Mappers.collectbox.get(pickup).collectbox))
			{
				switch(Mappers.collectbox.get(pickup).type)
				{
					case COIN:
						sfxCoin.play();
						
						playerInventory.coins++;
						playerInventory.score += 200;
						
						engine.removeEntity(pickup);
						break;
				}
			}
		}
	}
}
