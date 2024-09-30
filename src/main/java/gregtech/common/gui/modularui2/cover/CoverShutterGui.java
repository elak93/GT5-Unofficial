package gregtech.common.gui.modularui2.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverShutter;
import gregtech.common.gui.modularui2.LinkedBoolValue;

public class CoverShutterGui extends CoverGui<CoverShutter> {

    @Override
    protected String getGuiId() {
        return "cover.shutter";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<Mode> modeSyncValue = new EnumSyncValue<>(
            Mode.class,
            () -> getMode(guiData),
            value -> setMode(value, guiData));
        syncManager.syncValue("mode", modeSyncValue);

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.OPEN_IF_ENABLED))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .size(16),
                    IKey.str(GTUtility.trans("082", "Open if work enabled"))
                        .asWidget())
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.OPEN_IF_DISABLED))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .size(16),
                    IKey.str(GTUtility.trans("083", "Open if work disabled"))
                        .asWidget())
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.ONLY_OUTPUT))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .size(16),
                    IKey.str(GTUtility.trans("084", "Only Output allowed"))
                        .asWidget())
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.ONLY_INPUT))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .size(16),
                    IKey.str(GTUtility.trans("085", "Only Input allowed"))
                        .asWidget()));
    }

    private enum Mode {

        OPEN_IF_ENABLED,
        OPEN_IF_DISABLED,
        ONLY_OUTPUT,
        ONLY_INPUT;

        private static final Mode[] VALUES = Mode.values();
    }

    private Mode getMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        if (coverVariable >= 0 && coverVariable < Mode.VALUES.length) {
            return Mode.VALUES[coverVariable];
        }
        return Mode.OPEN_IF_ENABLED;
    }

    private void setMode(Mode mode, CoverGuiData guiData) {
        CoverShutter cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        if (mode.ordinal() == coverVariable) return;

        cover.setVariable(mode.ordinal());
        guiData.setCoverData(cover);
    }

}
