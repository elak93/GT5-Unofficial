package gregtech.common.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityOres extends TileEntity {

    public short mMetaData = 0;
    public boolean mNatural = false;

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        this.mMetaData = aNBT.getShort("m");
        this.mNatural = aNBT.getBoolean("n");
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
        aNBT.setShort("m", this.mMetaData);
        aNBT.setBoolean("n", this.mNatural);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    /** Temporary stub method to reduce compilation errors */
    public static boolean setOreBlock(World world, int x, int y, int z, int meta, boolean small) {
        return false;
    }

    public static boolean setOreBlock(World world, int x, int y, int z, short meta, boolean small, boolean checkAir) {
        return false;
    }
}
