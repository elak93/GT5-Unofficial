package gregtech.common.tileentities.machines.multi.nanochip;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.MAIN_OFFSET_X;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.MAIN_OFFSET_Y;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.MAIN_OFFSET_Z;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import appeng.api.AEApi;
import appeng.tile.crafting.TileCraftingStorageTile;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_CraftingInput_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_InputBus_ME;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.GT_MetaTileEntity_Hatch_VacuumConveyor;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.GT_MetaTileEntity_Hatch_VacuumConveyor_Output;
import gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import gregtech.common.tileentities.machines.multi.nanochip.util.ItemStackWithSourceBus;
import gregtech.common.tileentities.machines.multi.nanochip.util.VacuumConveyorHatchMap;

public class GT_MetaTileEntity_NanochipAssemblyComplex
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_NanochipAssemblyComplex>
    implements ISurvivalConstructable {

    public static final String STRUCTURE_PIECE_MAIN = "main";
    public static final int CASING_INDEX_BASE = GregTech_API.getCasingTextureIndex(GregTech_API.sBlockCasings8, 10);
    public static final int CASING_INDEX_WHITE = GregTech_API.getCasingTextureIndex(GregTech_API.sBlockCasings8, 5);

    public static final IStructureDefinition<GT_MetaTileEntity_NanochipAssemblyComplex> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_NanochipAssemblyComplex>builder()
        .addShape(STRUCTURE_PIECE_MAIN, AssemblyComplexStructureString.MAIN_STRUCTURE)
        .addElement('A', ofBlock(GregTech_API.sBlockCasings1, 14))
        // Vacuum conveyor hatches that the main controller cares about go in specific slots
        .addElement(
            'B',
            GT_HatchElementBuilder.<GT_MetaTileEntity_NanochipAssemblyComplex>builder()
                .atLeastList(Arrays.asList(AssemblyHatchElement.VacuumConveyorHatch, InputBus, OutputBus))
                .casingIndex(CASING_INDEX_WHITE)
                .dot(2)
                .buildAndChain(ofBlock(GregTech_API.sBlockCasings8, 5)))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 5))
        .addElement('D', ofBlock(GregTech_API.sBlockCasings8, 10))
        // Either a white casing block or an ignored hatch (this hatch is on the module)
        .addElement(
            'E',
            GT_HatchElementBuilder.<GT_MetaTileEntity_NanochipAssemblyComplex>builder()
                .atLeast(AssemblyHatchElement.IgnoredHatch)
                .casingIndex(CASING_INDEX_WHITE)
                .dot(3)
                .buildAndChain(ofBlock(GregTech_API.sBlockCasings8, 5)))
        // Crafting storage block
        .addElement('F', ofBlock(getCraftingStorageBlock(), getCraftingStorageMeta()))
        // .addElement('F', ofSpecificTileAdder(GT_MetaTileEntity_NanochipAssemblyComplex::addCraftingStorage,
        // TileCraftingStorageTile.class, getCraftingStorageBlock(), getCraftingStorageMeta()))
        .addElement('G', ofFrame(Materials.Naquadah))
        // Energy Hatch
        .addElement(
            'L',
            GT_HatchElementBuilder.<GT_MetaTileEntity_NanochipAssemblyComplex>builder()
                .atLeast(Energy, ExoticEnergy)
                .casingIndex(CASING_INDEX_BASE)
                .dot(1)
                .buildAndChain(GregTech_API.sBlockCasings8, 10))
        // Module
        .addElement(
            'M',
            GT_HatchElementBuilder.<GT_MetaTileEntity_NanochipAssemblyComplex>builder()
                .atLeast(AssemblyHatchElement.AssemblyModule)
                .casingIndex(CASING_INDEX_BASE)
                .dot(1)
                // Base casing or assembly module
                .buildAndChain(GregTech_API.sBlockCasings8, 10))
        .build();

    public static final int MODULE_CONNECT_INTERVAL = 20;
    private static final int INTERNAL_BUFFER_MULTIPLIER = 8;

    private final ArrayList<GT_MetaTileEntity_NanochipAssemblyModuleBase<?>> modules = new ArrayList<>();

    private final VacuumConveyorHatchMap<GT_MetaTileEntity_Hatch_VacuumConveyor> vacuumConveyors = new VacuumConveyorHatchMap<>();

    public GT_MetaTileEntity_NanochipAssemblyComplex(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_NanochipAssemblyComplex(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, MAIN_OFFSET_X, MAIN_OFFSET_Y, MAIN_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            MAIN_OFFSET_X,
            MAIN_OFFSET_Y,
            MAIN_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    private GT_MetaTileEntity_Hatch getEnergyHatch() {
        if (this.mExoticEnergyHatches.isEmpty()) {
            if (this.mEnergyHatches.isEmpty()) return null;
            return this.mEnergyHatches.get(0);
        }
        return this.mExoticEnergyHatches.get(0);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        fixAllIssues();
        modules.clear();
        vacuumConveyors.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, MAIN_OFFSET_X, MAIN_OFFSET_Y, MAIN_OFFSET_Z)) return false;
        // At least most one energy hatch is accepted
        if (this.mEnergyHatches.isEmpty()) {
            return this.mExoticEnergyHatches.size() == 1;
        } else {
            return this.mEnergyHatches.size() == 1;
        }
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_NanochipAssemblyComplex> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return new GT_Multiblock_Tooltip_Builder().toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_NanochipAssemblyComplex(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public boolean addModuleToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_NanochipAssemblyModuleBase<?>module) {
            return modules.add(module);
        }
        return false;
    }

    public boolean addConveyorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_VacuumConveyor hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            return vacuumConveyors.addHatch(hatch);
        }
        return false;
    }

    public boolean ignoreAndAcceptHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            return true;
        }
        return false;
    }

    public boolean addCraftingStorage(TileCraftingStorageTile tile) {
        // Only accept 16384k storage components
        return tile.isStorage() && tile.getStorageBytes() >= 16384000;
    }

    private static Block getCraftingStorageBlock() {
        // Should never error on get()
        return AEApi.instance()
            .definitions()
            .blocks()
            .craftingStorage16384k()
            .maybeBlock()
            .get();
    }

    private static int getCraftingStorageMeta() {
        // Should never error on get()
        return AEApi.instance()
            .definitions()
            .blocks()
            .craftingStorage16384k()
            .maybeStack(1)
            .get()
            .getItemDamage();
    }

    /**
     * Callback that will be invoked when the controller is removed
     */
    @Override
    public void onRemoval() {
        // On destroying the controller block, all modules should be disconnected
        disconnectAll();
        super.onRemoval();
    }

    private void disconnectAll() {
        for (GT_MetaTileEntity_NanochipAssemblyModuleBase<?> module : modules) {
            module.disconnect();
        }
    }

    private ArrayList<ItemStackWithSourceBus> getStoredInputsWithBus() {
        // We need to replicate some behaviour of getStoredInputs() here to avoid duplicating items with stocking
        // buses, but we cannot call getStoredInputs() directly because the specific hatch the ItemStack is coming
        // from matters for routing the created circuit components

        ArrayList<ItemStackWithSourceBus> inputs = new ArrayList<>();
        Map<GT_Utility.ItemId, ItemStackWithSourceBus> inputsFromME = new HashMap<>();
        for (GT_MetaTileEntity_Hatch_InputBus bus : filterValidMTEs(this.mInputBusses)) {
            // Ignore crafting input buses
            if (bus instanceof GT_MetaTileEntity_Hatch_CraftingInput_ME) {
                continue;
            }

            // Same as the original implementation of getStoredInputs(), but keep track of the bus we found the input
            // in.
            IGregTechTileEntity te = bus.getBaseMetaTileEntity();
            boolean isMEBus = bus instanceof GT_MetaTileEntity_Hatch_InputBus_ME;
            for (int i = te.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack stack = te.getStackInSlot(i);
                if (stack != null) {
                    if (isMEBus) {
                        // Prevent the same item from different ME buses from being recognized
                        inputsFromME.put(GT_Utility.ItemId.createNoCopy(stack), new ItemStackWithSourceBus(stack, bus));
                    } else {
                        inputs.add(new ItemStackWithSourceBus(stack, bus));
                    }
                }
            }
        }
        // Now add all values from the ME input map
        inputs.addAll(inputsFromME.values());
        return inputs;
    }

    // Route circuit components to a set of hatches. Returns true if the components were routed successfully and the
    // stack
    // should be consumed
    private boolean routeToHatches(List<GT_MetaTileEntity_Hatch_VacuumConveyor> hatches, byte color,
        CircuitComponent component, int amount) {
        // If no hatches were passed, we can't route
        if (hatches == null) return false;
        // Find the first hatch that can be used for routing
        for (GT_MetaTileEntity_Hatch_VacuumConveyor hatch : filterValidMTEs(hatches)) {
            // Hatch must be an output
            if (hatch instanceof GT_MetaTileEntity_Hatch_VacuumConveyor_Output outputHatch) {
                // Ensure that the color matches the expected color, since hatches can be recolored in between rebuilds
                // of the hatch map
                if (outputHatch.getBaseMetaTileEntity()
                    .getColorization() != color) {
                    // If the color did not match, we found an inconsistency in the hatch map, so fix it instead
                    // of waiting for the next structure check
                    vacuumConveyors.fixConsistency();
                    continue;
                }
                // Now we can route our components to this hatch
                CircuitComponentPacket packet = new CircuitComponentPacket(component, amount);
                // Merge with the already existing hatch contents
                outputHatch.unifyPacket(packet);
                return true;
            }
        }
        return false;
    }

    private void processCircuitInputs() {
        ArrayList<ItemStackWithSourceBus> inputs = getStoredInputsWithBus();
        // For each stack in the input, try to find a matching circuit component and if so send it to the correct hatch
        for (ItemStackWithSourceBus stack : inputs) {
            // Find a conversion recipe
            GT_Recipe recipe = RecipeMaps.nanochipConversionRecipes.findRecipeQuery()
                .items(stack.stack)
                .find();
            if (recipe == null) continue;
            // If one existed, we have the component now
            CircuitComponent component = CircuitComponent.getFromFakeStack(recipe.mOutputs[0]);
            // Find destination hatch. Note that we already know that this bus is a valid MTE, see
            // getStoredInputsWithBus
            byte busColor = stack.bus.getBaseMetaTileEntity()
                .getColorization();
            ArrayList<GT_MetaTileEntity_Hatch_VacuumConveyor> destinationHatches = vacuumConveyors
                .findColoredHatches(busColor);
            // Try to route to the set of destination hatches
            boolean routed = routeToHatches(destinationHatches, busColor, component, stack.stack.stackSize);
            // If successful, consume the input
            if (routed) {
                this.depleteInput(stack.stack);
            }
        }
    }

    private void tryChargeInternalBuffer() {
        GT_MetaTileEntity_Hatch hatch = this.getEnergyHatch();
        if (hatch == null) return;

        long eut = this.getMaxInputEu();
        long euToAdd = Math.min(eut, hatch.getEUVar());
        if (hatch.getBaseMetaTileEntity()
            .decreaseStoredEnergyUnits(euToAdd, false)) {
            setEUVar(getEUVar() + euToAdd);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (isAllowedToWork()) {
                // Every running tick, try to charge the buffer in the controller,
                // because the MTE base class does not actually do this, but we need to in order to distribute the
                // power to the modules.
                tryChargeInternalBuffer();
                // If the complex is turned on, periodically reconnect modules.
                // This code can be extended to only connect modules based on tiers of the complex or other
                // conditions such as energy tier.
                if (aTick % MODULE_CONNECT_INTERVAL == 0) {
                    if (!modules.isEmpty()) {
                        long eutPerModule = this.getMaxInputEu() / modules.size();
                        long euToCharge = eutPerModule * MODULE_CONNECT_INTERVAL;
                        for (GT_MetaTileEntity_NanochipAssemblyModuleBase<?> module : modules) {
                            module.connect();
                            // Set available EU/t for this module, which is the total EU/t divided by the amount of
                            // modules,
                            // since each module can draw power equally (no mixed overclocks).
                            module.setAvailableEUt(eutPerModule);
                            // Charge the module with power
                            long availableEnergy = getEUVar();
                            if (availableEnergy > 0) {
                                setEUVar(
                                    Math.max(
                                        0,
                                        availableEnergy
                                            - module.increaseStoredEU(Math.min(euToCharge, availableEnergy))));
                            }
                        }
                    }
                }
            } else {
                // If the complex is turned off or unformed, disconnect all modules
                disconnectAll();
            }
        }
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        // Always keep the machine running, it doesn't run recipes directly.
        if (isAllowedToWork()) {
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 1 * SECONDS;

            // Inside checkProcessing we can safely consume inputs from hatches
            processCircuitInputs();

            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    public static void registerLocalName(ItemStack stack, CircuitComponent component) {
        component.fallbackLocalizedName = stack.getDisplayName();
    }

    @Override
    public long maxEUStore() {
        return INTERNAL_BUFFER_MULTIPLIER * super.maxEUStore();
    }

    // Hatch adder for modules
    public enum AssemblyHatchElement implements IHatchElement<GT_MetaTileEntity_NanochipAssemblyComplex> {

        AssemblyModule(GT_MetaTileEntity_NanochipAssemblyComplex::addModuleToMachineList,
            GT_MetaTileEntity_NanochipAssemblyComplex.class) {

            @Override
            public long count(GT_MetaTileEntity_NanochipAssemblyComplex tileEntity) {
                return tileEntity.modules.size();
            }
        },
        VacuumConveyorHatch(GT_MetaTileEntity_NanochipAssemblyComplex::addConveyorToMachineList,
            GT_MetaTileEntity_NanochipAssemblyComplex.class) {

            @Override
            public long count(GT_MetaTileEntity_NanochipAssemblyComplex tileEntity) {
                return tileEntity.vacuumConveyors.size();
            }
        },
        // Hatches are allowed in the module base slots, but the assembly complex ignores these for its base operation,
        // so we need a custom adder to not add them to our hatch lists
        IgnoredHatch(GT_MetaTileEntity_NanochipAssemblyComplex::ignoreAndAcceptHatch,
            GT_MetaTileEntity_NanochipAssemblyComplex.class) {

            @Override
            public long count(GT_MetaTileEntity_NanochipAssemblyComplex tileEntity) {
                return 0;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<GT_MetaTileEntity_NanochipAssemblyComplex> adder;

        @SafeVarargs
        AssemblyHatchElement(IGT_HatchAdder<GT_MetaTileEntity_NanochipAssemblyComplex> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super GT_MetaTileEntity_NanochipAssemblyComplex> adder() {
            return adder;
        }
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // Does not get have maintenance issues
        return true;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
