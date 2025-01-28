package gregtech.common.gui.modularui2.value;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.value.sync.ValueSyncHandler;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.common.covers.Cover;

public class CoverDataSyncHandler<T extends Cover> extends ValueSyncHandler<T> {

    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private T cache;
    private final ICoverable coverable;

    public CoverDataSyncHandler(CoverGuiData guiData) {
        this.coverable = guiData.getCoverable();
        // noinspection unchecked
        this.getter = () -> (T) guiData.getCoverData();
        this.setter = guiData::setCoverData;
        this.cache = this.getter.get();
    }

    @Override
    public void setValue(T value, boolean setSource, boolean sync) {
        this.cache = value;
        if (setSource) {
            this.setter.accept(value);
        }
        if (sync) {
            sync(0, this::write);
        }
    }

    @Override
    public boolean updateCacheFromSource(boolean isFirstSync) {
        if (isFirstSync || !this.getter.get()
            .equals(this.cache)) {
            setValue(this.getter.get(), false, false);
            return true;
        }
        return false;
    }

    @Override
    public void write(PacketBuffer buffer) throws IOException {
        getValue().writeToByteBuf(buffer);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void read(PacketBuffer buffer) throws IOException {
        ByteArrayDataInput data = ByteStreams.newDataInput(buffer.array());
        int coverId = data.readInt();
        ForgeDirection side = ForgeDirection.getOrientation(data.readByte());
        // noinspection unchecked
        T cover = (T) CoverRegistry.getRegistration(coverId)
            .buildCover(side, coverable, data);
        setValue(cover, true, false);
    }

    @Override
    public T getValue() {
        return cache;
    }
}
