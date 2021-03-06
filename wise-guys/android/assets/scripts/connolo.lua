Connolo = {}

function Connolo.execute(connolo, game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
	local playerStates = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PlayerComponent")
	
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
	  local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
	  local powerupSfx = game.assets.manager:get(assetFiles.sfxPowerup)
	  
	  local playerComponent = mappers.player:get(collidingEntity)
	  
	  powerupSfx:play()
	  playerComponent.score = playerComponent.score + 1000
    connoloStateComponent.enabledState = states.EnabledState.DISABLED
	  
	  if playerComponent.playerState == playerStates.PlayerState.NORMAL then
  	  local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
  	  
  	  local scriptArgs = arrayInstantiator:getNewArray()
  	  scriptArgs:add(game)
  	  
  	  local playerBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\jules_connolo_behavior.lua", scriptArgs)
  	  playerBehaviorComponent.behaviorState = "TOUCHED"
  	  game.scriptManager:loadScript("scripts\\jules_connolo_behavior.lua")
  	  collidingEntity:add(playerBehaviorComponent)
	  end
	end
	
	-------------------------------
	-- Connolo Finished Spawning --
	-------------------------------
	if not collidingEntityHitboxComponent.hitbox:overlaps(connoloHitboxComponent.hitbox) then
		local connoloVelocityComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.VelocityComponent", 0.8, 0)
		local connoloAccelerationComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.AccelerationComponent", 0, 0)
		
		connolo:add(connoloVelocityComponent)
		connolo:add(connoloAccelerationComponent)
		
		connoloStateComponent.motionState = states.MotionState.MOVING
	end
end

return Connolo
