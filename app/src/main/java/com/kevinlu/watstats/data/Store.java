package com.kevinlu.watstats.data;

import com.kevinlu.watstats.R;

import org.jetbrains.annotations.Nullable;

public enum Store {
    //TODO: make colors for each UW building (this will take a while)
    MUDIES("MUDIES", "Mudie's", "V1", R.color.v1),
    CHATIME("DC-CHAT", "Chatime", "DC", R.color.dc),
    MKV_LAUNDRY("MKV LAUNDRY", "MKV Laundry", "MKV", R.color.mkv),
    REV_LAUNDRY("REV LAUNDRY", "REV Laundry", "REV", R.color.rev),
    QUESADA("QUESADA", "Quesada", "SLC", R.color.slc),
    W_STORE("W STORE", "W Store", "SLC", R.color.slc),
    TIM_HORTONS_SLC("SLC TH", "Tim Horton's - SLC", "SLC", R.color.slc),
    TIM_HORTONS_DC("DC TH", "Tim Horton's - DC", "DC", R.color.dc),
    TIM_HORTONS_EC5("EC5 TH", "Tim Horton's - EC5", "EC5", R.color.ec5),
    TIM_HORTONS_ML("ML TH", "Tim Horton's - ML", "ML", R.color.ml),
    TIM_HORTONS_SCH("SCH TH", "Tim Horton's - SCH", "SCH", R.color.sch),
    PRINTING("PRINTING", "W Print Printing", "UW", R.color.uw),
    ML_DINER("ML DINER", "ML's Diner", "ML", R.color.ml),
    LIQUID_ASSETS("HH LA", "Liquid Assets", "HH", R.color.hh),
    DC_BYTES("DC BYTES", "DC BYTES", "DC", R.color.dc),
    SLC_MOBILE("PREORDER PICKUP SLC", "SLC Mobile Order", "SLC", R.color.slc),
    THE_MARKET("UWP MARKET", "The Market at CMH", "CMH", R.color.cmh),
    SUBWAY("SUBWAY", "Subway", "SLC", R.color.slc),
    TOP_UP("WAT-TASK", "Top up", "UW", R.color.uw),
    AHS_VENDING_MACHINE("AHS_SNACK", "AHS Vending Machine", "AHS", R.color.ahs),
    AL_VENDING_MACHINE("AL_SNACK", "AL Vending Machine", "AL", R.color.al),
    BMH_VENDING_MACHINE("BMH_SNACK", "BMH Vending Machine", "BMH", R.color.bmh),
    QNC_VENDING_MACHINE("QNC_SNACK", "QNC Vending Machine", "QNC", R.color.qnc),
    MKV_VENDING_MACHINE("MKV_SNACK", "MKV Vending Machine", "MKV", R.color.mkv),
    V1_VENDING_MACHINE("V1_SNACK", "V1 Vending Machine", "V1", R.color.v1),
    PITA_PIT("PITA-SHWARMA", "Pita Pit", "SLC", R.color.slc),
    V1_LAUNDRY("V1C", "V1 Laundry", "V1", R.color.v1),
    BRUBAKERS("SLC BRUBAKERS", "Brubakers", "SLC", R.color.slc),
    TOP_UP_2("WATCARD OFFICE", "Top up", "UW", R.color.uw),
    SOUTHSIDE("SCH SOUTHSIDE", "South Side Mktplce", "SCH", R.color.sch);

    private final String match;
    private final String name;
    private final String location;
    private final int color;

    Store(String match, String name, String location, int color) {
        this.match = match;
        this.name = name;
        this.location = location;
        this.color = color;
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

    public String getLocation() {
        return location;
    }

    public int getColor() {
        return color;
    }
}
