Jules_death = {}

function Jules_death.execute(game, sfxJulesDeath)
	local thread = luajava.bindClass("java.lang.Thread") 
	
	sfxJulesDeath:play()
	thread:sleep(2500)
	game.wasSleeping = true
	
	--Remove a life
	--Check if lives > 0, if so restart level, otherwise game over
	--If game over, dispose of all game objects like worlds and scripts
	
	newGameOverScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.GameOverScreen", game)
	game.currentScreen:dispose()
	game.currentScreen = newGameOverScreen
	game:setScreen(newGameOverScreen)
end

return Jules_death
