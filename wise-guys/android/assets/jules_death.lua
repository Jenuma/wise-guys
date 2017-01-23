Jules_death = {}

function Jules_death.execute(game, playerInventory, worldName, sfxJulesDeath)
	local thread = luajava.bindClass("java.lang.Thread") 
	
	sfxJulesDeath:play()
	thread:sleep(3000)
	game.wasSleeping = true
	
	playerInventory.lives = playerInventory.lives - 1
	game.engine:removeAllEntities()
	
	if playerInventory.lives > 0 then
		newScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.WorldIntroScreen", game, worldName)
	else
		newScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.GameOverScreen", game)
	end
	
	game.currentScreen:dispose()
	game.currentScreen = newScreen
	game:setScreen(newScreen)
end

return Jules_death
