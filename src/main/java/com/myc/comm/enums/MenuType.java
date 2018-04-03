package com.myc.comm.enums;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/20 14:23
 */
public enum MenuType {
    navigation(1),
    button(2);

    private int value;

    MenuType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
