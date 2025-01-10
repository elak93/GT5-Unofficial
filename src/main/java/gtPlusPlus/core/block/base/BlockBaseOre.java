package gtPlusPlus.core.block.base;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.ores.GTPPOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.common.render.GTRendererBlock;
import gtPlusPlus.core.item.base.itemblock.ItemBlockOre;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BlockBaseOre extends BasicBlock implements IBlockWithTextures {

    private final Material blockMaterial;

    public BlockBaseOre(final Material material, final BlockTypes blockType) {
        super(
            blockType,
            Utils.sanitizeString(material.getUnlocalizedName()),
            net.minecraft.block.material.Material.rock,
            Math.min(Math.max(material.vTier, 1), 6));
        int aMaterialTierForMining = Math.min(Math.max(material.vTier, 1), 6);
        this.blockMaterial = material;
        this.setHardness(1.0f * aMaterialTierForMining);
        this.setResistance(6.0F);
        this.setLightLevel(0.0F);
        this.setHarvestLevel("pickaxe", aMaterialTierForMining);
        this.setStepSound(soundTypeStone);
        this.setBlockName("Ore" + Utils.sanitizeString(Utils.sanitizeString(material.getUnlocalizedName())));
        this.setBlockTextureName("stone");
        try {
            GameRegistry.registerBlock(
                this,
                ItemBlockOre.class,
                Utils.sanitizeString("ore" + Utils.sanitizeString(this.blockMaterial.getLocalizedName())));
            GTOreDictUnificator.registerOre(
                "ore" + Utils.sanitizeString(this.blockMaterial.getLocalizedName()),
                ItemUtils.getSimpleStack(this));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y,
        final int z) {
        return false;
    }

    public Material getMaterialEx() {
        return this.blockMaterial;
    }

    @Override
    public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int ordinalSide) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.mRenderID;
    }

    @Override
    @Nullable
    public ITexture[][] getTextures(int metadata) {
        ITexture[] layers;

        ITexture aIconSet = TextureFactory
            .of(blockMaterial.getTextureSet().mTextures[OrePrefixes.ore.mTextureIndex], blockMaterial.getRGBA());
        layers = new ITexture[] { StoneType.Stone.getTexture(0), aIconSet };

        return new ITexture[][] { layers, layers, layers, layers, layers, layers };
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {}

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        EntityPlayer harvester = this.harvesters.get();

        boolean doFortune = !(harvester instanceof FakePlayer);
        boolean doSilktouch = harvester != null && EnchantmentHelper.getSilkTouchModifier(harvester);

        try (OreInfo<Material> info = GTPPOreAdapter.INSTANCE.getOreInfo(this, metadata)) {
            if (info == null) return new ArrayList<>();

            return GTPPOreAdapter.INSTANCE
                .getOreDrops(ThreadLocalRandom.current(), info, doSilktouch, doFortune ? fortune : 0);
        }
    }
}
