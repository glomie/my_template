package com.temp.cube.finder;

import com.temp.cube.enums.Color;
import com.temp.cube.enums.Layer;
import com.temp.cube.model.Cube;
import com.temp.cube.model.EdgeBlock;
import com.temp.cube.model.EdgePosition;

import java.util.List;

public class DefaultEdgeBlockFinder implements EdgeBlockFinder {

    private Cube cube;

    public DefaultEdgeBlockFinder(Cube cube) {
        this.cube = cube;
    }

    @Override
    public EdgePosition find(EdgeBlock block) {
        return null;
    }

    @Override
    public List<EdgePosition> find(Color color, Layer layer) {
        return null;
    }
}
