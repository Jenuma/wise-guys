Teleport = {}

function Teleport.execute(game, input, triggerKey, sfxPipe, camera, destination, x, y)
	if input:isKeyPressed(triggerKey) then
		local thread = luajava.bindClass("java.lang.Thread") 
		
		sfxPipe:play()
		thread:sleep(1000)
		game.wasSleeping = true
		
		newGameScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.GameScreen", game, camera, destination, x, y)
		game.currentScreen:dispose()
		game.currentScreen = newGameScreen
		game:setScreen(newGameScreen)
	end
end

return Teleport
