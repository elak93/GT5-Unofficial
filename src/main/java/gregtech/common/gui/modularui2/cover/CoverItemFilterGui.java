package gregtech.common.gui.modularui2.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverItemFilter;
import gregtech.common.gui.modularui2.EnumRowBuilder;

public class CoverItemFilterGui extends CoverGui<CoverItemFilter> {

    @Override
    protected String getGuiId() {
        return "cover.item_filter";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<FilterType> filterTypeSyncValue = new EnumSyncValue<>(
            FilterType.class,
            () -> getFilterType(guiData),
            value -> setFilterType(value, guiData));
        syncManager.syncValue("filter_type", filterTypeSyncValue);

        column.child(
            Flow.column()
                .coverChildren()
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .marginLeft(WIDGET_MARGIN)
                .child(
                    Flow.row()
                        .coverChildren()
                        .childPadding(WIDGET_MARGIN)
                        .child(
                            new EnumRowBuilder<>(FilterType.class).value(filterTypeSyncValue)
                                .overlay(GTGuiTextures.OVERLAY_BUTTON_WHITELIST, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
                                .build())
                        .child(
                            IKey.str(GTUtility.trans("318", "Check Mode"))
                                .asWidget()))
                .child(
                    IKey.str(GTUtility.trans("317", "Filter: "))
                        .asWidget()
                        .marginTop(WIDGET_MARGIN))
                .child(
                    new ItemSlot().slot(new ModularSlot(getCoverData(guiData).getFilter(), 0, true))
                        .marginTop(WIDGET_MARGIN)));
    }

    private enum FilterType implements KeyProvider {

        WHITELIST(IKey.str(GTUtility.trans("125.1", "Whitelist Mode"))),
        BLACKLIST(IKey.str(GTUtility.trans("124.1", "Blacklist Mode")));

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
        return getCoverData(guiData).isWhitelist() ? FilterType.BLACKLIST : FilterType.WHITELIST;
    }

    private void setFilterType(FilterType filterType, CoverGuiData guiData) {
        CoverItemFilter coverData = getCoverData(guiData);
        FilterType oldFilterType = getFilterType(guiData);
        if (filterType == oldFilterType) return;

        coverData.setWhitelist(filterType == FilterType.BLACKLIST);
        guiData.setCoverData(coverData);
    }

}
