package gregtech.api.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

import net.minecraft.item.ItemStack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import gregtech.api.casing.ICasing;
import gregtech.api.enums.HatchElement;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharComparator;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

/**
 * An extended tooltip builder with some compat for the structure wrapper system.
 */
@SuppressWarnings("unused")
public class MultiblockTooltipBuilder2<MTE extends MTEEnhancedMultiBlockBase<?> & IStructureProvider<MTE>>
    extends MultiblockTooltipBuilder {

    public final StructureWrapper<MTE> structure;

    private final Object2ObjectArrayMap<IHatchElement<? super MTE>, String> hatchNameOverrides = new Object2ObjectArrayMap<>();
    private final Object2ObjectArrayMap<IHatchElement<? super MTE>, String> hatchInfoOverrides = new Object2ObjectArrayMap<>();
    private final List<IHatchElement<? super MTE>> hatchOrder = new ArrayList<>();
    private boolean hasMultiampHatches = false, printMultiampSupport = true;

    public MultiblockTooltipBuilder2(StructureWrapper<MTE> structure) {
        this.structure = structure;
    }

    /**
     * Adds the structure size info.
     */
    public MultiblockTooltipBuilder2<MTE> beginStructureBlock(boolean hollow) {
        if (!structure.minSize.equals(structure.maxSize)) {
            super.beginVariableStructureBlock(
                structure.minSize.get0(),
                structure.minSize.get1(),
                structure.minSize.get2(),
                structure.maxSize.get0(),
                structure.maxSize.get1(),
                structure.maxSize.get2(),
                hollow);
        } else {
            super.beginStructureBlock(
                structure.minSize.get0(),
                structure.minSize.get1(),
                structure.minSize.get2(),
                hollow);
        }

        return this;
    }

    /**
     * Adds the structure size info.
     */
    public MultiblockTooltipBuilder2<MTE> beginStructureBlock() {
        if (!structure.minSize.equals(structure.maxSize)) {
            super.beginVariableStructureBlock(
                structure.minSize.get0(),
                structure.minSize.get1(),
                structure.minSize.get2(),
                structure.maxSize.get0(),
                structure.maxSize.get1(),
                structure.maxSize.get2(),
                false);
        } else {
            super.beginStructureBlock(
                structure.minSize.get0(),
                structure.minSize.get1(),
                structure.minSize.get2(),
                false);
        }

        return this;
    }

    /**
     * Adds a casing to the casing list manually.
     */
    public MultiblockTooltipBuilder2<MTE> addCasing(ICasing casing) {
        structure.addCasingInfoAuto(this, casing);
        return this;
    }

    /**
     * Sets the name for a hatch instead of using the default name.
     */
    public MultiblockTooltipBuilder2<MTE> addHatchNameOverride(IHatchElement<? super MTE> hatch, String newName) {
        hatchNameOverrides.put(hatch, newName);
        return this;
    }

    /**
     * Sets the name for a hatch instead of using the default name.
     */
    public MultiblockTooltipBuilder2<MTE> addHatchNameOverride(IHatchElement<? super MTE> hatch, ItemStack stack) {
        hatchNameOverrides.put(hatch, stack.getDisplayName());
        return this;
    }

    /**
     * Sets the location/casing for a hatch instead of using the default location.
     */
    public MultiblockTooltipBuilder2<MTE> addHatchLocationOverride(IHatchElement<? super MTE> hatch,
        String newLocation) {
        hatchInfoOverrides.put(hatch, newLocation);
        return this;
    }

    /**
     * Sets the location/casing for a hatch instead of using the default location.
     */
    public MultiblockTooltipBuilder2<MTE> addHatchLocationOverride(Collection<IHatchElement<? super MTE>> hatches,
        String newLocation) {
        for (var hatch : hatches) {
            hatchInfoOverrides.put(hatch, newLocation);
        }
        return this;
    }

    public MultiblockTooltipBuilder2<MTE> disableMultiAmpHatchLine() {
        printMultiampSupport = false;
        return this;
    }

    /**
     * Forces the hatches to be sorted in a specific order in case your special hatches are acting up
     */
    public MultiblockTooltipBuilder2<MTE> setHatchOrder(List<IHatchElement<? super MTE>> hatches) {
        hatchOrder.clear();
        hatchOrder.addAll(hatches);
        return this;
    }

    /**
     * Add a hatch line manually.
     */
    public MultiblockTooltipBuilder2<MTE> addHatch(ICasing casing, IHatchElement<? super MTE> hatch, int... dots) {
        String override = hatchNameOverrides.get(hatch);

        String info = hatchInfoOverrides.get(hatch);

        // if we were given a hatch info override, use it
        if (info == null) info = GTUtility.translate("GT5U.MBTT.HatchInfo", casing.getLocalizedName());

        // add dots to the info if possible
        if (dots.length > 0) {
            info += GTUtility.translate(
                "GT5U.MBTT.HatchDots",
                String.join(", ", GTUtility.mapToList(new IntArrayList(dots), Object::toString)));
        }

        if (override != null) {
            addOtherStructurePart(override, info, dots);
        } else {
            // try to use an existing addXHatch method if possible
            if (hatch instanceof HatchElement gtHatch) {
                switch (gtHatch) {
                    case Dynamo:
                        addDynamoHatch(info, dots);
                        break;
                    case Energy:
                        addEnergyHatch(info, dots);
                        break;
                    case ExoticEnergy:
                        addOtherStructurePart(GTUtility.translate("GT5U.MBTT.MultiampEnergyHatch"), info, dots);
                        addOtherStructurePart(GTUtility.translate("GT5U.MBTT.LaserTargetHatch"), info, dots);
                        hasMultiampHatches = true;
                        break;
                    case MultiAmpEnergy:
                        addOtherStructurePart(GTUtility.translate("GT5U.MBTT.MultiampEnergyHatch"), info, dots);
                        break;
                    case InputBus:
                        addInputBus(info, dots);
                        break;
                    case InputHatch:
                        addInputHatch(info, dots);
                        break;
                    case Maintenance:
                        addMaintenanceHatch(info, dots);
                        break;
                    case Muffler:
                        addMufflerHatch(info, dots);
                        break;
                    case OutputBus:
                        addOutputBus(info, dots);
                        break;
                    case OutputHatch:
                        addOutputHatch(info, dots);
                        break;
                    default:
                        break;
                }
            } else if (hatch instanceof TTMultiblockBase.HatchElement ttHatch) {

                switch (ttHatch) {
                    case EnergyMulti -> {
                        addOtherStructurePart(GTUtility.translate("GT5U.MBTT.MultiampEnergyHatch"), info, dots);
                        addOtherStructurePart(GTUtility.translate("GT5U.MBTT.LaserTargetHatch"), info, dots);
                        hasMultiampHatches = true;
                    }
                    case DynamoMulti -> {
                        addOtherStructurePart(GTUtility.translate("GT5U.MBTT.MultiampEnergyDynamo"), info, dots);
                        addOtherStructurePart(GTUtility.translate("GT5U.MBTT.LaserSourceHatch"), info, dots);
                        hasMultiampHatches = true;
                    }
                    default -> {
                        addOtherStructurePart(ttHatch.getDisplayName(), info, dots);
                    }
                }
            }
        }

        return this;
    }

    /**
     * Automatically adds casing and hatch lines.
     */
    public MultiblockTooltipBuilder2<MTE> addAllCasingInfo() {
        addAllCasingInfo(null);
        return this;
    }

    /**
     * Automatically adds casing and hatch lines.
     */
    public MultiblockTooltipBuilder2<MTE> addAllCasingInfo(List<ICasing> casingOrder) {
        ObjectArraySet<ICasing> addedCasings = new ObjectArraySet<>();

        // make a list containing the casing chars so that we can sort it properly
        CharList casings = new CharArrayList(structure.casings.keySet());

        if (casingOrder != null && !casingOrder.isEmpty()) {
            // if we were given a casing order, use it
            CharComparator comparator = (char a, char b) -> {
                int i1 = casingOrder.indexOf(structure.casings.get(a).casing);
                int i2 = casingOrder.indexOf(structure.casings.get(b).casing);

                if (i1 == -1 || i2 == -1) {
                    return -Integer.compare(i1, i2);
                } else {
                    return Integer.compare(i1, i2);
                }
            };

            casings.sort(comparator);
        } else {
            // otherwise, sort naturally (by the casing char, which isn't very useful, but it's deterministic)
            casings.sort(null);
        }

        Multimap<Pair<ICasing, IHatchElement<? super MTE>>, Integer> hatches = ArrayListMultimap.create();

        for (char c : casings) {
            CasingInfo<MTE> casingInfo = structure.casings.get(c);

            // add a line for casings as we see them, but don't add them twice if the same ICasing was used multiple
            // times
            if (addedCasings.add(casingInfo.casing)) {
                structure.addCasingInfoAuto(this, casingInfo.casing);
            }

            // keep track of any hatches in this structure element and their dots
            if (casingInfo.hatches != null) {
                for (var hatch : casingInfo.hatches) {
                    hatches.put(Pair.of(casingInfo.casing, hatch), casingInfo.dot);
                }
            }
        }

        List<Pair<ICasing, IHatchElement<? super MTE>>> hatchesSorted = new ArrayList<>(hatches.keys());

        if (!hatchOrder.isEmpty()) {
            // if we were given a hatch order, use it
            hatchesSorted.sort((p1, p2) -> {
                int i1 = hatchOrder.indexOf(p1.right());
                int i2 = hatchOrder.indexOf(p2.right());

                if (i1 == -1 || i2 == -1) {
                    return -Integer.compare(i1, i2);
                } else {
                    return Integer.compare(i1, i2);
                }
            });
        } else {
            // otherwise sort by the hatch type, followed by the hatch display name
            ToIntFunction<Pair<ICasing, IHatchElement<? super MTE>>> categoryComparator = p -> {
                if (p.right() instanceof HatchElement gtHatch) {
                    return gtHatch.ordinal();
                } else if (p.right() instanceof TTMultiblockBase.HatchElement ttHatch) {
                    return ttHatch.ordinal() + 100;
                } else {
                    return 200;
                }
            };

            // this is only relevant for special hatches
            Comparator<Pair<ICasing, IHatchElement<? super MTE>>> nameComparator = Comparator.nullsFirst(
                Comparator.comparing(
                    p -> hatchNameOverrides.getOrDefault(
                        p.right(),
                        p.right()
                            .getDisplayName())));

            hatchesSorted.sort(
                Comparator.comparingInt(categoryComparator)
                    .thenComparing(nameComparator));
        }

        for (var hatch : hatchesSorted) {
            // dedup the dots and sort them
            IntArrayList dots = new IntArrayList(new IntArraySet(hatches.get(hatch)));
            dots.sort(null);

            // finally add the hatch
            addHatch(hatch.left(), hatch.right(), dots.toIntArray());
        }

        // add the tectech multi amp hatch info line if it should be added
        if (printMultiampSupport && hasMultiampHatches) {
            addTecTechHatchInfo();
        }

        return this;
    }
}
