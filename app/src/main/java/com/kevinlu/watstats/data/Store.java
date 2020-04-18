package com.kevinlu.watstats.data;

public enum Store {
    MUDIES("MUDIES", "Mudie's"),
    CHATIME("DC-CHAT", "Chatime"),
    V1_LAUNDRY("/V1([C])/g", "Chatime"),;

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
