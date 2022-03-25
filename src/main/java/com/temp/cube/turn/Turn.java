package com.temp.cube.turn;

import com.temp.cube.constants.Direction;
import com.temp.cube.model.Cube;

public interface Turn {

    /**
     * 转动
     * @param cube
     * @param direction 方向
     * @return
     */
    void turn(Cube cube, Direction direction);
}
