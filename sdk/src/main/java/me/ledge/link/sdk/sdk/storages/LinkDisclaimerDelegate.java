package me.ledge.link.sdk.sdk.storages;

import me.ledge.link.api.vos.responses.config.LinkDisclaimerVo;

/**
 * Delegation interface for accessing the link disclaimer stored in the Config Storage.
 *
 * @author Adrian
 */
public interface LinkDisclaimerDelegate {

    void linkDisclaimersRetrieved(LinkDisclaimerVo linkDisclaimer);

    void errorReceived(String error);
}
