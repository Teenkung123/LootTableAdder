package com.Teenkung123.lootTableAdder.Elements.Enum;

public enum ItemType {
    VANILLA,
    MMOITEMS,
    ITEMSADDER;

    public static ItemType fromString(String type) {
        if (type.equalsIgnoreCase("VANILLA") || type.equalsIgnoreCase("MINECRAFT")) {
            return VANILLA;
        } else if (type.equalsIgnoreCase("MMOITEMS") || type.equalsIgnoreCase("MMOITEM") || type.equalsIgnoreCase("MMO_ITEMS") || type.equalsIgnoreCase("MMO_ITEM")) {
            return MMOITEMS;
        } else if (type.equalsIgnoreCase("ITEMSADDER") || type.equalsIgnoreCase("ITEMS_ADDER")) {
            return ITEMSADDER;
        }
        return null;
    }
}
