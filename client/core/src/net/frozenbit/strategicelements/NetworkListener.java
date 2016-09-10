package net.frozenbit.strategicelements;

import com.google.gson.JsonObject;

public interface NetworkListener {
	void onDataReceived(JsonObject data);
}
