package io.whitegoldlabs.wiseguys.util;

import com.strongjoshua.console.CommandExecutor;

import io.whitegoldlabs.wiseguys.WiseGuys;
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
	
	public void zoomOut()
	{
		game.camera.zoom = 1;
	}
	
	public void zoomIn()
	{
		game.camera.zoom = 0.3f;
	}
}
