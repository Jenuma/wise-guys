Warp = {}

function Warp.execute(warpObject, game, triggerKey, destination, x, y)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	local gdx = luajava.bindClass("com.badlogic.gdx.Gdx")
	local input = luajava.bindClass("com.badlogic.gdx.Input")
	
	local collisionComponent = mappers.collision:get(warpObject)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	---------------------------
	-- Collision with Player --
	---------------------------
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
	
		--------------------
		-- Warp Animation --
		--------------------
		if game.eventProcessing then
			local playerPositionComponent = mappers.position:get(collidingEntity)
			local playerHitboxComponent = mappers.hitbox:get(collidingEntity)
			local warpHitboxComponent = mappers.hitbox:get(warpObject)
			
			if triggerKey == input.Keys.DOWN then
				playerPositionComponent.y = playerPositionComponent.y - 0.4
				playerHitboxComponent.hitbox.y = playerPositionComponent.y
			elseif triggerKey == input.Keys.RIGHT then
				playerPositionComponent.x = playerPositionComponent.x + 0.4
				playerHitboxComponent.hitbox.x = playerPositionComponent.x
			end
			
			-----------------------------
			-- Warp Animation Finished --
			-----------------------------
			if not playerHitboxComponent.hitbox:overlaps(warpHitboxComponent.hitbox) then
				game:prepareNextGameScreen(destination, x, y)
				game.eventProcessing = false;
			end
		
		----------------------
		-- Start Warp Event --
		----------------------
		elseif gdx.input:isKeyPressed(triggerKey) then
			local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
			local pipeSfx = game.assets.manager:get(assetFiles.sfxPipe)
		
			local playerVelocityComponent = mappers.velocity:get(collidingEntity)
			local playerAccelerationComponent = mappers.acceleration:get(collidingEntity)
			
			playerVelocityComponent.x = 0;
			playerVelocityComponent.y = 0;
			playerAccelerationComponent.x = 0;
			playerAccelerationComponent.y = 0;
		
			pipeSfx:play()
			game.eventProcessing = true;
		end
	end
end

return Warp
