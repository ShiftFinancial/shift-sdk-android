package me.ledge.link.api.utils.parsers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.ledge.link.api.vos.datapoints.BankAccount;
import me.ledge.link.api.vos.datapoints.Card;
import me.ledge.link.api.vos.datapoints.FinancialAccountVo;
import me.ledge.link.api.vos.datapoints.VirtualCard;

/**
 * Created by adrian on 25/01/2017.
 */

public class FinancialAccountParser implements JsonDeserializer<FinancialAccountVo> {
    @Override
    public FinancialAccountVo deserialize(JsonElement json, Type iType, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jObject = json.getAsJsonObject();
        String type = ParsingUtils.getStringFromJson(jObject.get("type"));
        if(type == null) {
            return null;
        }
        if(type.equalsIgnoreCase("card")) {
            return new Card(jObject.get("account_id").getAsString(),
                    Card.CardNetwork.valueOf(jObject.get("card_network").getAsString()),
                    ParsingUtils.getStringFromJson(jObject.get("pan")),
                    ParsingUtils.getStringFromJson(jObject.get("cvv_number")),
                    ParsingUtils.getStringFromJson(jObject.get("last_four_digits")),
                    ParsingUtils.getStringFromJson(jObject.get("expiration")), false);
        }
        else if(type.equalsIgnoreCase("bank_account")) {
            return new BankAccount(jObject.get("account_id").getAsString(),
                    jObject.get("bank_name").getAsString(),
                    jObject.get("last_four_digits").getAsString(), false);
        }
        else if(type.equalsIgnoreCase("virtual_card")){
            return new VirtualCard(jObject.get("account_id").getAsString(),
                    Card.CardNetwork.valueOf(ParsingUtils.getStringFromJson(jObject.get("card_network"))),
                    ParsingUtils.getStringFromJson(jObject.get("pan")),
                    ParsingUtils.getStringFromJson(jObject.get("cvv_number")),
                    ParsingUtils.getStringFromJson(jObject.get("last_four_digits")),
                    ParsingUtils.getStringFromJson(jObject.get("expiration")), false);
        }
        else {
            return null;
        }
    }
}