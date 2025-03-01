package gregtech.api.interfaces.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.Cover;

public interface ICoverable extends IRedstoneTileEntity, IHasInventory, IBasicEnergyContainer {

    /**
     * Remove the cover from the coverable and spawn the result of detachCover in the world on the dropped side.
     */
    void dropCover(ForgeDirection side, ForgeDirection droppedSide);

    /**
     * Actually removes the cover from the coverable and return the cover item. Called by dropCover.
     */
    ItemStack detachCover(ForgeDirection side);

    void setCoverDataAtSide(ForgeDirection side, ISerializableObject aData);

    /**
     * Called when the cover is initially attached to a machine.
     *
     * @param cover The cover
     * @param side  Which side the cover is attached to
     */
    void attachCover(Cover cover, ForgeDirection side);

    boolean hasCoverAtSide(ForgeDirection side);

    Cover getCoverAtSide(ForgeDirection side);

    int getCoverIDAtSide(ForgeDirection side);

    ItemStack getCoverItemAtSide(ForgeDirection side);

    /**
     * For use by the regular MetaTileEntities. Returns the Cover Manipulated input Redstone. Don't use this if you are
     * a Cover Behavior. Only for MetaTileEntities.
     */
    byte getInternalInputRedstoneSignal(ForgeDirection side);

    /**
     * For use by the regular MetaTileEntities. This makes it not conflict with Cover based Redstone Signals. Don't use
     * this if you are a Cover Behavior. Only for MetaTileEntities.
     */
    void setInternalOutputRedstoneSignal(ForgeDirection side, byte aStrength);

    /**
     * Causes a general Cover Texture update. Sends 6 Integers to Client + causes @issueTextureUpdate()
     */
    void issueCoverUpdate(ForgeDirection side);

    /**
     * Receiving a packet with cover data.
     *
     * @param cover
     * @param side  cover side
     */
    void updateCover(Cover cover, ForgeDirection side);
}
