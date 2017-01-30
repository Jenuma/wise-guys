Goomba = {}

function Goomba.execute(thisEntity, game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	local thisEntityStateComponent = mappers.state:get(thisEntity)
	
	---------------------------
	-- Collision with Player --
	---------------------------
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local collidingEntityPlayerComponent = mappers.player:get(collidingEntity)
		
		if not collidingEntityPlayerComponent.damaged then
			local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
			local playerStates = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PlayerComponent")
			
			local stompSfx = game.assets.manager:get(assetFiles.sfxStomp)
			local julesDeathSfx = game.assets.manager:get(assetFiles.sfxJulesDeath)
			local powerdownSfx = game.assets.manager:get(assetFiles.sfxPipe)
			
			local collidingEntityHitboxComponent = mappers.hitbox:get(collidingEntity)
			local thisEntityHitboxComponent = mappers.hitbox:get(thisEntity)
		
			--------------------------------
			-- Get Intersection Rectangle --
			--------------------------------
			local intersectX = math.max(collidingEntityHitboxComponent.hitbox.x, thisEntityHitboxComponent.hitbox.x)
			local intersectY = math.max(collidingEntityHitboxComponent.hitbox.y, thisEntityHitboxComponent.hitbox.y)
			local intersectWidth = math.min(collidingEntityHitboxComponent.hitbox.x + collidingEntityHitboxComponent.hitbox.width, thisEntityHitboxComponent.hitbox.x + thisEntityHitboxComponent.hitbox.width) - intersectX
			local intersectHeight = math.min(collidingEntityHitboxComponent.hitbox.y + collidingEntityHitboxComponent.hitbox.height, thisEntityHitboxComponent.hitbox.y + thisEntityHitboxComponent.hitbox.height) - intersectY
			
			if intersectWidth > intersectHeight then
				if collidingEntityHitboxComponent.hitbox.y > thisEntityHitboxComponent.hitbox.y then
					local collidingEntityVelocityComponent = mappers.velocity:get(collidingEntity)
					
					stompSfx:play()
					collidingEntityVelocityComponent.y = 430
					
					thisEntityStateComponent.enabledState = states.EnabledState.DISABLED
				end
			else
				if collidingEntityPlayerComponent.playerState == playerStates.PlayerState.NORMAL then
					Jules_death.execute(game, julesDeathSfx)
				elseif collidingEntityPlayerComponent.playerState == playerStates.PlayerState.SUPER then
					powerdownSfx:play()
					game:powerdownJulesNormal()
				end
			end
		end
		
	-----------------------------
	-- Collision with Obstacle --
	-----------------------------
	elseif collidingEntityTypeComponent.type == types.Type.OBSTACLE then
		if thisEntityStateComponent.airborneState == states.AirborneState.GROUNDED then
			local thisEntityVelocityComponent = mappers.velocity:get(thisEntity)
			thisEntityVelocityComponent.x = 0 - thisEntityVelocityComponent.x
			
			if thisEntityStateComponent.directionState == states.DirectionState.RIGHT then
				thisEntityStateComponent.directionState = states.DirectionState.LEFT
			else
				thisEntityStateComponent.directionState = states.DirectionState.RIGHT
			end
		end
	end
end

return Goomba
