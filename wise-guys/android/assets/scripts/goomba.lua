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
	  local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
		local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
		local playerStates = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PlayerComponent")
		
		local stompSfx = game.assets.manager:get(assetFiles.sfxStomp)
		local powerdownSfx = game.assets.manager:get(assetFiles.sfxPipe)
		
		local playerComponent = mappers.player:get(collidingEntity)
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
			  local velocities = luajava.bindClass("io.whitegoldlabs.wiseguys.component.VelocityComponent")
			  local accelerations = luajava.bindClass("io.whitegoldlabs.wiseguys.component.AccelerationComponent")
			  
			  local animations = luajava.bindClass("io.whitegoldlabs.wiseguys.component.AnimationComponent")
			  local scripts = luajava.bindClass("io.whitegoldlabs.wiseguys.component.ScriptComponent")
				local playerVelocityComponent = mappers.velocity:get(collidingEntity)
				local playerPositionComponent = mappers.position:get(collidingEntity)
				local goombaPositionComponent = mappers.position:get(goomba)
				local goombaSpriteComponent = mappers.sprite:get(goomba)
				
				local spriteSheet = game.assets.manager:get(assetFiles.spriteSheet)
				
				stompSfx:play()
				playerPositionComponent.y = goombaPositionComponent.y + goombaSpriteComponent.sprite:getHeight()
				playerHitboxComponent.hitbox.y = playerPositionComponent.y
				playerVelocityComponent.y = 4
				
				local scriptArgs = arrayInstantiator:getNewArray()
				scriptArgs:add(goomba)
				
        local boxBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\goomba_death_behavior.lua", scriptArgs)
        boxBehaviorComponent.behaviorState = "DEAD"
        game.scriptManager:loadScript("scripts\\goomba_death_behavior.lua")
        
        goomba:add(boxBehaviorComponent)
        
        goombaSpriteComponent.sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 32, 48, 16, 16)
        goomba:remove(velocities)
        goomba:remove(accelerations)
        goomba:remove(animations)
        goomba:remove(scripts)
			end
			
		---------------------------
    -- Goomba Touched Player --
    ---------------------------
		else
		
		  -----------------
      -- Player Dies --
      -----------------
			if playerComponent.playerState == playerStates.PlayerState.NORMAL then
			  if not playerComponent.damaged then
          local playerPositionComponent = mappers.position:get(collidingEntity)
          
          local scriptArgs = arrayInstantiator:getNewArray()
          scriptArgs:add(game)
          scriptArgs:add(playerPositionComponent.y)
        
          local playerBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\jules_death_behavior.lua", scriptArgs)
          playerBehaviorComponent.behaviorState = "DYING"
          game.scriptManager:loadScript("scripts\\jules_death_behavior.lua")
    
          game.player:add(playerBehaviorComponent)
			  end
				
			--------------------
      -- Player Damaged --
      --------------------
			elseif playerComponent.playerState == playerStates.PlayerState.SUPER then
        powerdownSfx:play()
      
        local scriptArgs = arrayInstantiator:getNewArray()
        scriptArgs:add(game)
        
        local playerBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\jules_powerdown_normal_behavior.lua", scriptArgs)
        playerBehaviorComponent.behaviorState = "TOUCHED"
        game.scriptManager:loadScript("scripts\\jules_powerdown_normal_behavior.lua")
        collidingEntity:add(playerBehaviorComponent)  
      end
		end
		
	--------------------------------------
	-- Collision with Obstacle or Enemy --
	--------------------------------------
	elseif collidingEntityTypeComponent.type == types.Type.OBSTACLE or collidingEntityTypeComponent.type == types.Type.ENEMY then
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
