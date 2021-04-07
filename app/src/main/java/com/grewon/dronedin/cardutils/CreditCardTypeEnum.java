package com.grewon.dronedin.cardutils;

/**
 * Created by kauserali on 05/05/14.
 */
public enum CreditCardTypeEnum {
    VISA("Visa"),
    MASTER_CARD("MasterCard"),
    AMERICAN_EXPRESS("American Express"),
    DINERS("Diners"),
    RUPAY("Rupay"),
    JCB("JCB"),
    DISCOVER("Discover");

    public final String cartType;

    CreditCardTypeEnum(String cartType) {
        this.cartType = cartType;
    }

    public static String[] creditCardTypes() {
        String[] types = new String[values().length];
        int i = 0;
        for (CreditCardTypeEnum type : values()) {
            types[i] = type.cartType;
            i += 1;
        }
        return types;
    }
}
