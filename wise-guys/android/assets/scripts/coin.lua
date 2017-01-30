Coin = {}

function Coin.execute(coin, game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(coin)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	---------------------------
	-- Collision with Player --
	---------------------------
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
		local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
		
		local player = mappers.player:get(collidingEntity)
		local coinStateComponent = mappers.state:get(coin)
		local coinSfx = game.assets.manager:get(assetFiles.sfxCoin)
	
		coinSfx:play()
		player.coins = player.coins + 1
		player.score = player.score + 200
		
		coinStateComponent.enabledState = states.EnabledState.DISABLED
	end
end

return Coin
