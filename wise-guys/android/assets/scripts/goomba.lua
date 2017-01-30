Goomba = {}

function Goomba.execute(goomba, game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
	
	local collisionComponent = mappers.collision:get(goomba)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	local goombaStateComponent = mappers.state:get(goomba)
	
	---------------------------
	-- Collision with Player --
	---------------------------
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local playerComponent = mappers.player:get(collidingEntity)
		
		if not playerComponent.damaged then
			local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
			local playerStates = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PlayerComponent")
			
			local stompSfx = game.assets.manager:get(assetFiles.sfxStomp)
			local julesDeathSfx = game.assets.manager:get(assetFiles.sfxJulesDeath)
			local powerdownSfx = game.assets.manager:get(assetFiles.sfxPipe)
			
			local playerHitboxComponent = mappers.hitbox:get(collidingEntity)
			local goombaHitboxComponent = mappers.hitbox:get(goomba)
		
			--------------------------------
			-- Get Intersection Rectangle --
			--------------------------------
			local intersectX = math.max(playerHitboxComponent.hitbox.x, goombaHitboxComponent.hitbox.x)
			local intersectY = math.max(playerHitboxComponent.hitbox.y, goombaHitboxComponent.hitbox.y)
			local intersectWidth = math.min(playerHitboxComponent.hitbox.x + playerHitboxComponent.hitbox.width, goombaHitboxComponent.hitbox.x + goombaHitboxComponent.hitbox.width) - intersectX
			local intersectHeight = math.min(playerHitboxComponent.hitbox.y + playerHitboxComponent.hitbox.height, goombaHitboxComponent.hitbox.y + goombaHitboxComponent.hitbox.height) - intersectY
			
			-----------------------
      -- Goomba Stomped On --
      -----------------------
			if intersectWidth > intersectHeight then
				if playerHitboxComponent.hitbox.y > goombaHitboxComponent.hitbox.y then
					local playerVelocityComponent = mappers.velocity:get(collidingEntity)
					
					stompSfx:play()
					playerVelocityComponent.y = 430
					
					goombaStateComponent.enabledState = states.EnabledState.DISABLED
				end
				
			---------------------------
      -- Goomba Touched Player --
      ---------------------------
			else
			
			  -----------------
        -- Player Dies --
        -----------------
				if playerComponent.playerState == playerStates.PlayerState.NORMAL then
					Jules_death.execute(game, julesDeathSfx)
					
				--------------------
        -- Player Damaged --
        --------------------
				elseif playerComponent.playerState == playerStates.PlayerState.SUPER then
				  local playerAnimationComponent = mappers.animation:get(collidingEntity)
          local playerStateComponent = mappers.state:get(collidingEntity)
          local playerPowerupAnimation = playerAnimationComponent.animations:get("POWERUP")
    
          ---------------------------
          -- Start Powerdown Event --
          ---------------------------
				  if not game.eventProcessing then
            powerdownSfx:play()
            game.eventProcessing = true
            
            playerStateComponent.time = 0
            
          ---------------------------------
          -- Powerdown Animation Playing --
          ---------------------------------
          else
            local animations = luajava.bindClass("com.badlogic.gdx.graphics.g2d.Animation")
            local playerSpriteComponent = mappers.sprite:get(collidingEntity)
            
            playerPowerupAnimation:setPlayMode(animations.PlayMode.REVERSED)
            playerSpriteComponent.sprite = playerPowerupAnimation:getKeyFrame(playerStateComponent.time, false)
            
            if playerStateComponent.directionState == states.DirectionState.LEFT then
              playerSpriteComponent.sprite:setFlip(true, false)
            elseif playerStateComponent.directionState == states.DirectionState.LEFT then
              playerSpriteComponent.sprite:setFlip(false, false)
            end
          end
				
				  ----------------------------------
          -- Powerdown Animation Finished --
          ----------------------------------
				  if playerPowerupAnimation:isAnimationFinished(playerStateComponent.time) then
				    game:powerdownNormalJules()
				    game.eventProcessing = false
				  end
				end
			end
		end
		
	-----------------------------
	-- Collision with Obstacle --
	-----------------------------
	elseif collidingEntityTypeComponent.type == types.Type.OBSTACLE then
		if goombaStateComponent.airborneState == states.AirborneState.GROUNDED then
			local goombaVelocityComponent = mappers.velocity:get(goomba)
			goombaVelocityComponent.x = 0 - goombaVelocityComponent.x
			
			if goombaStateComponent.directionState == states.DirectionState.RIGHT then
				goombaStateComponent.directionState = states.DirectionState.LEFT
			else
				goombaStateComponent.directionState = states.DirectionState.RIGHT
			end
		end
	end
end

return Goomba
