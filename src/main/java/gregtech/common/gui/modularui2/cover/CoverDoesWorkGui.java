package gregtech.common.gui.modularui2.cover;

import static gregtech.common.covers.CoverDoesWork.*;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BinaryEnumSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverDoesWork;
import gregtech.common.gui.modularui2.EnumRowBuilder;

public class CoverDoesWorkGui extends CoverGui<CoverDoesWork> {

    @Override
    protected String getGuiId() {
        return "cover.activity_detector";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<DetectionMode> detectionModeSyncValue = new EnumSyncValue<>(
            DetectionMode.class,
            () -> getDetectionMode(guiData),
            mode -> setDetectionMode(mode, guiData, column));
        syncManager.syncValue("detection_mode", detectionModeSyncValue);
        BinaryEnumSyncValue<RedstoneMode> redstoneModeSyncValue = new BinaryEnumSyncValue<>(
            RedstoneMode.class,
            () -> getRedstoneMode(guiData),
            mode -> setRedstoneMode(mode, guiData, column));

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new EnumRowBuilder<>(DetectionMode.class).value(detectionModeSyncValue)
                        .overlay(
                            GTGuiTextures.OVERLAY_BUTTON_PROGRESS,
                            GTGuiTextures.OVERLAY_BUTTON_CHECKMARK,
                            GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                        .build(),
                    IKey.dynamic(() -> {
                        DetectionMode mode = detectionModeSyncValue.getValue();
                        if (mode == DetectionMode.MACHINE_ENABLED) {
                            return GTUtility.trans("271", "Machine enabled");
                        } else if (mode == DetectionMode.MACHINE_IDLE) {
                            return GTUtility.trans("242", "Machine idle");
                        } else {
                            return GTUtility.trans("241", "Recipe progress");
                        }
                    })
                        .asWidget())
                .row(
                    new ToggleButton().value(redstoneModeSyncValue)
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                        .size(16),
                    IKey.dynamic(
                        () -> redstoneModeSyncValue.getValue() == RedstoneMode.INVERTED
                            ? GTUtility.trans("INVERTED", "Inverted")
                            : GTUtility.trans("NORMAL", "Normal"))
                        .asWidget()));
    }

    private enum DetectionMode {
        RECIPE_PROGRESS,
        MACHINE_IDLE,
        MACHINE_ENABLED
    }

    private DetectionMode getDetectionMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        if (isFlagSet(coverVariable, FLAG_PROGRESS)) {
            return DetectionMode.MACHINE_IDLE;
        } else if (isFlagSet(coverVariable, FLAG_ENABLED)) {
            return DetectionMode.MACHINE_ENABLED;
        } else {
            return DetectionMode.RECIPE_PROGRESS;
        }
    }

    private void setDetectionMode(DetectionMode mode, CoverGuiData guiData, Flow column) {
        CoverDoesWork cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        final int newCoverVariable = switch (mode) {
            case RECIPE_PROGRESS -> (coverVariable & ~FLAG_ENABLED) & ~FLAG_PROGRESS;
            case MACHINE_IDLE -> (coverVariable & ~FLAG_ENABLED) | FLAG_PROGRESS;
            case MACHINE_ENABLED -> (coverVariable & ~FLAG_PROGRESS) | FLAG_ENABLED;
        };
        if (coverVariable != newCoverVariable) {
            cover.setVariable(newCoverVariable);
            guiData.setCoverData(cover);
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(column);
            }
        }
    }

    private enum RedstoneMode {
        NORMAL,
        INVERTED
    }

    private RedstoneMode getRedstoneMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        return isFlagSet(coverVariable, FLAG_INVERTED) ? RedstoneMode.INVERTED : RedstoneMode.NORMAL;
    }

    private void setRedstoneMode(RedstoneMode mode, CoverGuiData guiData, Flow column) {
        CoverDoesWork cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        int newCoverVariable = switch (mode) {
            case NORMAL -> coverVariable & ~FLAG_INVERTED;
            case INVERTED -> coverVariable | FLAG_INVERTED;
        };
        if (coverVariable != newCoverVariable) {
            cover.setVariable(newCoverVariable);
            guiData.setCoverData(cover);
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(column);
            }
        }
    }

}
