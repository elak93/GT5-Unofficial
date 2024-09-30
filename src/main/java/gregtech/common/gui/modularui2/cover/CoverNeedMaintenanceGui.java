package gregtech.common.gui.modularui2.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverNeedMaintainance;
import gregtech.common.gui.modularui2.LinkedBoolValue;

public class CoverNeedMaintenanceGui extends CoverGui<CoverNeedMaintainance> {

    @Override
    protected String getGuiId() {
        return "cover.maintenance";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<Threshold> thresholdSyncValue = new EnumSyncValue<>(
            Threshold.class,
            () -> getThreshold(guiData),
            value -> setThreshold(value, guiData));
        syncManager.syncValue("threshold", thresholdSyncValue);
        EnumSyncValue<RedstoneMode> redstoneModeSyncValue = new EnumSyncValue<>(
            RedstoneMode.class,
            () -> getRedstoneMode(guiData),
            value -> setRedstoneMode(value, guiData, column));

        column.child(
            Flow.row()
                .marginLeft(WIDGET_MARGIN)
                .coverChildrenHeight()
                .width(getGUIWidth() - WIDGET_MARGIN * 4)
                .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN)
                .child(
                    new Grid().coverChildren()
                        .minElementMargin(1, 1)
                        .alignment(Alignment.CenterLeft)
                        .row(
                            new ToggleButton().value(LinkedBoolValue.of(thresholdSyncValue, Threshold.ISSUE_1))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("056", "Emit if 1 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("247", "1 Issue"))
                                .asWidget())
                        .row(
                            new ToggleButton().value(LinkedBoolValue.of(thresholdSyncValue, Threshold.ISSUES_2))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("058", "Emit if 2 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("248", "2 Issues"))
                                .asWidget())
                        .row(
                            new ToggleButton().value(LinkedBoolValue.of(thresholdSyncValue, Threshold.ISSUES_3))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("060", "Emit if 3 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("249", "3 Issues"))
                                .asWidget())
                        .row(
                            new ToggleButton().value(LinkedBoolValue.of(thresholdSyncValue, Threshold.ISSUES_4))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("062", "Emit if 4 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("250", "4 Issues"))
                                .asWidget()))
                .child(
                    new Grid().coverChildren()
                        .minElementMargin(1, 1)
                        .alignment(Alignment.CenterLeft)
                        .row(
                            new ToggleButton().value(LinkedBoolValue.of(thresholdSyncValue, Threshold.ISSUES_5))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("064", "Emit if 5 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("251", "5 Issues"))
                                .asWidget())
                        .row(
                            new ToggleButton().value(LinkedBoolValue.of(thresholdSyncValue, Threshold.ROTOR_20))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(
                                    IKey.str(
                                        GTUtility.trans("066", "Emit if rotor needs maintenance low accuracy mod")))
                                .size(16),
                            IKey.str(GTUtility.trans("252", "Rotor < 20%"))
                                .asWidget())
                        .row(
                            new ToggleButton().value(LinkedBoolValue.of(thresholdSyncValue, Threshold.ROTOR_0))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(
                                    IKey.str(
                                        GTUtility.trans("068", "Emit if rotor needs maintenance high accuracy mod")))
                                .size(16),
                            IKey.str(GTUtility.trans("253", "Rotor ≈ 0%"))
                                .asWidget())
                        .row(
                            new CycleButtonWidget().value(redstoneModeSyncValue)
                                .stateOverlay(RedstoneMode.NORMAL, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                .stateOverlay(RedstoneMode.INVERTED, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                                .size(16),
                            IKey.dynamic(
                                () -> redstoneModeSyncValue.getValue() == RedstoneMode.NORMAL
                                    ? GTUtility.trans("NORMAL", "Normal")
                                    : GTUtility.trans("INVERTED", "Inverted"))
                                .asWidget())));
    }

    private enum Threshold {

        ISSUE_1,
        ISSUES_2,
        ISSUES_3,
        ISSUES_4,
        ISSUES_5,
        ROTOR_20,
        ROTOR_0;

        private static final Threshold[] VALUES = values();
    }

    private Threshold getThreshold(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        if (coverVariable >= 0 && coverVariable < Threshold.VALUES.length) {
            return Threshold.VALUES[coverVariable >> 1];
        }
        return Threshold.ISSUE_1;
    }

    private void setThreshold(Threshold threshold, CoverGuiData guiData) {
        CoverNeedMaintainance cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        int newCoverVariable = (coverVariable & 0x1) | (threshold.ordinal() << 1);
        if (coverVariable != newCoverVariable) {
            cover.setVariable(newCoverVariable);
            guiData.setCoverData(cover);
        }
    }

    private enum RedstoneMode {
        NORMAL,
        INVERTED
    }

    private RedstoneMode getRedstoneMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        return (coverVariable & 0x1) > 0 ? RedstoneMode.INVERTED : RedstoneMode.NORMAL;
    }

    private void setRedstoneMode(RedstoneMode redstoneMode, CoverGuiData guiData, Flow column) {
        CoverNeedMaintainance cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        int newCoverVariable = redstoneMode == RedstoneMode.NORMAL ? coverVariable & ~0x1 : coverVariable | 0x1;
        if (coverVariable != newCoverVariable) {
            cover.setVariable(newCoverVariable);
            guiData.setCoverData(cover);
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(column);
            }
        }
    }

}
