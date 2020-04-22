package com.kevinlu.watstats.data;

import com.kevinlu.watstats.R;

import org.jetbrains.annotations.Nullable;

public enum Balance {
    RESIDENCE_PLAN("1", "Residence Plan", "Residence\nPlan", R.drawable.ic_mealplan),
    SUPER_SAVER_MP("2", "Super Saver MP", "Super\nSaver MP", R.drawable.ic_mealplan),
    SAVER_MP("3", "Saver MP", "Saver\nMP", R.drawable.ic_mealplan),
    CASUAL_MP("4", "Casual MP","Casual\nMP",  R.drawable.ic_mealplan),
    FLEXIBLE_1("5", "Flex Dollars","Flex\nDollars",  R.drawable.ic_flexdollar),
    FLEXIBLE_2("6", "Flex Dollars","Flex\nDollars",  R.drawable.ic_flexdollar),
    TRANSFER_MP("7", "Transfer MP","Transfer\nMeal Plan",  R.drawable.ic_transfer),
    DONS_MEAL_ALLOW("8", "Don's Meal Allowance","Don's Meal\nAllowance",  R.drawable.ic_mealplan),
    DONS_FLEX("9", "Don's Flex Dollars","Don's Flex\nDollars",  R.drawable.ic_flexdollar),
    UNALLOCATED("A", "Unallocated","Unallocated",  R.drawable.ic_flexdollar),
    DEPT_CHARGE("B", "Department Charge","Department\nCharge",  R.drawable.ic_flexdollar),
    OVERDRAFT("C", "Overdraft","Overdraft",  R.drawable.ic_flexdollar);

    private final String id;
    private final String name;
    private final String newlineName;
    private final int icon;

    Balance(String id, String name, String newlineName, int icon) {
        this.id = id;
        this.name = name;
        this.newlineName = newlineName;
        this.icon = icon;
    }

    @Nullable
    public static Balance matchBalance(String id) {
        for (Balance balance : Balance.values()) {
            if (id.equals(balance.id)) {
                return balance;
            }
        }
        return null;
    }

    public String getNewlineName() {
        return newlineName;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}