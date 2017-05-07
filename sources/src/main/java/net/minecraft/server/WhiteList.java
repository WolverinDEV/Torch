package net.minecraft.server;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

import org.torch.server.Caches;

import static net.minecraft.server.UserCache.isOnlineMode;

public class WhiteList extends JsonList<GameProfile, WhiteListEntry> {

    public WhiteList(File file) {
        super(file);
    }

    @Override
    protected JsonListEntry<GameProfile> a(JsonObject jsonobject) {
        return new WhiteListEntry(jsonobject);
    }

    public boolean isWhitelisted(GameProfile profile) {
        return this.contains(profile);
    }

    @Override
    public String[] getEntries() {
        String[] values = new String[this.getMap().size()]; int index = 0;
        for (WhiteListEntry entry : this.getMap().values()) values[index++] = entry.getKey().getName();

        return values;
    }

    protected String b(GameProfile profile) {
        return isOnlineMode() ? Caches.objectString(profile) : Caches.toLowerCase(profile.getName()); // Torch - use cache
    }

    public GameProfile a(String s) {
        Iterator<WhiteListEntry> iterator = this.e().values().iterator();

        WhiteListEntry whitelistentry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            whitelistentry = iterator.next();
        } while (!s.equalsIgnoreCase(whitelistentry.getKey().getName()));

        return whitelistentry.getKey();
    }

    @Override
    protected String a(GameProfile object) {
        return this.b(object);
    }
}
