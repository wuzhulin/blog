import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;


public class test {

    public Node connect(Node root) {
        if(root==null) return root;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node temp = queue.poll();
                temp.next = queue.peek();
                if (temp.left != null) {
                    queue.add(temp.left);
                }
                if (temp.right != null) {
                    queue.add(temp.right);
                }
            }
        }
        return root;
    }

    public boolean increasingTriplet(int[] nums) {
        int mid = Integer.MAX_VALUE,min = Integer.MAX_VALUE;
        for (int num : nums) {
            if(num < min){
                min = num;
            }else if(num > min && num < mid) {
                mid = num;
            }else if(num>min && num > mid) {
                return true;
            }
        }
        return false;
    }

    public int[] productExceptSelf(int[] nums) {
        int[] pre =new int[nums.length];
        int[] suf =new int[nums.length];
        pre[0] = 1;
        suf[nums.length-1] = 1;
        for (int i = 1; i < nums.length; i++) {
            pre[i] *= nums[i-1];
        }
        for (int i = nums.length - 2; i >= 0; i--) {
            suf[i] *= nums[i+1];
        }
        for (int i = 0; i < nums.length; i++) {
            nums[i] = pre[i] * suf[i];
        }
        return nums;
    }

    public int subarraySum(int[] nums, int k) {
        int pre = 0 , count = 0;
        Map<Integer,Integer> map = new HashMap<>();
        map.put(0,1);
        for (int i = 0; i < nums.length; i++) {
            pre += nums[i];
            if(map.containsKey(pre-k)) {
                count++;
            }
            map.put(pre,map.getOrDefault(pre,0)+1);
        }
        return count;
    }


    public int shortestPathBinaryMatrix(int[][] grid) {
        if (grid[0][0] == 1) return -1;
        int n = grid.length , m = n , num = 0;
        int[][] div = {{-1, 0},{1,0},{0,1},{0,-1},{-1,1},{-1,-1},{1,1},{1,-1}};
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0,0});
        while(!queue.isEmpty()) {
            num++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] temp = queue.poll();
                if(temp[0] == n-1 && temp[1] == m-1) return num;
                for (int[] ints : div) {
                    int x = ints[0] + temp[0];
                    int y = ints[1] + temp[0];
                    if(x>=0&&x<n&&y>=0&&y<m&&grid[x][y] == 0) {
                        grid[x][y] = 1;
                        queue.add(new int[]{x,y});
                    }
                }
            }
        }
        return -1;
    }

    int[][] div = {{-1, 0},{1,0},{0,1},{0,-1}};
    public void solve(char[][] board) {
        int n = board.length-1,m=board[0].length-1;
        for (int i = 0; i < board.length; i++) {
            if(board[0][i] == 'O') dfs(board,0,i);
            if(board[n][i]=='O') dfs(board,n,i);
            if(board[i][0]=='O') dfs(board,i,0);
            if(board[i][m]=='O') dfs(board,i,m);
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j]=='O') board[i][j] = 'X';
                if (board[i][j]=='Z') board[i][j] = 'O';
            }
        }
    }
    public void dfs(char[][] board , int i,int j) {
        board[i][j] = 'Z';
        for (int[] ints : div) {
            int x = ints[0]+i;
            int y = ints[1]+j;
            if(x>=0&&x<board.length&&y>=0&&y<board[0].length&&board[x][y]=='O') {
                dfs(board,x,y);
            }
        }
    }


    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        dfs(graph,0);
        list.add(0);
        return lists;
    }

    public void dfs(int[][] graph , int index) {
        if(index == graph.length-1) {
            lists.add(new ArrayList<>(list));
            return ;
        }
        for (int i = 0; i < graph[index].length; i++) {
            list.add(graph[index][i]);
            dfs(graph,graph[index][i]);
            list.remove(list.size()-1);
        }
    }

    public String addStrings(String num1, String num2) {
        StringBuffer sb = new StringBuffer();
        int i = num1.length()-1, j = num2.length()-1 , add = 0;
        while(i>=0 || j >= 0 || add > 0){
            int x = i >= 0 ? num1.charAt(i) - '0' : 0 ;
            int y = j >= 0 ? num2.charAt(j) - '0' : 0 ;
            int result = x + y + add ;
            sb.append(result % 10);
            add = result / 10;
            i--;
            j--;
        }
        return sb.reverse().toString();
    }

    public int longestPalindrome(String s) {
        HashMap<Character, Integer> map =new HashMap<>();
        for (char c : s.toCharArray()) {
            map.put(c,map.getOrDefault(c,0)+1);
        }
        int count = 0;
        for(Map.Entry<Character,Integer> entry : map.entrySet()) {
            count += entry.getValue()/2;
        }
        if(count < s.length()) count++;
        return count;
    }


    public List<List<Integer>> subsets(int[] nums) {
        dfs(nums,0);
        return lists;
    }
    public void dfs(int[] nums,int index) {
        if(list.size() == nums.length+1) {
            return;
        }
        lists.add(new ArrayList<>(list));
        for (int i = index; i < nums.length; i++) {
            if(i>index && nums[i] == nums[i-1]) continue;
            list.add(nums[i]);
            dfs(nums,i+1);
        }
    }

    public boolean wordPattern(String pattern, String s) {
        Map<String,Character> map = new HashMap<>();
        String[] word = s.split(" ");
        for (int i = 0; i < word.length; i++) {
            if (map.get(word[i]) != null) {
                if(map.get(word[i]) != pattern.charAt(i)) return false;
            }else {
                if(map.containsValue(pattern.charAt(i))) return false;
                map.put(word[i],pattern.charAt(i));
            }
        }

        return true;
    }

    public List<Integer> partitionLabels(String s) {
        List<Integer> list = new ArrayList<>();
        int i = 0,start = 0,end = 0;
        while(i < s.length()) {
            int index = s.lastIndexOf(s.charAt(i));
            end = Math.max(end,index);
            if(end == i) {
                list.add(end - start + 1);
                start = end + 1;
            }else {
                i++;
            }
        }
        return list;
    }


    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        boolean[] isVisited = new boolean[nums.length];
        dfs(nums,isVisited);
        return lists;
    }

    public void dfs(int[] nums,boolean[] isVisited) {
        if(list.size() == nums.length) {
            lists.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if(isVisited[i]) continue;
            if(i > 0 && nums[i] == nums[i-1] && isVisited[i-1] ) continue;
            isVisited[i] = true;
            list.add(nums[i]);
            dfs(nums, isVisited);
            isVisited[i] = false;
            list.remove(list.size()-1);
        }
    }

    List<List<Integer>> lists = new ArrayList<>();
    List<Integer> list = new ArrayList<>();
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        dfs(candidates,target,0,0);
        return lists;
    }

    public void dfs(int[] candidates,int target,int index,int sum) {
        if (sum>target) {
            return;
        }
        if (sum==target) {
            lists.add(new ArrayList<>(list));
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if(i > index && candidates[i] == candidates[i-1]) continue;
            list.add(candidates[i]);
            dfs(candidates,target,i+1,sum+candidates[i]);
            list.remove(list.size()-1);
        }
    }



    public List<List<String>> groupAnagrams(String[] strs) {
        List<String> temp;
        Map<String,List<String>> map = new HashMap<>();
        for (int i = 0; i < strs.length; i++) {
            char[] word = strs[i].toCharArray();
            Arrays.sort(word);
            String str = new String(word);
            temp = map.getOrDefault(str,new ArrayList<>());
            temp.add(strs[i]);
            map.put(str,temp);
        }
        return new ArrayList<>(map.values());
    }

    public String multiply(String num1, String num2) {
        if (num1.equals("0")||num2.equals("0")) {
            return "0";
        }
        String[] strs = new String[num1.length()];
        String temp = "";
        for (int i = num1.length() - 1; i >= 0; i--) {
            char c = num1.charAt(i);
            StringBuffer sb = new StringBuffer(temp);
            int mul = 0;
            for (int j = num2.length() - 1; j >= 0 ; j--) {
                int result = (c - '0') * (num2.charAt(j) - '0') + mul;
                sb.append(result % 10);
                mul = result / 10;
            }
            if(mul != 0) sb.append(mul);
            strs[i] = sb.reverse().toString();
            temp += "0";
        }
        String ans = "";
        for (String str : strs) {
            ans = add(str,ans);
        }
        return ans;
    }
    public String add(String num1,String num2) {
        int i = num1.length() - 1, j = num2.length()-1,add = 0;
        StringBuffer sb = new StringBuffer();
        while(i >= 0 || j >= 0 || add > 0) {
            int x = i >= 0 ? num1.charAt(i) : '0';
            int y = j >= 0 ? num2.charAt(j) : '0';
            int result = (x-'0') + (y-'0') + add;
            sb.append(result%10);
            add = result/10;
            i--;
            j--;
        }
        return sb.reverse().toString();
    }


    public List<String> letterCombinations(String digits) {
        String[] word = {"","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
        dfs(digits,0,word,"");
        return ans;
    }

    public void dfs(String digits,int index, String[] word,String s) {
        if(index == digits.length()) {
            ans.add(s);
        }
        for (int i = 0; i < word[digits.charAt(index)-'0'].length(); i++) {
            dfs(digits,index+1,word,s+word[digits.charAt(index)-'0'].charAt(i));
        }
    }

    List<String> ans = new ArrayList<>();
    public List<String> generateParenthesis(int n) {
        dfs(n,"",0,0);
        return ans;
    }

    public void dfs(int n , String s,int left , int right) {
        if(n*2 == s.length()) {
            ans.add(s);
            return;
        }
        if(right < left) {
            dfs(n,s+")",left,right+1);
        }
        if(left <= n) {
            dfs(n,s+"(",left+1,right);
        }
    }

    public boolean exist(char[][] board, String word) {
        int[][] div = {{-1, 0},{1,0},{0,1},{0,-1}};
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == word.charAt(0)){
                    boolean flag = dfs(board,word,i,j,div,0);
                    if(flag) return true;
                }
            }
        }
        return false;
    }

    public boolean dfs(char[][] board,String word,int i,int j,int[][] div,int index) {
        if(i < 0|| i >= board.length || j < 0 || j >= board[0].length || word.charAt(index-1) != board[i][j]) return false;
        for (int k = 0; k < div.length; k++) {
            int x = i + div[k][0];
            int y = i + div[k][1];
            if (dfs(board,word,x,y,div,index+1)) return true;
        }
        return false;
    }

    public List<String> findRepeatedDnaSequences(String s) {
        List<String> list = new ArrayList<>();
        int length = s.length();
        Set<String> set = new HashSet<>();
        if(length < 10) return list;
        for (int i = 0; i < s.length() - 10; i++) {
            if(!set.add(s.substring(i, 10 + 1)) && !list.contains(s.substring(i,10+1)))
                list.add(s.substring(i,10+1));
        }
        return list;
    }

    public int rob(int[] nums) {
        return temp(nums,0,nums.length-2);
    }

    public int temp(int[] nums , int start , int end) {
        int[] dp = new int[nums.length];
        for (int i = start; i < end; i++) {
            dp[i] = Math.max(dp[i-2]+nums[i],dp[i-1]);
        }
        System.out.println(Arrays.toString(dp));
        return dp[end];
    }

    public boolean canJump(int[] nums) {
        int rightmost = 0, n = nums.length;
        for (int i = 0; i < nums.length; i++) {
            if (i <= rightmost) {
                rightmost = Math.max(rightmost, i + nums[i]);
                if (rightmost >= n - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public int jump(int[] nums) {
        int length = nums.length;
        int end = 0, MaxPosition = 0,steps = 0;
        for (int i = 0; i < length - 1; i++) {
            MaxPosition = Math.max(MaxPosition,i+nums[i]);
            if(i == end) {
                end = MaxPosition;
                steps++;
            }
        }
        return steps;
    }

    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < n; i++) {
            dp[0][i] = i;
        }
        for (int i = 0; i < m; i++) {
            dp[i][0] = i;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i][j-1] + dp[i-1][j];
            }
        }
        for (int[] ints : dp) {
            System.out.println(Arrays.toString(ints));
        }
        return dp[m-1][n-1];
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        Set<ListNode> set = new HashSet<>();
        ListNode temp = headA;
        while (headA != null) {
            set.add(headA);
            temp = temp.next;
        }
        temp = headB;
        while (headB != null) {
            if(set.contains(temp)) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }


    @Test
    public void test() {
        char[][] test = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
        exist(test,"ABCCED");
    }

}

class ListNode {
      int val;
      ListNode next;
      ListNode(int x) {
          val = x;
          next = null;
      }
}

class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
}
