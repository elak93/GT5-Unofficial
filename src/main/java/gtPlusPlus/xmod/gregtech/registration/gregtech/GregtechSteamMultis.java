package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.addItemTooltip;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamAlloySmelterMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamCentrifugeMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamCompressorMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamExtruder;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamForgeHammer;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamGateAssembler;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMaceratorMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMegaMaceratorMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMixerMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamRockBreaker;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamWasherMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_Steamgate;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Bus_Steam;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Output_Bus_Steam;

import gregtech.api.enums.GTValues;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamAlloySmelter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamCentrifuge;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamCompressor;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamExtruder;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamForgeHammer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamGateAssembler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMegaMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMixer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamRockBreaker;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamWasher;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamgate;

public class GregtechSteamMultis {

    public static void run() {

        Logger.INFO("Gregtech5u Content | Registering Steam Multiblocks.");

        GregtechItemList.Controller_SteamMaceratorMulti.set(
            new MTESteamMacerator(
                Controller_SteamMaceratorMulti.ID,
                "gtpp.multimachine.steam.macerator",
                "Steam Grinder").getStackForm(1L));
        GregtechItemList.Controller_SteamMegaMaceratorMulti.set(
            new MTESteamMegaMacerator(
                Controller_SteamMegaMaceratorMulti.ID,
                "gtpp.multimachine.steam.megamacerator",
                "Mega Steam Grinder").getStackForm(1L));
        GregtechItemList.Controller_SteamCompressorMulti.set(
            new MTESteamCompressor(
                Controller_SteamCompressorMulti.ID,
                "gtpp.multimachine.steam.compressor",
                "Steam Squasher").getStackForm(1L));
        GregtechItemList.Controller_SteamCentrifugeMulti.set(
            new MTESteamCentrifuge(
                Controller_SteamCentrifugeMulti.ID,
                "gtpp.multimachine.steam.centrifuge",
                "Steam Separator").getStackForm(1));
        GregtechItemList.Controller_SteamWasherMulti.set(
            new MTESteamWasher(Controller_SteamWasherMulti.ID, "gtpp.multimachine.steam.washer", "Steam Purifier")
                .getStackForm(1));
        GregtechItemList.Controller_SteamForgeHammerMulti.set(
            new MTESteamForgeHammer(
                Controller_SteamForgeHammer.ID,
                "gtpp.multimachine.steam.forge.hammer",
                "Steam Presser").getStackForm(1));
        GregtechItemList.Controller_SteamMixerMulti.set(
            new MTESteamMixer(Controller_SteamMixerMulti.ID, "gtpp.multimachine.steam.mixer", "Steam Blender")
                .getStackForm(1));
        GregtechItemList.Controller_SteamAlloySmelterMulti.set(
            new MTESteamAlloySmelter(
                Controller_SteamAlloySmelterMulti.ID,
                "gtpp.multimachine.steam.alloysmelter",
                "Steam Fuser").getStackForm(1));
        GregtechItemList.Controller_SteamGateAssembler.set(
            new MTESteamGateAssembler(
                Controller_SteamGateAssembler.ID,
                "gtpp.multimachine.steam.gateassembler",
                "Perfect Steam Progenitor").getStackForm(1));
        GregtechItemList.Controller_Steamgate.set(
            new MTESteamgate(Controller_Steamgate.ID, "gtpp.multimachine.steamgate", "Steamgate Base Block")
                .getStackForm(1));
        GregtechItemList.Controller_SteamExtruder.set(
            new MTESteamExtruder(Controller_SteamExtruder.ID, "gtpp.multimachine.steam.extruder", "Steam Conformer")
                .getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_SteamExtruder.get(1), GTValues.AuthorNoc);
        GregtechItemList.Controller_SteamRockBreaker.set(
            new MTESteamRockBreaker(
                Controller_SteamRockBreaker.ID,
                "gtpp.multimachine.steam.rockbreaker",
                "Steam Cobbler").getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_SteamRockBreaker.get(1), GTValues.AuthorNoc);

        GregtechItemList.Hatch_Input_Bus_Steam.set(
            new MTEHatchSteamBusInput(Hatch_Input_Bus_Steam.ID, "hatch.input_bus.tier.steam", "Input Bus (Steam)", 0)
                .getStackForm(1L));
        GregtechItemList.Hatch_Output_Bus_Steam.set(
            new MTEHatchSteamBusOutput(
                Hatch_Output_Bus_Steam.ID,
                "hatch.output_bus.tier.steam",
                "Output Bus (Steam)",
                0).getStackForm(1L));
    }
}
