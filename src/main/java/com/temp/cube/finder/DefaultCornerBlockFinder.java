package com.temp.cube.finder;

import com.temp.cube.enums.Color;
import com.temp.cube.enums.Layer;
import com.temp.cube.model.CornerBlock;
import com.temp.cube.model.CornerPosition;
import com.temp.cube.model.Cube;

import java.util.List;

public class DefaultCornerBlockFinder implements CornerBlockFinder {

    private Cube cube;

    public DefaultCornerBlockFinder(Cube cube) {
        this.cube = cube;
    }

    @Override
    public CornerPosition find(CornerBlock block) {
        return null;
    }

    @Override
    public List<CornerPosition> find(Color color, Layer layer) {
        return null;
    }
}
