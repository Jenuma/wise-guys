package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.InventoryComponent;
import io.whitegoldlabs.wiseguys.component.PickupComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;
import io.whitegoldlabs.wiseguys.view.GameScreen;

import static io.whitegoldlabs.wiseguys.component.StateComponent.EnabledState;

public class PickupSystem extends EntitySystem
{
	private WiseGuys game;
	private GameScreen screen;
	private Engine engine;
	private Entity player;
	private ImmutableArray<Entity> pickups;
	
	private Sound sfxCoin;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public PickupSystem(WiseGuys game, GameScreen screen, Engine engine, Entity player)
	{
		this.game = game;
		this.screen = screen;
		this.engine = engine;
		this.player = player;
		
		sfxCoin = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
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
				switch(Mappers.pickup.get(pickup).pickup)
				{
					case COIN:
						coinEffect();
						break;
						
					case CONNOLI:
						connoliEffect();
						break;
						
					case FINGERLESS_GLOVE:
						fingerlessGloveEffect();
						break;
						
					case ONION:
						onionEffect();
						break;
						
					case ONE_UP:
						oneUpEffect();
						break;
						
					case TELEPORT_DOWN:
						teleportDownEffect();
						break;
				}
				
				Mappers.state.get(pickup).enabledState = EnabledState.DISABLED;
			}
		}
	}
	
	private void coinEffect()
	{
		InventoryComponent playerInventory = Mappers.inventory.get(player);
		
		sfxCoin.play();
		
		playerInventory.coins++;
		playerInventory.score += 200;
	}
	
	private void connoliEffect()
	{
		
	}
	
	private void fingerlessGloveEffect()
	{
		
	}
	
	private void onionEffect()
	{
		
	}
	
	private void oneUpEffect()
	{
		
	}
	
	private void teleportDownEffect()
	{
		if(Gdx.input.isKeyPressed(Keys.DOWN))
		{
			game.setScreen(new GameScreen(game, "world1-1a.csv", 16, 32));
			screen.dispose();
		}
	}
}
