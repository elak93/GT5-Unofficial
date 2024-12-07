package gtneioreplugin;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Version;
import gregtech.api.enums.Materials;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.NEIPluginConfig;
import gtneioreplugin.plugin.block.ModBlocks;
import gtneioreplugin.util.CSVMaker;
import gtneioreplugin.util.GT5OreLayerHelper;
import gtneioreplugin.util.GT5OreSmallHelper;
import gtneioreplugin.util.GT5UndergroundFluidHelper;

@Mod(
    modid = GTNEIOrePlugin.MODID,
    name = GTNEIOrePlugin.NAME,
    version = GTNEIOrePlugin.VERSION,
    dependencies = "required-after:gregtech;required-after:NotEnoughItems")
public class GTNEIOrePlugin {

    static {
        try {
            ConfigurationManager.registerConfig(Config.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String MODID = "gtneioreplugin";
    public static final String NAME = "GT NEI Ore Plugin GT:NH Mod";
    public static final String VERSION = GT_Version.VERSION;
    public static final Logger LOG = LogManager.getLogger(NAME);
    public static File instanceDir;
    public static final CreativeTabs creativeTab = new CreativeTabs(MODID) {

        @SideOnly(Side.CLIENT)
        public ItemStack getIconItemStack() {
            try (OreInfo<Materials> info = OreInfo.getNewInfo()) {
                info.material = Materials.Manyullyn;
                
                return OreManager.getStack(info, 1);
            }
        };

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(GTOreAdapter.INSTANCE.ores1);
        }
    };

    @Mod.Instance(MODID)
    public static GTNEIOrePlugin instance;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        instanceDir = event.getModConfigurationDirectory()
            .getParentFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModBlocks.init();
        MinecraftForge.EVENT_BUS.register(new NEIPluginConfig());
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        GT5OreLayerHelper.init();
        GT5OreSmallHelper.init();
        GT5UndergroundFluidHelper.init();
        if (event.getSide() == Side.CLIENT) {
            if (Config.printCsv) {
                new CSVMaker().run();
            }
        }
    }
}
