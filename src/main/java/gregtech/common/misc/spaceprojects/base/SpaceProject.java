package gregtech.common.misc.spaceprojects.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.enums.UpgradeStatus;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

public class SpaceProject implements ISpaceProject {

    // #region Variables

    protected String name;
    protected String unlocalizedName;
    protected long voltage;
    protected int buildTime;
    protected int projectTier;
    protected int currentStage;
    protected int totalStage;
    protected Map<String, ISP_Upgrade> upgradesAvailable;
    protected Map<String, ISP_Upgrade> upgradesInstalled;
    protected ISP_Requirements requirements;
    protected ISP_Upgrade currentUpgrade;
    protected ItemStack[] itemsCost;
    protected FluidStack[] fluidsCost;
    protected ISpaceBody location;

    // #endregion

    // #region Getters

    @Override
    public String getProjectName() {
        return name;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(unlocalizedName);
    }

    @Override
    public long getProjectVoltage() {
        return voltage;
    }

    @Override
    public int getProjectBuildTime() {
        return buildTime;
    }

    @Override
    public float getProjectCurrentProgress() {
        if (currentUpgrade != null) {
            return currentUpgrade.getCurrentProgress();
        }

        return currentStage / totalStage * 100.0f;
    }

    @Override
    public int getProjectTier() {
        return projectTier;
    }

    @Override
    public int getCurrentStage() {
        return currentStage;
    }

    @Override
    public int getTotalStages() {
        return totalStage;
    }

    @Override
    public List<ISP_Upgrade> getUpgradesAvailable() {
        List<ISP_Upgrade> upgrades = new ArrayList<>();
        for (ISP_Upgrade upgrade : upgradesAvailable.values()) {
            if (upgrade.getStatus() == UpgradeStatus.Unlocked) {
                upgrades.add(upgrade);
            }
        }

        return upgrades;
    }

    @Override
    public Map<String, ISP_Upgrade> getUpgradesBuilt() {
        return upgradesInstalled;
    }

    @Override
    public ItemStack[] getItemsCostPerStage() {
        if (currentUpgrade != null) {
            return currentUpgrade.getItemsCostPerStage();
        }

        return itemsCost;
    }

    @Override
    public ItemStack getItemCostPerStage(int index) {
        if (itemsCost == null || index < 0 || index >= itemsCost.length) {
            return null;
        }

        if (currentUpgrade != null) {
            return currentUpgrade.getItemCostPerStage(index);
        }

        return itemsCost[index];
    }

    @Override
    public ItemStack[] getCurrentItemsProgress() {
        if (currentUpgrade != null) {
            return currentUpgrade.getCurrentItemsProgress();
        }

        ItemStack[] currentItemsProgress = new ItemStack[itemsCost.length];
        int index = 0;
        for (ItemStack item : itemsCost) {
            ItemStack copy = item.copy();
            copy.stackSize *= getCurrentStage();
            currentItemsProgress[index++] = copy;
        }

        return currentItemsProgress;
    }

    @Override
    public ItemStack getCurrentItemProgress(int index) {
        if (currentUpgrade != null) {
            return currentUpgrade.getCurrentItemProgress(index);
        }

        if (itemsCost == null || index < 0 || index >= itemsCost.length) {
            return null;
        }

        ItemStack item = itemsCost[index].copy();
        item.stackSize *= getCurrentStage();
        return item;
    }

    @Override
    public ItemStack[] getTotalItemsCost() {
        if (currentUpgrade != null) {
            return currentUpgrade.getTotalItemsCost();
        }

        ItemStack[] totalItemsCost = new ItemStack[itemsCost.length];
        int index = 0;
        for (ItemStack item : itemsCost) {
            ItemStack copy = item.copy();
            copy.stackSize *= getTotalStages();
            totalItemsCost[index++] = copy;
        }

        return totalItemsCost;
    }

    @Override
    public ItemStack getTotalItemCost(int index) {
        if (currentUpgrade != null) {
            return currentUpgrade.getTotalItemCost(index);
        }

        if (itemsCost == null || index < 0 || index >= itemsCost.length) {
            return null;
        }

        ItemStack item = itemsCost[index].copy();
        item.stackSize *= getTotalStages();
        return item;
    }

    @Override
    public FluidStack[] getFluidsCostPerStage() {
        if (currentUpgrade != null) {
            return currentUpgrade.getFluidsCostPerStage();
        }

        return fluidsCost;
    }

    @Override
    public FluidStack getFluidCostPerStage(int index) {
        if (currentUpgrade != null) {
            return currentUpgrade.getFluidCostPerStage(index);
        }

        if (fluidsCost == null || index < 0 || index >= fluidsCost.length) {
            return null;
        }

        return fluidsCost[index];
    }

    @Override
    public FluidStack[] getCurrentFluidsProgress() {
        if (currentUpgrade != null) {
            return currentUpgrade.getCurrentFluidsProgress();
        }

        if (fluidsCost == null) {
            return null;
        }

        FluidStack[] currentFluidsProgress = new FluidStack[fluidsCost.length];
        int index = 0;
        for (FluidStack tFluid : fluidsCost) {
            FluidStack copy = tFluid.copy();
            copy.amount *= getCurrentStage();
            currentFluidsProgress[index++] = copy;
        }

        return currentFluidsProgress;
    }

    @Override
    public FluidStack getCurrentFluidProgress(int index) {
        if (currentUpgrade != null) {
            return currentUpgrade.getCurrentFluidProgress(index);
        }

        if (fluidsCost == null || index < 0 || index >= fluidsCost.length) {
            return null;
        }

        FluidStack fluid = fluidsCost[index].copy();
        fluid.amount *= getCurrentStage();
        return fluid;
    }

    @Override
    public FluidStack[] getTotalFluidsCost() {
        if (currentUpgrade != null) {
            return currentUpgrade.getTotalFluidsCost();
        }

        if (fluidsCost == null) {
            return null;
        }

        FluidStack[] totalFluidsCost = new FluidStack[fluidsCost.length];
        int index = 0;
        for (FluidStack fluid : fluidsCost) {
            FluidStack copy = fluid.copy();
            copy.amount *= getTotalStages();
            totalFluidsCost[index++] = copy;
        }

        return totalFluidsCost;
    }

    @Override
    public FluidStack getTotalFluidCost(int index) {
        if (currentUpgrade != null) {
            return currentUpgrade.getTotalFluidCost(index);
        }

        if (fluidsCost == null || index < 0 || index >= fluidsCost.length) {
            return null;
        }

        FluidStack fluid = fluidsCost[index].copy();
        fluid.amount *= getTotalStages();
        return fluid;
    }

    @Override
    public ISP_Upgrade getUpgradeBeingBuilt() {
        return currentUpgrade;
    }

    // #endregion

    // #region Setter/Builder

    public SpaceProject setProjectName(String spaceProjectName) {
        name = spaceProjectName;
        return this;
    }

    public SpaceProject setProjectUnlocalizedName(String spaceProjectUnlocalizedName) {
        unlocalizedName = spaceProjectUnlocalizedName;
        return this;
    }

    public SpaceProject setProjectVoltage(long spaceProjectVoltage) {
        voltage = spaceProjectVoltage;
        return this;
    }

    public SpaceProject setProjectBuildTime(int spaceProjectBuildTime) {
        buildTime = spaceProjectBuildTime;
        return this;
    }

    public SpaceProject setTotalStages(int spaceProjectTotalStages) {
        totalStage = spaceProjectTotalStages;
        return this;
    }

    public SpaceProject setItemCosts(ItemStack... spaceProjectItemsCost) {
        itemsCost = spaceProjectItemsCost;
        return this;
    }

    public SpaceProject setFluidCosts(FluidStack... spaceProjectFluidsCost) {
        fluidsCost = spaceProjectFluidsCost;
        return this;
    }

    public SpaceProject setUpgrades(ISP_Upgrade... spaceProjectUpgrades) {
        for (ISP_Upgrade upgrade : spaceProjectUpgrades) {
            upgradesAvailable.put(upgrade.getUpgradeName(), upgrade);
        }
        return this;
    }

    @Override
    public void setCurrentUpgradeBeingBuilt(ISP_Upgrade newCurrentUpgrade) {
        if (totalStage == currentStage) {
            currentUpgrade = newCurrentUpgrade;
        }
    }

    @Override
    public void setProjectStage(int aStage) {
        currentStage = aStage;
    }

    @Override
    public void setProjectLocation(ISpaceBody newLocation) {
        location = newLocation;
    }

    // #endregion

    // #region Other

    @Override
    public SpaceProject copy() {
        SpaceProject aCopy = new SpaceProject().setProjectName(name).setProjectUnlocalizedName(unlocalizedName)
                .setProjectVoltage(voltage).setProjectBuildTime(buildTime).setItemCosts(itemsCost)
                .setFluidCosts(fluidsCost).setTotalStages(totalStage);
        if (upgradesAvailable != null) {
            ISP_Upgrade[] tUpgrades = new ISP_Upgrade[upgradesAvailable.size()];
            int tIndex = 0;
            for (ISP_Upgrade tUpgrade : upgradesAvailable.values()) {
                tUpgrades[tIndex++] = tUpgrade.copy();
            }
            aCopy.setUpgrades(tUpgrades);
        }
        return aCopy;
    }

    @Override
    public void goToNextStage() {
        if (getCurrentStage() == getTotalStages()) {
            if (currentUpgrade != null) {
                upgradesInstalled.put(currentUpgrade.getUpgradeName(), currentUpgrade);
                currentUpgrade = null;
            }
            return;
        }
        if (currentUpgrade != null) {
            currentUpgrade.goToNextStage();
            return;
        }
        currentStage++;
    }

    @Override
    public boolean meetsRequirements(UUID aTeam, ISpaceBody aLocation) {
        if (requirements == null) {
            return true;
        }

        if (requirements.getBodyType() != null) {
            if (!requirements.getBodyType().equals(location.getType())) {
                return false;
            }
        }

        if (requirements.getStarType() != null) {
            if (!requirements.getStarType().equals(location.getStarType())) {
                return false;
            }
        }

        if (requirements.getProjects() != null) {
            for (SpaceProject tProject : requirements.getProjects()) {
                if (!SpaceProjectManager.teamHasProject(aTeam, tProject)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpaceProject)) {
            return false;
        }
        return getProjectName().equals(((SpaceProject) obj).getProjectName());
    }

    @Override
    public boolean isFinished() {
        return currentStage == totalStage;
    }

    // #endregion
}
