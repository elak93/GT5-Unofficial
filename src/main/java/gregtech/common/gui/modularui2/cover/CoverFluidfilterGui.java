package gregtech.common.gui.modularui2.cover;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.FluidSlot;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverFluidfilter;
import gregtech.common.gui.modularui2.EnumRowBuilder;

public class CoverFluidfilterGui extends CoverGui<CoverFluidfilter> {

    @Override
    protected String getGuiId() {
        return "cover.fluid_filter";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<IOMode> ioModeSyncValue = new EnumSyncValue<>(
            IOMode.class,
            () -> getFilterDirection(guiData),
            value -> setFilterDirection(value, guiData));
        syncManager.syncValue("io_mode", ioModeSyncValue);
        EnumSyncValue<FilterType> filterTypeSyncValue = new EnumSyncValue<>(
            FilterType.class,
            () -> getFilterType(guiData),
            value -> setFilterType(value, guiData));
        syncManager.syncValue("filter_type", filterTypeSyncValue);
        EnumSyncValue<BlockMode> blockModeSyncValue = new EnumSyncValue<>(
            BlockMode.class,
            () -> getBlockMode(guiData),
            value -> setBlockMode(value, guiData));
        syncManager.syncValue("block_mode", blockModeSyncValue);

        IFluidTank filterTank = new FluidStackTank(() -> {
            Fluid fluid = FluidRegistry.getFluid(getCoverData(guiData).getFluidId());
            if (fluid != null) {
                return new FluidStack(fluid, 1);
            }
            return null;
        }, fluidStack -> {
            int fluidId = fluidStack != null ? FluidRegistry.getFluidID(fluidStack.getFluid()) : -1;
            CoverFluidfilter cover = getCoverData(guiData);
            cover.setFluidId(fluidId);
            guiData.setCoverData(cover);
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(column);
            }
        }, 1);

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new EnumRowBuilder<>(IOMode.class).value(ioModeSyncValue)
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_IMPORT, GTGuiTextures.OVERLAY_BUTTON_EXPORT)
                        .build(),
                    IKey.str(GTUtility.trans("238", "Filter Direction"))
                        .asWidget())
                .row(
                    new EnumRowBuilder<>(FilterType.class).value(filterTypeSyncValue)
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_WHITELIST, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
                        .build(),
                    IKey.str(GTUtility.trans("239", "Filter Type"))
                        .asWidget())
                .row(
                    new EnumRowBuilder<>(BlockMode.class).value(blockModeSyncValue)
                        .overlay(
                            new DynamicDrawable(
                                () -> ioModeSyncValue.getValue() == IOMode.INPUT
                                    ? GTGuiTextures.OVERLAY_BUTTON_BLOCK_INPUT
                                    : GTGuiTextures.OVERLAY_BUTTON_BLOCK_OUTPUT),
                            new DynamicDrawable(
                                () -> ioModeSyncValue.getValue() == IOMode.INPUT
                                    ? GTGuiTextures.OVERLAY_BUTTON_ALLOW_INPUT
                                    : GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT))
                        .tooltip(
                            IKey.dynamic(
                                () -> ioModeSyncValue.getValue() == IOMode.INPUT ? GTUtility.trans("314", "Allow Input")
                                    : GTUtility.trans("312", "Allow Output")),
                            IKey.dynamic(
                                () -> ioModeSyncValue.getValue() == IOMode.INPUT ? GTUtility.trans("313", "Block Input")
                                    : GTUtility.trans("311", "Block Output")))
                        .build(),
                    IKey.str(GTUtility.trans("240", "Block Flow"))
                        .asWidget()))
            .child(
                Flow.row()
                    .marginLeft(WIDGET_MARGIN)
                    .marginTop(WIDGET_MARGIN)
                    .coverChildren()
                    .childPadding(WIDGET_MARGIN)
                    .child(
                        new FluidSlot().syncHandler(
                            new FluidSlotSyncHandler(filterTank).phantom(true)
                                .controlsAmount(false)))
                    .child(IKey.dynamic(() -> {
                        FluidStack fluidStack = filterTank.getFluid();
                        if (fluidStack != null) {
                            return fluidStack.getLocalizedName();
                        }
                        return GTUtility.trans("315", "Filter Empty");
                    })
                        .asWidget()));
    }

    private enum IOMode implements KeyProvider {

        INPUT(IKey.str(GTUtility.trans("232", "Filter Input"))),
        OUTPUT(IKey.str(GTUtility.trans("233", "Filter Output")));

        private final IKey key;

        IOMode(IKey key) {
            this.key = key;
        }

        @Override
        public IKey getKey() {
            return this.key;
        }
    }

    private IOMode getFilterDirection(CoverGuiData guiData) {
        CoverFluidfilter coverData = getCoverData(guiData);
        return (coverData.getFilterMode() >> 2 & 0x1) == 0 ? IOMode.INPUT : IOMode.OUTPUT;
    }

    private void setFilterDirection(IOMode ioMode, CoverGuiData guiData) {
        CoverFluidfilter coverData = getCoverData(guiData);
        IOMode oldMode = getFilterDirection(guiData);
        if (ioMode == oldMode) return;

        int filterMode = coverData.getFilterMode();
        if (ioMode == IOMode.INPUT) {
            filterMode &= 0x3;
        } else {
            filterMode |= 0x4;
        }
        coverData.setFilterMode(filterMode);
        guiData.setCoverData(coverData);
    }

    private enum FilterType implements KeyProvider {

        WHITELIST(IKey.str(GTUtility.trans("236", "Whitelist Fluid"))),
        BLACKLIST(IKey.str(GTUtility.trans("237", "Blacklist Fluid")));

        private final IKey key;

        FilterType(IKey key) {
            this.key = key;
        }

        @Override
        public IKey getKey() {
            return this.key;
        }
    }

    private FilterType getFilterType(CoverGuiData guiData) {
        CoverFluidfilter coverData = getCoverData(guiData);
        return (coverData.getFilterMode() & 0x1) == 0 ? FilterType.WHITELIST : FilterType.BLACKLIST;
    }

    private void setFilterType(FilterType filterType, CoverGuiData guiData) {
        CoverFluidfilter coverData = getCoverData(guiData);
        FilterType oldFilterType = getFilterType(guiData);
        if (filterType == oldFilterType) return;

        int filterMode = coverData.getFilterMode();
        if (filterType == FilterType.WHITELIST) {
            filterMode &= 0x6;
        } else {
            filterMode |= 0x1;
        }
        coverData.setFilterMode(filterMode);
        guiData.setCoverData(coverData);
    }

    private enum BlockMode {
        BLOCK,
        ALLOW
    }

    private BlockMode getBlockMode(CoverGuiData guiData) {
        CoverFluidfilter coverData = getCoverData(guiData);
        return (coverData.getFilterMode() >> 1 & 0x1) == 0 ? BlockMode.BLOCK : BlockMode.ALLOW;
    }

    private void setBlockMode(BlockMode blockMode, CoverGuiData guiData) {
        CoverFluidfilter coverData = getCoverData(guiData);
        BlockMode oldBlockMode = getBlockMode(guiData);
        if (blockMode == oldBlockMode) return;

        int filterMode = coverData.getFilterMode();
        if (blockMode == BlockMode.BLOCK) {
            filterMode &= 0x5;
        } else {
            filterMode |= 0x2;
        }
        coverData.setFilterMode(filterMode);
        guiData.setCoverData(coverData);
    }
}
