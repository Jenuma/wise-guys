Coin = {}

function Coin.execute(player, coinState, disabled, sfxCoin)
	sfxCoin:play()
	
	player.coins = player.coins + 1
	player.score = player.score + 200
	
	coinState.enabledState = disabled
end

return Coin
