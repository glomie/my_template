package com.temp.cube.finder;

import com.temp.cube.enums.Color;
import com.temp.cube.model.Block;
import com.temp.cube.enums.Layer;
import com.temp.cube.model.Position;

import java.util.List;

public interface BlockFinder<Pos extends Position, Blk extends Block> {

    /**
     * 定位某一块的所在位置
     * @param block
     * @return
     */
    Pos find(Blk block);

    /**
     * 定位某一层某一颜色的所在位置
     * @param color
     * @param layer
     * @return
     */
    List<Pos> find(Color color, Layer layer);
}
