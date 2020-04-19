package com.kevinlu.watstats.data;

public enum Store {
    MUDIES("MUDIES", "Mudie's"),
    CHATIME("DC-CHAT", "Chatime"),
    MKV_LAUNDRY("MKV LAUNDRY", "MKV Laundry"),
    QUESADA("QUESADA", "Quesada"),
    W_STORE("W STORE", "W Store"),
    TIM_HORTONS("TH", "Tim Horton's"),
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
    V1_LAUNDRY("V1C", "V1 Laundry");

    private final String match;
    private final String name;

    Store(String match, String name) {
        this.match = match;
        this.name = name;
    }

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
