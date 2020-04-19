package com.kevinlu.watstats.data;

import org.jetbrains.annotations.Nullable;

public enum Balance {
    RESIDENCE_PLAN("1", "Residence Plan"),
    SUPER_SAVER_MP("2", "Super Saver MP"),
    SAVER_MP("3", "Saver MP"),
    CASUAL_MP("4", "Casual MP"),
    FLEXIBLE_1("5", "Flex Dollars"),
    FLEXIBLE_2("6", "Flex Dollars"),
    TRANSFER_MP("7", "Transfer MP"),
    DONS_MEAL_ALLOW("8", "Don's Meal Allowance"),
    DONS_FLEX("9", "Don's Flex Dollars"),
    UNALLOCATED("A", "Unallocated"),
    DEPT_CHARGE("B", "Department charge"),
    OVERDRAFT("C", "Overdraft");

    private final String id;
    private final String name;

    Balance(String id, String name) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }
}