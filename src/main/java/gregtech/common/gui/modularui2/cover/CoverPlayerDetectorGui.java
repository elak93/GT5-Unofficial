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
import gregtech.common.covers.CoverPlayerDetector;
import gregtech.common.gui.modularui2.LinkedBoolValue;

public class CoverPlayerDetectorGui extends CoverGui<CoverPlayerDetector> {

    @Override
    protected String getGuiId() {
        return "cover.player_detector";
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
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.ANY_PLAYER))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.str(GTUtility.trans("068.1", "Emit if any Player is close")))
                        .size(16),
                    IKey.str(GTUtility.trans("319", "Any player"))
                        .asWidget())
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.OTHER_PLAYERS))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.str(GTUtility.trans("069.1", "Emit if other Player is close")))
                        .size(16),
                    IKey.str(GTUtility.trans("320", "Other players"))
                        .asWidget())
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.ONLY_OWNER))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.str(GTUtility.trans("070", "Emit if you are close")))
                        .size(16),
                    IKey.str(GTUtility.trans("321", "Only owner"))
                        .asWidget()));
    }

    private enum Mode {

        ANY_PLAYER,
        OTHER_PLAYERS,
        ONLY_OWNER;

        private static final Mode[] VALUES = values();
    }

    private Mode getMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        if (coverVariable >= 0 && coverVariable < Mode.VALUES.length) {
            return Mode.VALUES[coverVariable];
        }
        return Mode.ANY_PLAYER;
    }

    private void setMode(Mode mode, CoverGuiData guiData) {
        CoverPlayerDetector cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        if (mode.ordinal() == coverVariable) return;

        cover.setVariable(mode.ordinal());
        guiData.setCoverData(cover);
    }

}
