Jules_death = {}

function Jules_death.execute(game, sfxJulesDeath)
	--local thread = luajava.bindClass("java.lang.Thread")
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers") 
		
	local player = mappers.player:get(game.player)
	
	sfxJulesDeath:play()
	--thread:sleep(3000)
	--game.wasSleeping = true
	
	player.lives = player.lives - 1
	game.engine:removeAllEntities()
	
	if player.lives > 0 then
		newScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.WorldIntroScreen", game, game.currentWorld)
	else
		newScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.GameOverScreen", game)
	end
	
	game.currentScreen:dispose()
	game.currentScreen = newScreen
	game:setScreen(newScreen)
end

return Jules_death
