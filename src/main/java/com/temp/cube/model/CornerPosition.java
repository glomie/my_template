package com.temp.cube.model;

import com.temp.cube.enums.SideEnum;

/**
 * 角块位置
 */
public class CornerPosition extends Position {

    private SideEnum sideEnum1;

    private SideEnum sideEnum2;

    private SideEnum sideEnum3;

    public CornerPosition(SideEnum sideEnum1, SideEnum sideEnum2, SideEnum sideEnum3) {
        this.sideEnum1 = sideEnum1;
        this.sideEnum2 = sideEnum2;
        this.sideEnum3 = sideEnum3;
    }
}
