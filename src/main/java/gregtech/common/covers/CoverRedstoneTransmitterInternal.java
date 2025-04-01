package gregtech.common.covers;

import java.util.Objects;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverRedstoneTransmitterInternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneTransmitterInternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void onCoverRemoval() {
        GregTechAPI.sWirelessRedstone.remove(coverData);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        GregTechAPI.sWirelessRedstone.put(coverData, coverable.getOutputRedstoneSignal(coverSide));
    }

    @Override
    public void preDataChanged(Cover newCover) {
        if (newCover instanceof CoverRedstoneTransmitterInternal newTransmitterCover
            && !Objects.equals(coverData, newTransmitterCover.coverData)) {
            GregTechAPI.sWirelessRedstone.remove(coverData);
        }
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }
}
