package com.temp.leetcode;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.solve();
    }

    public void solve() {
        // trigger solve() to run the leetcode method
        myMethod(null, null, null);
    }

    public int[] myMethod(int[] a, int[] b, int[] c) {
        int i = 0, j = 0, k = 0;
        List<Integer> list = new ArrayList<>();
        while (true) {
            if (a[i] == b[j] && b[j] == c[k]) {
                list.add(a[i]);
                if (i == a.length - 1 || j == b.length - 1 || k == c.length - 1) {
                    break;
                }
            } else {
                int min = Math.min(Math.min(a[i], b[j]), c[k]);
                if (a[i] == min && i < a.length - 1) {
                    i++;
                }
                if (b[j] == min && j < b.length - 1) {
                    j++;
                }
                if (c[k] == min && k < c.length - 1) {
                    k++;
                }
            }

        }
        return list.stream().mapToInt(value -> value).toArray();
    }



    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                if (nums[i] + nums[left] + nums[right] > 0) {
                    right--;
                } else if (nums[i] + nums[left] + nums[right] < 0) {
                    left++;
                } else {
                    if (left == i + 1 || nums[left] != nums[left - 1]) {
                        result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    }
                    left++;
                    right--;
                }
            }
        }
        return result;
    }

    public static class MyStack {
        // 1 2 3
        // stack1 3 2 1
        // stack2 1 2 3
        private Stack<Integer> stack1;
        private Stack<Integer> stack2;

        public MyStack() {
            stack1 = new Stack<>();
            stack2 = new Stack<>();
        }

        public void offer(int num) {
            stack1.push(num);
        }

        public int poll() {
            if (stack2.isEmpty()) {
                while (!stack1.isEmpty()) {
                    stack2.push(stack1.pop());
                }
            }
            if (stack2.isEmpty()) {
                return -1;
            }
            return stack2.pop();
        }

    }

    public int method(String[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    // do nothing
                } else if (i == 0) {
                    dp[i][j] = dp[i][j - 1] + fetchRightTime(grid[i][j - 1]);
                } else if (j == 0) {
                    dp[i][j] = dp[i - 1][j] + fetchDownTime(grid[i - 1][j]);
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j] + fetchDownTime(grid[i - 1][j]), dp[i][j - 1] + fetchRightTime(grid[i][j - 1]));
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    private int fetchRightTime(String point) {
        String[] split = point.split(",");
        return Integer.parseInt(split[0]);
    }

    private int fetchDownTime(String point) {
        String[] split = point.split(",");
        return Integer.parseInt(split[1]);
    }


    public int findMinArrowShots(int[][] points) {
        if (points.length == 1) return 1;
        Arrays.sort(points, Comparator.comparingInt(o -> o[0]));
        int pos = Integer.MIN_VALUE;
        int ans = 0;
        for (int[] point : points) {
            if (point[0] > pos) {
                pos = point[1];
                ans++;
            }
        }
        return ans;
    }

    public List<Integer> rightSideView(TreeNode root) {
        if (root == null) return new ArrayList<>();
        Integer[] arr = new Integer[100];
        travel(root, 0, arr);
        return Arrays.asList(Arrays.copyOf(arr, maxDepth + 1));
    }

    private int maxDepth = 0;

    private void travel(TreeNode node, int curLayer, Integer[] arr) {
        if (node == null) {
            return;
        }
        arr[curLayer] = node.val;
        maxDepth = Math.max(maxDepth, curLayer);
        travel(node.left, curLayer + 1, arr);
        travel(node.right, curLayer + 1, arr);
    }

    public int numRabbits(int[] answers) {
        int[] arr = new int[1000];
        for (int answer : answers) {
            arr[answer]++;
        }
        int need = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                continue;
            }
            int cur = arr[i] % (i + 1);
            if (cur == 0) {
                continue;
            }
            need += i + 1 - cur;
        }
        return answers.length + need;
    }



    public String decodeString(String s) {
        Stack<Object[]> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        int cur = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                cur = cur * 10 + (c - '0');
            } else if (c == '[') {
                stack.push(new Object[] {cur, new StringBuilder()});
                cur = 0;
            } else if (c == ']') {
                Object[] top = stack.pop();
                int count = (int) top[0];
                StringBuilder temp = new StringBuilder();
                for (int j = 0; j < count; j++) {
                    temp.append((StringBuilder) top[1]);
                }
                if (!stack.isEmpty()) {
                    Object[] peek = stack.peek();
                    StringBuilder peekStr = (StringBuilder) peek[1];
                    peekStr.append(temp);
                } else {
                    sb.append(temp);
                }
            } else {
                if (!stack.isEmpty()) {
                    Object[] peek = stack.peek();
                    StringBuilder temp = (StringBuilder) peek[1];
                    temp.append(c);
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private int count;
    private Integer result;

    public int kthSmallest(TreeNode root, int k) {
        travel(root, k);
        return result;
    }

    private void travel(TreeNode node, int k) {
        if (node == null) {
            return;
        }
        travel(node.left, k);
        count++;
        if (count == k) {
            result = node.val;
            return;
        }
        travel(node.right, k);
    }

    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = grid[i][j];
                if (i == 0 && j == 0) {
                } else if (i == 0) {
                    dp[0][j] += dp[0][j - 1];
                } else if (j == 0) {
                    dp[i][0] += dp[i - 1][0];
                } else {
                    dp[i][j] += Math.min(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    public TreeNode sortedArrayToBST(int[] nums) {
        TreeNode lastTreeNode = new TreeNode(nums[0]);
        TreeNode root = null;
        for (int i = 1; i < nums.length; i++) {
            TreeNode treeNode = new TreeNode(nums[i]);
            if (nums[i] > nums[i - 1]) {
                treeNode.left = lastTreeNode;
            } else {
                treeNode.right = lastTreeNode;
            }
            lastTreeNode = treeNode;
            if (root == null && i == nums.length / 2) {
                root = treeNode;
            }
        }
        return root;
    }


    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
            for (int x = 1; x * x <= i; x++) {
                dp[i] = Math.min(dp[i - x * x] + 1, dp[i]);
            }
        }
        return dp[n];
    }

    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int i = 0, j = matrix[0].length - 1;
        while (i < matrix.length && j >= 0) {
            if (matrix[i][j] == target) {
                return true;
            }
            if (matrix[i][j] > target) {
                j--;
                continue;
            }
            if (matrix[i][j] < target) {
                i++;
                continue;
            }
        }
        return false;
    }

    public int lengthOfLongestSubstring(String s) {
        if (s.isEmpty()) {
            return 0;
        }
        int[] exists = new int[100];
        int start = 0, maxLen = 1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            while (exists[c - ' '] > 0) {
                exists[s.charAt(start) - ' ']--;
                start++;
            }
            maxLen = Math.max(maxLen, i + 1 - start);
            exists[c - ' ']++;
        }
        return maxLen;
    }

    private int[] merge(int[] array1, int[] array2, int left, int right) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < array1.length || j < array2.length) {
            boolean isValid1 = i < array1.length && left <= array1[i] && array1[i] <= right;
            boolean isValid2 = j < array2.length && left <= array2[j] && array2[j] <= right;
            if (!isValid1 && !isValid2) {
                i++;
                j++;
            } else if (!isValid1 && isValid2) {
                i++;
                result.add(array2[j++]);
            } else if (isValid1 && !isValid2) {
                result.add(array1[i++]);
                j++;
            } else {
                if (array1[i] < array2[j]) {
                    result.add(array1[i++]);
                } else {
                    result.add(array2[j++]);
                }
            }
        }
        return result.stream().mapToInt(value -> value).toArray();
    }

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(Arrays.asList(1));
        for (int i = 1; i < numRows; i++) {
            List<Integer> layer = new ArrayList<>();
            layer.add(1);
            List<Integer> lastLayer = result.get(i - 1);
            for (int j = 1; j < lastLayer.size(); j++) {
                layer.add(lastLayer.get(j) + lastLayer.get(j - 1));
            }
            layer.add(1);
            result.add(layer);
        }
        return result;
    }

    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        return root;
    }

    public boolean isSymmetric(TreeNode root) {
        return isSymmetric(root.left, root.right);
    }

    private boolean isSymmetric(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null && right != null) {
            return false;
        }
        if (left != null && right == null) {
            return false;
        }
        return left.val == right.val && isSymmetric(left.right, right.left) && isSymmetric(left.left, right.right);
    }

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        List<int[]> result = new ArrayList<>();
        int start = intervals[0][0], end = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] > end) {
                result.add(new int[] {start, end});
                start = intervals[i][0];
                end = intervals[i][1];
            } else {
                end = Math.max(end, intervals[i][1]);
            }
        }
        result.add(new int[] {start, end});
        return result.toArray(new int[][]{});
    }

    public int trap(int[] height) {
        Stack<Integer> stack = new Stack<>();
        int res = 0;
        for (int i = 0; i < height.length; i++) {
            if (stack.isEmpty()) {
                stack.push(i);
            } else {
                if (height[stack.peek()] <= height[i]) {
                    int bottomHeight = height[stack.pop()];
                    while (!stack.isEmpty()) {
                        int leftIndex = stack.peek();
                        if (height[leftIndex] <= height[i]) {
                            res += (i - leftIndex - 1) * (height[leftIndex] - bottomHeight);
                            stack.pop();
                            bottomHeight = height[leftIndex];
                        } else {
                            res += (i - leftIndex - 1) * (height[i] - bottomHeight);
                            break;
                        }
                    }

                }
                stack.push(i);
            }
        }
        return res;
    }

    public int maxProfit(int[] prices) {
        int max = 0, min = Integer.MAX_VALUE;
        for (int price : prices) {
            max = Math.max(max, price - min);
            min = Math.min(min, price);
        }
        return max;
    }

    public ListNode reverseList(ListNode head) {
        ListNode dummyNode = new ListNode();
        ListNode cur = head;
        while (cur != null) {
            ListNode node = new ListNode(cur.val);
            node.next = dummyNode.next;
            dummyNode.next = node;
            cur = cur.next;
        }
        return dummyNode.next;
    }


    public void setZeroes(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        long[] rows = new long[4], cols = new long[4];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    rows[i / 64] |= 1L << (i % 64);
                    cols[j / 64] |= 1L << (j % 64);
                }
            }

        }
        for (int i = 0; i < m; i++) {
            if ((rows[i / 64] & (1L << (i % 64))) > 0) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int j = 0; j < n; j++) {
            if ((cols[j / 64] & (1L << (j % 64))) > 0) {
                for (int i = 0; i < m; i++) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        result.addAll(inorderTraversal(root.left));
        result.add(root.val);
        result.addAll(inorderTraversal(root.right));
        return result;
    }

    public int maxSubArray(int[] nums) {
        int max = nums[0], pre = nums[0];
        for (int i = 1; i < nums.length; i++) {
            pre = Math.max(pre + nums[i], nums[i]);
            max = Math.max(max, pre);
        }
        return max;
    }



    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    public int subarraySum(int[] nums, int k) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            long sum = 0;
            for (int j = i; j >= 0; j--) {
                sum += nums[j];
                if (sum == k) {
                    count++;
                }
            }
        }
        return count;
    }



    public class LRUCache {
        class DLinkedNode {
            int key;
            int value;
            DLinkedNode prev;
            DLinkedNode next;
            public DLinkedNode() {}
            public DLinkedNode(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private final Map<Integer, DLinkedNode> cache = new HashMap<>();
        private int size;
        private final int capacity;
        private final DLinkedNode dummyHead = new DLinkedNode();
        private final DLinkedNode dummyTail = new DLinkedNode();

        public LRUCache(int capacity) {
            this.capacity = capacity;
            dummyHead.next = dummyTail;
            dummyTail.prev = dummyHead;
        }

        public int get(int key) {
            DLinkedNode node = cache.get(key);
            if (node == null) {
                return -1;
            }
            removeNode(node);
            insertHead(node);
            return node.value;
        }

        public void put(int key, int value) {
            DLinkedNode node = cache.get(key);
            if (node == null) {
                DLinkedNode newNode = new DLinkedNode(key, value);
                insertHead(newNode);
                size++;
                if (size > capacity) {
                    removeNode(dummyTail.prev);
                    cache.remove(dummyTail.prev.key);
                    size--;
                }
            } else {
                node.value = value;
                removeNode(node);
                insertHead(node);
            }
        }

        private void removeNode(DLinkedNode node) {
            DLinkedNode next = node.next;
            DLinkedNode prev = node.prev;
            prev.next = next;
            next.prev = prev;
        }

        private void insertHead(DLinkedNode node) {
            DLinkedNode head = dummyHead.next;
            node.prev = dummyHead;
            node.next = head;
            head.prev = node;
            dummyHead.next = node;
        }

    }



}
