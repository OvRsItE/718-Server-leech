/**
 * 
 */
package com.rs.game.world.entity.player.content.donate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rs.game.world.entity.player.Player;

public class Donation {

	private int id, amount;

	/**
	 * Represents a donation in the database
	 * @param id The id in the SQL database for this donation
	 * @param amount The amount of tokens this donation is for
	 */
	public Donation(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	/**
	 * @return The id of the donation in the SQL database
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The amount of tokens this donation represents
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Makes a call to the web API to mark the donation as claimed.
	 * @param player The player claiming the donation
	 * @return <code>true</code> if database was updated, <code>false</code> otherwise
	 */
	public boolean claim(Player player) {

			URL url = null;
			HttpURLConnection request;
			try
			{
				url = new URL(DonationConstants.API_URL + "&claim=" + id + "&ip=" + player.getSession().getIP());
				request = (HttpURLConnection)url.openConnection();
				request.setDoOutput(true);
				request.setRequestMethod("GET");

				request.connect();
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(new InputStreamReader(request.getInputStream()));
				JsonObject grabbedJson = element.getAsJsonObject();

				if (request.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					DonationReturnStatus returnStatus = DonationReturnStatus.getByString(grabbedJson.get("status").getAsString());
					switch(returnStatus) {
					case SUCCESS:
						int bonusTokens = DonationFunctions.calculateBonusTokens(amount);
						player.getDonationManager().increment(amount);
						player.getBank().addItem(8800, amount + bonusTokens, true);
						DonationFunctions.giveBonusRewards(player, amount);
						return true;
					default:
						return false;
					}

				}
			}
			catch(IOException e)
			{
				return false;
			}
		
		return false;
	}
}
