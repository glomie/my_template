package com.temp.cube.model;


import com.temp.cube.enums.SideEnum;

/**
 * 棱块位置
 */
public class EdgePosition extends Position {

    private SideEnum sideEnum1;

    private SideEnum sideEnum2;

    public EdgePosition(SideEnum sideEnum1, SideEnum sideEnum2) {
        this.sideEnum1 = sideEnum1;
        this.sideEnum2 = sideEnum2;
    }
}
