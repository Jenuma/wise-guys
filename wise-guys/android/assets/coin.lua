Coin = {}

function Coin.execute(playerInventory, coinState, disabled, sfxCoin)
	sfxCoin:play()
	
	playerInventory.coins = playerInventory.coins + 1
	playerInventory.score = playerInventory.score + 200
	
	coinState.enabledState = disabled
end

return Coin
