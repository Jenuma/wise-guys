Connolo = {}

function Connolo.execute(connolo, game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
	
	local collisionComponent = mappers.collision:get(connolo)
	
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	local collidingEntityHitboxComponent = mappers.hitbox:get(collidingEntity)
	
	local connoloHitboxComponent = mappers.hitbox:get(connolo)
	local connoloStateComponent = mappers.state:get(connolo)
	local connoloPositionComponent = mappers.position:get(connolo)
	
	-----------------------------
	-- Collision with Obstacle --
	-----------------------------
	if collidingEntityTypeComponent.type == types.Type.OBSTACLE then
	
		----------------------
		-- Connolo Spawning --
		----------------------
		if connoloStateComponent.motionState == states.MotionState.STILL then
			connoloPositionComponent.y = connoloPositionComponent.y + 0.4
			connoloHitboxComponent.hitbox.y = connoloPositionComponent.y
		else
			---------------------
			-- Connolo Falling --
			---------------------
			if connoloStateComponent.airborneState == states.AirborneState.FALLING then
				connoloPositionComponent.y = collidingEntityHitboxComponent.hitbox.y + collidingEntityHitboxComponent.hitbox.height
				connoloHitboxComponent.hitbox.y = connoloPositionComponent.y
				
				connoloStateComponent.airborneState = states.AirborneState.GROUNDED
				
			--------------------
			-- Connolo Moving --
			--------------------
			else
				local connoloVelocityComponent = mappers.velocity:get(connolo)
				connoloVelocityComponent.x = 0 - connoloVelocityComponent.x
			
				if connoloStateComponent.directionState == states.DirectionState.RIGHT then
					connoloStateComponent.directionState = states.DirectionState.LEFT
				else
					connoloStateComponent.directionState = states.DirectionState.RIGHT
				end
			end
		end
		
	---------------------------
	-- Collision with Player --
	---------------------------
	elseif collidingEntityTypeComponent.type == types.Type.PLAYER then
    local playerAnimationComponent = mappers.animation:get(collidingEntity)
    local playerStateComponent = mappers.state:get(collidingEntity)
    local playerPowerupAnimation = playerAnimationComponent.animations:get("POWERUP")
	
	  -------------------------
    -- Start Powerup Event --
    -------------------------
    if not game.eventProcessing then
      local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
      
      local powerupSfx = game.assets.manager:get(assetFiles.sfxPowerup)
      local connoloSpriteComponent = mappers.sprite:get(connolo)
      local spriteSheet = game.assets.manager:get(assetFiles.spriteSheet)
      
      powerupSfx:play()
      game.eventProcessing = true
      
      connoloSpriteComponent.sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 0, 0, 0, 0)
      
      playerStateComponent.time = 0
    -------------------------------
    -- Powerup Animation Playing --
    -------------------------------
    else
      local playerSpriteComponent = mappers.sprite:get(collidingEntity)
      
      playerSpriteComponent.sprite = playerPowerupAnimation:getKeyFrame(playerStateComponent.time, false)
      
      if playerStateComponent.directionState == states.DirectionState.LEFT then
        playerSpriteComponent.sprite:setFlip(true, false)
      elseif playerStateComponent.directionState == states.DirectionState.LEFT then
        playerSpriteComponent.sprite:setFlip(false, false)
      end
    end
    
    --------------------------------
    -- Powerup Animation Finished --
    --------------------------------
    if playerPowerupAnimation:isAnimationFinished(playerStateComponent.time) then
      game:powerupSuperJules()
      game.eventProcessing = false
      connoloStateComponent.enabledState = states.EnabledState.DISABLED
    end
	end
	
	-------------------------------
	-- Connolo Finished Spawning --
	-------------------------------
	if not collidingEntityHitboxComponent.hitbox:overlaps(connoloHitboxComponent.hitbox) then
		local connoloVelocityComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.VelocityComponent", 25, 0)
		local connoloAccelerationComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.AccelerationComponent", 0, 0)
		
		connolo:add(connoloVelocityComponent)
		connolo:add(connoloAccelerationComponent)
		
		connoloStateComponent.motionState = states.MotionState.MOVING
	end
end

return Connolo
