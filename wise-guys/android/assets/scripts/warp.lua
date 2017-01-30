Warp = {}

function Warp.execute(thisEntity, game, triggerKey, destination, x, y)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	local gdx = luajava.bindClass("com.badlogic.gdx.Gdx")
	local input = luajava.bindClass("com.badlogic.gdx.Input")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		--------------------
		-- Warp Animation --
		--------------------
		if game.eventProcessing then
			local collidingEntityPositionComponent = mappers.position:get(collidingEntity)
			local collidingEntityHitboxComponent = mappers.hitbox:get(collidingEntity)
			local thisEntityHitboxComponent = mappers.hitbox:get(thisEntity)
			
			if triggerKey == input.Keys.DOWN then
				collidingEntityPositionComponent.y = collidingEntityPositionComponent.y - 0.4
				collidingEntityHitboxComponent.hitbox.y = collidingEntityPositionComponent.y
			elseif triggerKey == input.Keys.RIGHT then
				collidingEntityPositionComponent.x = collidingEntityPositionComponent.x + 0.4
				collidingEntityHitboxComponent.hitbox.x = collidingEntityPositionComponent.x
			end
			
			-----------------------------
			-- Warp Animation Finished --
			-----------------------------
			if not collidingEntityHitboxComponent.hitbox:overlaps(thisEntityHitboxComponent.hitbox) then
				game:prepareNextGameScreen(destination, x, y)
				game.eventProcessing = false;
			end
		
		----------------------
		-- Start Warp Event --
		----------------------
		elseif gdx.input:isKeyPressed(triggerKey) then
			local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
			local pipeSfx = game.assets.manager:get(assetFiles.sfxPipe)
		
			local collidingEntityVelocityComponent = mappers.velocity:get(collidingEntity)
			local collidingEntityAccelerationComponent = mappers.acceleration:get(collidingEntity)
			
			collidingEntityVelocityComponent.x = 0;
			collidingEntityVelocityComponent.y = 0;
			collidingEntityAccelerationComponent.x = 0;
			collidingEntityAccelerationComponent.y = 0;
		
			pipeSfx:play()
			game.eventProcessing = true;
		end
	end
end

return Warp
