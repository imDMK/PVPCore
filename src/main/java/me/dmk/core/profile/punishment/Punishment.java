package me.dmk.core.profile.punishment;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Date;

/**
 * Created by DMK on 29.12.2022
 */

@Data
public class Punishment {

    private final PunishmentType type;

    private final String addedBy;
    private final String reason;

    private Date createdAt;
    @Nullable private Date expireAt;

    private boolean removed;
    @Nullable private String removedBy;
    @Nullable private Date removedAt;

    public Punishment(PunishmentType punishmentType, String addedBy, String reason, Instant expire) {
        this.type = punishmentType;
        this.addedBy = addedBy;
        this.reason = reason;
        this.createdAt = new Date();
        this.expireAt = Date.from(expire);

        this.removed = false;
        this.removedBy = null;
        this.removedAt = null;
    }

    public Punishment(PunishmentType punishmentType, String addedBy, String reason) {
        this.type = punishmentType;
        this.addedBy = addedBy;
        this.reason = reason;

        this.createdAt = new Date();
        this.expireAt = null;

        this.removed = false;
        this.removedBy = null;
        this.removedAt = null;
    }

    public boolean isPermanent() {
        return this.expireAt == null;
    }

    public boolean isActive() {
        if (this.removed) {
            return false;
        }
        if (this.expireAt == null) {
            return true;
        }
        return Instant.now().isBefore(this.expireAt.toInstant());
    }
}
