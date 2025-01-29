package gregtech.common.gui.modularui2.cover;

import static gregtech.common.gui.modularui2.cover.CoverGui.WIDGET_MARGIN;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverFacadeBase;

public class CoverFacadeBaseGui extends CoverGui<CoverFacadeBase> {

    @Override
    protected String getGuiId() {
        return "cover.facade";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new ToggleButton().value(
                        new BooleanSyncValue(() -> getRedstonePass(guiData), value -> setRedstonePass(value, guiData)))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("128", "Redstone"))
                        .asWidget())
                .row(
                    new ToggleButton()
                        .value(
                            new BooleanSyncValue(() -> getEnergyPass(guiData), value -> setEnergyPass(value, guiData)))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("129", "Energy"))
                        .asWidget())
                .row(
                    new ToggleButton()
                        .value(new BooleanSyncValue(() -> getFluidPass(guiData), value -> setFluidPass(value, guiData)))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("130", "Fluids"))
                        .asWidget())
                .row(
                    new ToggleButton()
                        .value(new BooleanSyncValue(() -> getItemPass(guiData), value -> setItemPass(value, guiData)))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("131", "Items"))
                        .asWidget()));
    }

    @Override
    protected void addTitleToUI(CoverGuiData guiData, Flow column) {
        ItemStack coverItem = getCoverData(guiData).getStack();
        if (coverItem == null) return;
        column.child(
            Flow.row()
                .coverChildren()
                .marginBottom(4)
                .child(new com.cleanroommc.modularui.drawable.ItemDrawable(coverItem).asWidget())
                .child(
                    new com.cleanroommc.modularui.widgets.TextWidget(coverItem.getDisplayName()).marginLeft(4)
                        .widgetTheme(GTWidgetThemes.TITLE_TEXT)));
    }

    private boolean getRedstonePass(CoverGuiData guiData) {
        CoverFacadeBase coverData = getCoverData(guiData);
        return (coverData.getFlags() & 0x1) > 0;
    }

    private void setRedstonePass(boolean redstonePass, CoverGuiData guiData) {
        CoverFacadeBase coverData = getCoverData(guiData);
        boolean wasEnabled = getRedstonePass(guiData);
        if (redstonePass == wasEnabled) return;

        int flags = coverData.getFlags();
        if (redstonePass) {
            flags |= 0x1;
        } else {
            flags &= ~0x1;
        }
        coverData.setFlags(flags);
        guiData.setCoverData(coverData);
    }

    private boolean getEnergyPass(CoverGuiData guiData) {
        CoverFacadeBase coverData = getCoverData(guiData);
        return (coverData.getFlags() & 0x2) > 0;
    }

    private void setEnergyPass(boolean energyPass, CoverGuiData guiData) {
        CoverFacadeBase coverData = getCoverData(guiData);
        boolean wasEnabled = getEnergyPass(guiData);
        if (energyPass == wasEnabled) return;

        int flags = coverData.getFlags();
        if (energyPass) {
            flags |= 0x2;
        } else {
            flags &= ~0x2;
        }
        coverData.setFlags(flags);
        guiData.setCoverData(coverData);
    }

    private boolean getFluidPass(CoverGuiData guiData) {
        CoverFacadeBase coverData = getCoverData(guiData);
        return (coverData.getFlags() & 0x4) > 0;
    }

    private void setFluidPass(boolean fluidPass, CoverGuiData guiData) {
        CoverFacadeBase coverData = getCoverData(guiData);
        boolean wasEnabled = getFluidPass(guiData);
        if (fluidPass == wasEnabled) return;

        int flags = coverData.getFlags();
        if (fluidPass) {
            flags |= 0x4;
        } else {
            flags &= ~0x4;
        }
        coverData.setFlags(flags);
        guiData.setCoverData(coverData);
    }

    private boolean getItemPass(CoverGuiData guiData) {
        CoverFacadeBase coverData = getCoverData(guiData);
        return (coverData.getFlags() & 0x8) > 0;
    }

    private void setItemPass(boolean itemPass, CoverGuiData guiData) {
        CoverFacadeBase coverData = getCoverData(guiData);
        boolean wasEnabled = getItemPass(guiData);
        if (itemPass == wasEnabled) return;

        int flags = coverData.getFlags();
        if (itemPass) {
            flags |= 0x8;
        } else {
            flags &= ~0x8;
        }
        coverData.setFlags(flags);
        guiData.setCoverData(coverData);
    }

}
