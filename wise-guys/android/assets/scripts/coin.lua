Coin = {}

function Coin.execute(thisEntity, sfxCoin)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
	
		local player = mappers.player:get(collidingEntity)
		local thisEntityStateComponent = mappers.state:get(thisEntity)
	
		sfxCoin:play()
		player.coins = player.coins + 1
		player.score = player.score + 200
		
		thisEntityStateComponent.enabledState = states.EnabledState.DISABLED
	end
end

return Coin
