package gregtech.common.gui.modularui2.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverControlsWork;
import gregtech.common.gui.modularui2.LinkedBoolValue;

public class CoverControlsWorkGui extends CoverGui<CoverControlsWork> {

    @Override
    protected String getGuiId() {
        return "cover.machine_controller";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<ConditionMode> conditionModeSyncValue = new EnumSyncValue<>(
            ConditionMode.class,
            () -> getConditionMode(guiData),
            mode -> setConditionMode(mode, guiData));
        syncManager.syncValue("condition_mode", conditionModeSyncValue);
        BooleanSyncValue safeModeSyncValue = new BooleanSyncValue(
            () -> getSafeMode(guiData),
            safeMode -> setSafeMode(safeMode, guiData));

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new ToggleButton()
                        .value(LinkedBoolValue.of(conditionModeSyncValue, ConditionMode.ENABLE_WITH_REDSTONE))
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                        .size(16),
                    IKey.str(GTUtility.trans("243", "Enable with Redstone"))
                        .asWidget())
                .row(
                    new ToggleButton()
                        .value(LinkedBoolValue.of(conditionModeSyncValue, ConditionMode.DISABLE_WITH_REDSTONE))
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                        .size(16),
                    IKey.str(GTUtility.trans("244", "Disable with Redstone"))
                        .asWidget())
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(conditionModeSyncValue, ConditionMode.DISABLE))
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("245", "Disable machine"))
                        .asWidget())
                .row(
                    new ToggleButton().value(safeModeSyncValue)
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("507", "Safe Mode"))
                        .asWidget()));
    }

    private enum ConditionMode {
        ENABLE_WITH_REDSTONE,
        DISABLE_WITH_REDSTONE,
        DISABLE
    }

    private ConditionMode getConditionMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        if (coverVariable % 3 == 0) {
            return ConditionMode.ENABLE_WITH_REDSTONE;
        } else if (coverVariable % 3 == 1) {
            return ConditionMode.DISABLE_WITH_REDSTONE;
        }
        return ConditionMode.DISABLE;
    }

    private void setConditionMode(ConditionMode mode, CoverGuiData guiData) {
        CoverControlsWork cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        boolean safeMode = coverVariable > 2;
        int newCoverVariable = switch (mode) {
            case ENABLE_WITH_REDSTONE -> safeMode ? 3 : 0;
            case DISABLE_WITH_REDSTONE -> safeMode ? 4 : 1;
            case DISABLE -> safeMode ? 5 : 2;
        };
        if (coverVariable != newCoverVariable) {
            cover.setVariable(newCoverVariable);
            guiData.setCoverData(cover);
        }
    }

    private boolean getSafeMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        return coverVariable > 2;
    }

    private void setSafeMode(boolean safeMode, CoverGuiData guiData) {
        CoverControlsWork cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        int newCoverVariable;
        if (safeMode && coverVariable < 3) {
            newCoverVariable = coverVariable + 3;
        } else if (!safeMode && coverVariable > 2) {
            newCoverVariable = coverVariable - 3;
        } else {
            newCoverVariable = coverVariable;
        }
        if (coverVariable != newCoverVariable) {
            cover.setVariable(newCoverVariable);
            guiData.setCoverData(cover);
        }
    }
}
