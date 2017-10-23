package me.ledge.link.api.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.ledge.link.api.vos.responses.config.ContentVo;
import me.ledge.link.api.vos.responses.workflow.CallToActionVo;
import me.ledge.link.api.vos.responses.workflow.ActionConfigurationVo;
import me.ledge.link.api.vos.responses.workflow.GenericMessageConfigurationVo;

/**
 * Created by adrian on 25/01/2017.
 */

public class ActionConfigurationParser implements JsonDeserializer<ActionConfigurationVo> {
    @Override
    public ActionConfigurationVo deserialize(JsonElement json, Type iType, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject config = json.getAsJsonObject();
        if(config == null) {
            return null;
        }

        String type = ParsingUtils.getStringFromJson(config.get("type"));
        if (type != null) {
            switch (type) {
                case "action_generic_message_config":
                    return parseGenericMessageConfig(config);
            }
        }
        return null;
    }

    private static ContentVo parseContent(JsonObject content) {
        String contentType = ParsingUtils.getStringFromJson(content.get("type"));
        String contentFormat = ParsingUtils.getStringFromJson(content.get("format"));
        String contentValue = ParsingUtils.getStringFromJson(content.get("value"));

        return new ContentVo(contentType, contentFormat, contentValue);
    }

    private static CallToActionVo parseCallToAction(JsonObject callToAction) {
        String type = ParsingUtils.getStringFromJson(callToAction.get("type"));
        String title = ParsingUtils.getStringFromJson(callToAction.get("title"));
        String actionType = ParsingUtils.getStringFromJson(callToAction.get("action_type"));
        String externalUrl = ParsingUtils.getStringFromJson(callToAction.get("external_url"));
        String trackerClickEventName = ParsingUtils.getStringFromJson(callToAction.get("tracker_click_event_name"));
        String trackerIncrementName = ParsingUtils.getStringFromJson(callToAction.get("tracker_increment_name"));

        return new CallToActionVo(type, title, actionType, externalUrl, trackerClickEventName, trackerIncrementName);
    }

    private GenericMessageConfigurationVo parseGenericMessageConfig(JsonObject config) {
        String type = config.get("type").getAsString();
        String title = ParsingUtils.getStringFromJson(config.get("title"));
        String image = ParsingUtils.getStringFromJson(config.get("image"));
        String trackerEventName = ParsingUtils.getStringFromJson(config.get("tracker_event_name"));
        String trackerIncrementName = ParsingUtils.getStringFromJson(config.get("tracker_increment_name"));

        ContentVo content = parseContent(config.get("content").getAsJsonObject());
        CallToActionVo callToAction = parseCallToAction(config.get("call_to_action").getAsJsonObject());

        return new GenericMessageConfigurationVo(type, title, content, image, trackerEventName, trackerIncrementName, callToAction);
    }
}