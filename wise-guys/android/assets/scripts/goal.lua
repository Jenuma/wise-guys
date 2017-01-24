Goal = {}

function Goal.execute(game, sfxStageClear)
	local thread = luajava.bindClass("java.lang.Thread") 
	local worlds = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Worlds")
	
	sfxStageClear:play()
	thread:sleep(6000)
	game.wasSleeping = true
	
	newMainMenuScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.MainMenuScreen", game)
	game.currentScreen:dispose()
	game.currentScreen = newMainMenuScreen
	game:setScreen(newMainMenuScreen)
	
	game.engine:removeAllEntities()
	game.scriptManager = luajava.newInstance("io.whitegoldlabs.wiseguys.util.ScriptManager")
	worlds:unloadWorldEntities()
end

return Goal
