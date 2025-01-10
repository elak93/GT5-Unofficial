package galacticgreg.api.enums;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderHell;

import galacticgreg.api.Enums.DimensionType;
import galacticgreg.api.ModDimensionDef;
import gtPlusPlus.everglades.chunk.ChunkProviderModded;

public enum DimensionDef {

    // spotless:off
    Overworld(new ModDimensionDef(
        DimNames.OW,
        ChunkProviderGenerate.class,
        DimensionType.Planet)),
    Nether(new ModDimensionDef(
        DimNames.NETHER,
        ChunkProviderHell.class,
        DimensionType.Planet)),
    TheEnd(new ModDimensionDef(
        DimNames.THE_END,
        ChunkProviderEnd.class,
        DimensionType.Planet)),
    EndAsteroids(new ModDimensionDef(
        DimNames.ENDASTEROID,
        ChunkProviderEnd.class,
        DimensionType.Asteroid)),
    TwilightForest(new ModDimensionDef(
        DimNames.TWILIGHT_FOREST,
        "twilightforest.world.ChunkProviderTwilightForest",
        DimensionType.Planet)),
    Everglades(new ModDimensionDef(
        DimNames.EVERGLADES,
        ChunkProviderModded.class,
        DimensionType.Planet)
        .setOreVeinChance(66)
        .disableEoHRecipe()),


    Moon(new ModDimensionDef(
        DimNames.MOON,
        "micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderMoon",
        DimensionType.Planet)),
    Mars(new ModDimensionDef(
        DimNames.MARS,
        "micdoodle8.mods.galacticraft.planets.mars.world.gen.ChunkProviderMars",
        DimensionType.Planet)),
    Asteroids(new ModDimensionDef(
        DimNames.ASTEROIDS,
        "micdoodle8.mods.galacticraft.planets.asteroids.world.gen.ChunkProviderAsteroids",
        DimensionType.Asteroid)
        .disableVoidMining()),
    Ross128b(new ModDimensionDef(
        DimNames.ROSS128B,
        "bwcrossmod.galacticraft.planets.ross128b.ChunkProviderRoss128b",
        DimensionType.Planet)
        .disableEoHRecipe()),
    Ross128ba(new ModDimensionDef(
        DimNames.ROSS128BA,
        "bwcrossmod.galacticraft.planets.ross128ba.ChunkProviderRoss128ba",
        DimensionType.Planet)
        .disableEoHRecipe()),
    Pluto(new ModDimensionDef(
        DimNames.PLUTO,
        "galaxyspace.SolarSystem.planets.pluto.dimension.ChunkProviderPluto",
        DimensionType.Planet)),
    Triton(new ModDimensionDef(
        DimNames.TRITON,
        "galaxyspace.SolarSystem.moons.triton.dimension.ChunkProviderTriton",
        DimensionType.Planet)),
    Proteus(new ModDimensionDef(
        DimNames.PROTEUS,
        "galaxyspace.SolarSystem.moons.proteus.dimension.ChunkProviderProteus",
        DimensionType.Planet)),
    Oberon(new ModDimensionDef(
        DimNames.OBERON,
        "galaxyspace.SolarSystem.moons.oberon.dimension.ChunkProviderOberon",
        DimensionType.Planet)),
    Titan(new ModDimensionDef(
        DimNames.TITAN,
        "galaxyspace.SolarSystem.moons.titan.dimension.ChunkProviderTitan",
        DimensionType.Planet)),
    Callisto(new ModDimensionDef(
        DimNames.CALLISTO,
        "galaxyspace.SolarSystem.moons.callisto.dimension.ChunkProviderCallisto",
        DimensionType.Planet)),
    Ganymede(new ModDimensionDef(
        DimNames.GANYMEDE,
        "galaxyspace.SolarSystem.moons.ganymede.dimension.ChunkProviderGanymede",
        DimensionType.Planet)),
    Ceres(new ModDimensionDef(
        DimNames.CERES,
        "galaxyspace.SolarSystem.planets.ceres.dimension.ChunkProviderCeres",
        DimensionType.Planet)),
    Deimos(new ModDimensionDef(
        DimNames.DEIMOS,
        "galaxyspace.SolarSystem.moons.deimos.dimension.ChunkProviderDeimos",
        DimensionType.Planet)),
    Enceladus(new ModDimensionDef(
        DimNames.ENCELADUS,
        "galaxyspace.SolarSystem.moons.enceladus.dimension.ChunkProviderEnceladus",
        DimensionType.Planet)),
    Io(new ModDimensionDef(
        DimNames.IO,
        "galaxyspace.SolarSystem.moons.io.dimension.ChunkProviderIo",
        DimensionType.Planet)),
    Europa(new ModDimensionDef(
        DimNames.EUROPA,
        "galaxyspace.SolarSystem.moons.europa.dimension.ChunkProviderEuropa",
        DimensionType.Planet)),
    Phobos(new ModDimensionDef(
        DimNames.PHOBOS,
        "galaxyspace.SolarSystem.moons.phobos.dimension.ChunkProviderPhobos",
        DimensionType.Planet)),
    Venus(new ModDimensionDef(
        DimNames.VENUS,
        "galaxyspace.SolarSystem.planets.venus.dimension.ChunkProviderVenus",
        DimensionType.Planet)),
    Mercury(new ModDimensionDef(
        DimNames.MERCURY,
        "galaxyspace.SolarSystem.planets.mercury.dimension.ChunkProviderMercury",
        DimensionType.Planet)),
    MakeMake(new ModDimensionDef(
        DimNames.MAKEMAKE,
        "galaxyspace.SolarSystem.planets.makemake.dimension.ChunkProviderMakemake",
        DimensionType.Planet)),
    Haumea(new ModDimensionDef(
        DimNames.HAUMEA,
        "galaxyspace.SolarSystem.planets.haumea.dimension.ChunkProviderHaumea",
        DimensionType.Planet)),
    CentauriAlpha(new ModDimensionDef(
        DimNames.CENTAURIA,
        "galaxyspace.ACentauriSystem.planets.aCentauriBb.dimension.ChunkProviderACentauri",
        DimensionType.Planet)),
    VegaB(new ModDimensionDef(
        DimNames.VEGAB,
        "galaxyspace.VegaSystem.planets.vegaB.dimension.ChunkProviderVegaB",
        DimensionType.Planet)),
    BarnardC(new ModDimensionDef(
        DimNames.BARNARDC,
        "galaxyspace.BarnardsSystem.planets.barnardaC.dimension.ChunkProviderBarnardaC",
        DimensionType.Planet)),
    BarnardE(new ModDimensionDef(
        DimNames.BARNARDE,
        "galaxyspace.BarnardsSystem.planets.barnardaE.dimension.ChunkProviderBarnardaE",
        DimensionType.Planet)),
    BarnardF(new ModDimensionDef(
        DimNames.BARNARDF,
        "galaxyspace.BarnardsSystem.planets.barnardaF.dimension.ChunkProviderBarnardaF",
        DimensionType.Planet)),
    TcetiE(new ModDimensionDef(
        DimNames.TCETIE,
        "galaxyspace.TCetiSystem.planets.tcetiE.dimension.ChunkProviderTCetiE",
        DimensionType.Planet)),
    Miranda(new ModDimensionDef(
        DimNames.MIRANDA,
        "galaxyspace.SolarSystem.moons.miranda.dimension.ChunkProviderMiranda",
        DimensionType.Planet)),
    KuiperBelt(new ModDimensionDef(
        DimNames.KUIPERBELT,
        "galaxyspace.SolarSystem.planets.kuiperbelt.dimension.ChunkProviderKuiper",
        DimensionType.Asteroid)
        .disableVoidMining()),

    Neper(new ModDimensionDef(
        DimNames.NEPER,
        "de.katzenpapst.amunra.world.neper.NeperChunkProvider",
        DimensionType.Planet)),
    Maahes(new ModDimensionDef(
        DimNames.MAAHES,
        "de.katzenpapst.amunra.world.maahes.MaahesChunkProvider",
        DimensionType.Planet)),
    Anubis(new ModDimensionDef(
        DimNames.ANUBIS,
        "de.katzenpapst.amunra.world.anubis.AnubisChunkProvider",
        DimensionType.Planet)),
    Horus(new ModDimensionDef(
        DimNames.HORUS,
        "de.katzenpapst.amunra.world.horus.HorusChunkProvider",
        DimensionType.Planet)),
    Seth(new ModDimensionDef(
        DimNames.SETH,
        "de.katzenpapst.amunra.world.seth.SethChunkProvider",
        DimensionType.Planet)),
    MehenBelt(new ModDimensionDef(
        DimNames.MEHENBELT,
        "de.katzenpapst.amunra.world.mehen.MehenChunkProvider",
        DimensionType.Asteroid)
        .disableVoidMining()),

    DeepDark(new ModDimensionDef(
        DimNames.DEEPDARK,
        "",
        DimensionType.Planet)),
    ;
    // spotless:on

    public final ModDimensionDef modDimensionDef;

    DimensionDef(ModDimensionDef modDimDef) {
        this.modDimensionDef = modDimDef;
    }

    private static final Map<String, ModDimensionDef> DEF_BY_WORLD_NAME = new HashMap<>();

    static {
        for (DimensionDef def : values()) {
            DEF_BY_WORLD_NAME.put(def.modDimensionDef.getDimensionName(), def.modDimensionDef);
        }
    }

    public static ModDimensionDef getDefByName(String worldName) {
        return DEF_BY_WORLD_NAME.get(worldName);
    }

    public static ModDimensionDef getDefForWorld(World world, int chunkX, int chunkZ) {
        ModDimensionDef def = DEF_BY_WORLD_NAME.get(world.provider.getDimensionName());

        if (def == null) return null;

        if (def.getDimensionName()
            .equals(DimNames.THE_END)) {
            if (chunkX * chunkX + chunkZ * chunkZ > 16 * 16) {
                def = DimensionDef.EndAsteroids.modDimensionDef;
            }
        }

        return def;
    }

    public static class DimNames {

        public static final String OW = "Overworld";
        public static final String NETHER = "Nether";
        public static final String THE_END = "The End";
        public static final String ENDASTEROID = "EndAsteroid";
        public static final String TWILIGHT_FOREST = "Twilight Forest";
        public static final String EVERGLADES = "dimensionDarkWorld";

        public static final String MOON = "Moon";
        public static final String MARS = "Mars";
        public static final String ASTEROIDS = "Asteroids";
        public static final String PLUTO = "Pluto";
        public static final String TRITON = "Triton";
        public static final String PROTEUS = "Proteus";
        public static final String OBERON = "Oberon";
        public static final String TITAN = "Titan";
        public static final String ROSS128B = "Ross128b";
        public static final String ROSS128BA = "Ross128ba";
        public static final String CALLISTO = "Callisto";
        public static final String GANYMEDE = "Ganymede";
        public static final String CERES = "Ceres";
        public static final String DEIMOS = "Deimos";
        public static final String ENCELADUS = "Enceladus";
        public static final String IO = "Io";
        public static final String EUROPA = "Europa";
        public static final String PHOBOS = "Phobos";
        public static final String VENUS = "Venus";
        public static final String MERCURY = "Mercury";
        public static final String MAKEMAKE = "MakeMake";
        public static final String HAUMEA = "Haumea";
        public static final String CENTAURIA = "CentauriA";
        public static final String VEGAB = "VegaB";
        public static final String BARNARDC = "BarnardC";
        public static final String BARNARDE = "BarnardE";
        public static final String BARNARDF = "BarnardF";
        public static final String TCETIE = "TcetiE";
        public static final String MIRANDA = "Miranda";
        public static final String KUIPERBELT = "Kuiper Belt";
        public static final String NEPER = "Neper";
        public static final String MAAHES = "Maahes";
        public static final String ANUBIS = "Anubis";
        public static final String HORUS = "Horus";
        public static final String SETH = "Seth";
        public static final String MEHENBELT = "Mehen Belt";
        public static final String DEEPDARK = "Underdark";

    }
}
