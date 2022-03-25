package com.temp.cube.turn;

import com.temp.cube.model.Side;
import com.temp.cube.utils.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class TurnChain {

    private List<ChainNode> chainNodes;

    private TurnChain() {}

    public static TurnChain make(ChainNode... chainNode) {
        TurnChain chain = new TurnChain();
        chain.chainNodes = Arrays.asList(chainNode);
        return chain;
    }

    public void process() {
        ChainNode firstNode = chainNodes.get(0);
        String[] tempLine = firstNode.getSide().getLine(firstNode.getLineType());
        for (int i = 1; i < chainNodes.size(); i++) {
            ChainNode currentNode = chainNodes.get(i);
            tempLine = currentNode.getSide().changeAndGet(currentNode.getLineType(), currentNode.isReverseArray() ? ArrayUtils.reverse(tempLine) : tempLine);
        }
        firstNode.getSide().changeAndGet(firstNode.getLineType(), firstNode.isReverseArray() ? ArrayUtils.reverse(tempLine) : tempLine);
    }

    public static class ChainNode {

        private Side side;

        private Side.LineType lineType;

        private boolean isReverseArray;

        public ChainNode(Side side, Side.LineType lineType, boolean isReverseArray) {
            this.side = side;
            this.lineType = lineType;
            this.isReverseArray = isReverseArray;
        }

        public Side getSide() {
            return side;
        }

        public Side.LineType getLineType() {
            return lineType;
        }

        public boolean isReverseArray() {
            return isReverseArray;
        }
    }
}
