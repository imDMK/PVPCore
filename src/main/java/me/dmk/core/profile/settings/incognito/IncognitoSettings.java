package me.dmk.core.profile.settings.incognito;

import lombok.Data;
import me.dmk.core.CorePlugin;
import me.dmk.core.util.string.StringUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * Created by DMK on 12.01.2023
 */

@Data
public class IncognitoSettings implements Serializable {

    private boolean enabled = false;
    private String identifier = StringUtil.generateRandomString(8);
    private Date lastResetIdentifier = null;

    private transient long timeToResetIdentifier = CorePlugin.getCorePlugin().getPluginConfiguration().getTimeToResetIdentifier();

    public void toggle() {
        this.enabled = !enabled;
    }

    public String changeIdentifier() {
        this.lastResetIdentifier = Date.from(Instant.now().plusSeconds(this.timeToResetIdentifier));
        return this.identifier = StringUtil.generateRandomString(8);
    }

    public boolean canChangeIdentifier() {
        if (this.lastResetIdentifier == null) {
            return true;
        }

        return Instant.now().isAfter(this.lastResetIdentifier.toInstant());
    }

    public Instant getWhenCanChange()  {
        return this.lastResetIdentifier.toInstant();
    }
}
