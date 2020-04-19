package com.kevinlu.watstats.data;

import org.jetbrains.annotations.Nullable;

public enum Store {
    MUDIES("MUDIES", "Mudie's"),
    CHATIME("DC-CHAT", "Chatime"),
    MKV_LAUNDRY("MKV LAUNDRY", "MKV Laundry"),
    QUESADA("QUESADA", "Quesada"),
    W_STORE("W STORE", "W Store"),
    TIM_HORTONS_SLC("SLC TH", "Tim Horton's - SLC"),
    TIM_HORTONS_DC("DC TH", "Tim Horton's - DC"),
    TIM_HORTONS_EC5("EC5 TH", "Tim Horton's - EC5"),
    TIM_HORTONS_ML("ML TH", "Tim Horton's - ML"),
    TIM_HORTONS_SCH("SCH TH", "Tim Horton's - SCH"),
    PRINTING("PRINTING", "W Print Printing"),
    ML_DINER("ML DINER", "ML's Diner"),
    LIQUID_ASSETS("HH LA", "Liquid Assets"),
    DC_BYTES("DC BYTES", "DC BYTES"),
    SLC_MOBILE("PREORDER PICKUP SLC", "SLC Mobile Order"),
    THE_MARKET("UWP MARKET", "The Market at CMH"),
    SUBWAY("SUBWAY", "Subway"),
    TOP_UP("WAT-TASK", "Top up"),
    VENDING_MACHINE("SNACK", "Vending Machine"),
    PITA_PIT("PITA-SHWARMA", "Pita Pit"),
    V1_LAUNDRY("V1C", "V1 Laundry"),
    BRUBAKERS("SLC BRUBAKERS", "Brubakers"),
    TOP_UP_2("WATCARD OFFICE", "Top up"),
    SOUTHSIDE("SCH SOUTHSIDE", "South Side Mktplce");

    private final String match;
    private final String name;

    Store(String match, String name) {
        this.match = match;
        this.name = name;
    }

    @Nullable
    public static Store matchStore(String string) {
        for (Store store : Store.values()) {
            if (string.contains(store.match)) {
                return store;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
