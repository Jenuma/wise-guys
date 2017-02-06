package io.whitegoldlabs.wiseguys.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.strongjoshua.console.CommandExecutor;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.TypeComponent;
import io.whitegoldlabs.wiseguys.view.MainMenuScreen;

public class ConsoleCommandExecutor extends CommandExecutor
{
	private final WiseGuys game;
	
	public ConsoleCommandExecutor(final WiseGuys game)
	{
		this.game = game;
	}
	
	public void restart()
	{
		MainMenuScreen mainMenu = new MainMenuScreen(game);
		game.currentScreen.dispose();
		game.currentScreen = mainMenu;
		game.setScreen(game.currentScreen);
		
		game.engine.removeAllEntities();
		game.scriptManager = new ScriptManager();
		Worlds.unloadWorldEntities();
	}
	
	public void getPlayerID()
	{
		console.log(game.player.toString());
	}
	
	public void getNumberOfEngineEntities()
	{
		console.log(String.valueOf(game.engine.getEntities().size()));
	}
	
	public void listLoadedWorlds()
	{
		for(String worldName : Worlds.getLoadedWorlds())
		{
			console.log(worldName);
		}
	}
	
	public void listLoadedScripts()
	{
		for(String scriptName : game.scriptManager.getLoadedScripts())
		{
			console.log(scriptName);
		}
	}
	
	public void killBullets()
	{
		ImmutableArray<Entity> bullets = game.engine.getEntitiesFor(Family.all(TypeComponent.class).get());
		
		for(Entity bullet : bullets)
		{
			if(Mappers.type.get(bullet).type == TypeComponent.Type.PLAYER_PROJECTILE)
			{
				Mappers.state.get(bullet).enabledState = StateComponent.EnabledState.DISABLED;
			}
		}
	}
	
	public void zoomOut()
	{
		game.camera.zoom = 1;
	}
	
	public void zoomIn()
	{
		game.camera.zoom = 0.3f;
	}
}
