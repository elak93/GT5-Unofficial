package gregtech.common.gui.modularui2.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverConveyor;
import gregtech.common.gui.modularui2.EnumRowBuilder;

public class CoverConveyorGui extends CoverGui<CoverConveyor> {

    @Override
    protected String getGuiId() {
        return "cover.conveyor";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<IOMode> ioModeSyncValue = new EnumSyncValue<>(
            IOMode.class,
            () -> getIOMode(guiData),
            mode -> setIOMode(mode, guiData, column));
        syncManager.syncValue("io_mode", ioModeSyncValue);
        IWidget exportImportButtons = new EnumRowBuilder<>(IOMode.class).value(ioModeSyncValue)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_EXPORT, GTGuiTextures.OVERLAY_BUTTON_IMPORT)
            .build();
        IWidget exportImportLabel = IKey.str(GTUtility.trans("229", "Export/Import"))
            .asWidget();

        EnumSyncValue<ConditionMode> conditionModeSyncValue = new EnumSyncValue<>(
            ConditionMode.class,
            () -> getConditionMode(guiData),
            mode -> setConditionMode(mode, guiData));
        syncManager.syncValue("condition_mode", conditionModeSyncValue);
        IWidget conditionButtons = new EnumRowBuilder<>(ConditionMode.class).value(conditionModeSyncValue)
            .overlay(
                GTGuiTextures.OVERLAY_BUTTON_CHECKMARK,
                GTGuiTextures.OVERLAY_BUTTON_USE_PROCESSING_STATE,
                GTGuiTextures.OVERLAY_BUTTON_USE_INVERTED_PROCESSING_STATE)
            .build();
        IWidget conditionLabel = IKey.str(GTUtility.trans("230", "Conditional"))
            .asWidget();

        EnumSyncValue<BlockMode> blockModeSyncValue = new EnumSyncValue<>(
            BlockMode.class,
            () -> getBlockMode(guiData),
            mode -> setBlockMode(mode, guiData));
        syncManager.syncValue("block_mode", blockModeSyncValue);
        IWidget blockingButtons = new EnumRowBuilder<>(BlockMode.class).value(blockModeSyncValue)
            .overlay(
                new DynamicDrawable(
                    () -> ioModeSyncValue.getValue() == IOMode.IMPORT ? GTGuiTextures.OVERLAY_BUTTON_ALLOW_INPUT
                        : GTGuiTextures.OVERLAY_BUTTON_ALLOW_OUTPUT),
                new DynamicDrawable(
                    () -> ioModeSyncValue.getValue() == IOMode.IMPORT ? GTGuiTextures.OVERLAY_BUTTON_BLOCK_INPUT
                        : GTGuiTextures.OVERLAY_BUTTON_BLOCK_OUTPUT))
            .tooltip(
                IKey.dynamic(
                    () -> ioModeSyncValue.getValue() == IOMode.IMPORT ? GTUtility.trans("314", "Allow Input")
                        : GTUtility.trans("312", "Allow Output")),
                IKey.dynamic(
                    () -> ioModeSyncValue.getValue() == IOMode.IMPORT ? GTUtility.trans("313", "Block Input")
                        : GTUtility.trans("311", "Block Output")))
            .build();
        IWidget blockingLabel = IKey
            .dynamic(
                () -> ioModeSyncValue.getValue() == IOMode.IMPORT ? GTUtility.trans("344", "Input Blocking")
                    : GTUtility.trans("344.1", "Output Blocking"))
            .asWidget();

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(exportImportButtons, exportImportLabel)
                .row(conditionButtons, conditionLabel)
                .row(blockingButtons, blockingLabel));
    }

    private enum IOMode implements KeyProvider {

        EXPORT(IKey.str(GTUtility.trans("006", "Export"))),
        IMPORT(IKey.str(GTUtility.trans("007", "Import")));

        private final IKey key;

        IOMode(IKey key) {
            this.key = key;
        }

        @Override
        public IKey getKey() {
            return this.key;
        }
    }

    private IOMode getIOMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        return ((0x1 & coverVariable) == 1) ? IOMode.IMPORT : IOMode.EXPORT;
    }

    private void setIOMode(IOMode mode, CoverGuiData guiData, Flow column) {
        CoverConveyor cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        int newCoverVariable = switch (mode) {
            case EXPORT -> coverVariable & ~0x1;
            case IMPORT -> coverVariable | 0x1;
        };
        if (coverVariable != newCoverVariable) {
            cover.setVariable(newCoverVariable);
            guiData.setCoverData(cover);
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(column);
            }
        }
    }

    private enum ConditionMode implements KeyProvider {

        ALWAYS(IKey.str(GTUtility.trans("224", "Always On"))),
        CONDITIONAL(IKey.str(GTUtility.trans("343", "Use Machine Processing State"))),
        INVERTED(IKey.str(GTUtility.trans("343.1", "Use Inverted Machine Processing State")));

        private final IKey key;

        ConditionMode(IKey key) {
            this.key = key;
        }

        @Override
        public IKey getKey() {
            return this.key;
        }
    }

    private ConditionMode getConditionMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        if ((coverVariable % 6) < 2) {
            return ConditionMode.ALWAYS;
        }
        if ((coverVariable % 6) == 2 || (coverVariable % 6) == 3) {
            return ConditionMode.CONDITIONAL;
        }
        return ConditionMode.INVERTED;
    }

    private void setConditionMode(ConditionMode mode, CoverGuiData guiData) {
        CoverConveyor cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        int newCoverVariable = switch (mode) {
            case ALWAYS -> {
                if (coverVariable > 5) {
                    yield 0x6 | (coverVariable & ~0xE);
                }
                yield (coverVariable & ~0xE);
            }
            case CONDITIONAL -> {
                if (coverVariable > 5) {
                    yield 0x8 | (coverVariable & ~0xE);
                }
                yield 0x2 | (coverVariable & ~0xE);
            }
            case INVERTED -> {
                if (coverVariable > 5) {
                    yield 0xA | (coverVariable & ~0xE);
                }
                yield (0x4 | (coverVariable & ~0xE));
            }
        };
        if (coverVariable != newCoverVariable) {
            cover.setVariable(newCoverVariable);
            guiData.setCoverData(cover);
        }
    }

    private enum BlockMode {
        ALLOW,
        BLOCK
    }

    private BlockMode getBlockMode(CoverGuiData guiData) {
        int coverVariable = getCoverData(guiData).getVariable();
        return coverVariable < 6 ? BlockMode.BLOCK : BlockMode.ALLOW;
    }

    private void setBlockMode(BlockMode mode, CoverGuiData guiData) {
        CoverConveyor cover = getCoverData(guiData);
        int coverVariable = cover.getVariable();
        int newCoverVariable = switch (mode) {
            case ALLOW -> {
                if (coverVariable <= 5) {
                    yield coverVariable + 6;
                }
                yield coverVariable;
            }
            case BLOCK -> {
                if (coverVariable > 5) {
                    yield coverVariable - 6;
                }
                yield coverVariable;
            }
        };
        if (coverVariable != newCoverVariable) {
            cover.setVariable(newCoverVariable);
            guiData.setCoverData(cover);
        }
    }
}
