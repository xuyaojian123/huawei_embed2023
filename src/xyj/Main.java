package xyj;

import java.util.*;


class Edge {
    int sno; //边的编号
    int weight; // 边权重
    boolean[] channel;

    public Edge(int sno, int weight, boolean[] channel) {
        this.sno = sno;
        this.weight = weight;
        this.channel = channel;
    }
}

class Node {
    int vertex;
    int distance;

    public Node(int vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }
}

class Business {
    int b_sno;//业务编号
    int start; //业务起点
    int end; //业务终点

    int channel;
    List<Integer> business_path;
    List<Integer> amplifier_sno;

    public Business(int b_sno, int start, int end) {
        this.start = start;
        this.end = end;
        this.b_sno = b_sno;
    }
}

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    private static int N;
    private static int M;
    private static int M_copy;
    private static int T;
    private static int P;
    private static int D;

    private static final Map<String, List<Edge>> graph = new HashMap<>();
    private static final List<List<Node>> graph_memo = new ArrayList<>();//邻接表
    private static List<List<Node>> graph_memo_memo = new ArrayList<>();//邻接表
    private static final List<int[]> add_edge = new ArrayList<>();
    private static final List<Business> business = new ArrayList<>();

    public static void readInfo() {
        // (2 ≤ N, M ≤ 5000; 2 ≤ T ≤ 10,000; 2 ≤ P ≤ 80; 2 ≤ D ≤ 1000)
        N = scanner.nextInt();
        M = scanner.nextInt();
        M_copy = M;
        T = scanner.nextInt();
        P = scanner.nextInt();
        D = scanner.nextInt();
        for (int i = 0; i < N; i++) {
            graph_memo.add(new ArrayList<>());
            graph_memo_memo.add(new ArrayList<>());
        }
        for (int i = 0; i < M; i++) {
            int s_i = scanner.nextInt();
            int t_i = scanner.nextInt();
            int d_i = scanner.nextInt();
            boolean[] channel = new boolean[P];
            int min = Math.min(s_i, t_i);
            int max = Math.max(s_i, t_i);
            String s = min + "+" + max;
            List<Edge> edges = graph.getOrDefault(s, new ArrayList<>());
            edges.add(new Edge(i, d_i, channel));
            graph.put(s, edges);
            List<Node> nodes1 = graph_memo.get(s_i);
            List<Node> nodes2 = graph_memo.get(t_i);
            List<Node> nodes3 = graph_memo_memo.get(s_i);
            List<Node> nodes4 = graph_memo_memo.get(t_i);
            int flag = 0;
            for (Node node : nodes1) {
                if (node.vertex == t_i) {
                    node.distance = Math.min(node.distance, d_i);
                    flag = 1;
                }
            }
            for (Node node : nodes3) {
                if (node.vertex == t_i) {
                    node.distance = Math.min(node.distance, d_i);
                }
            }
            for (Node node : nodes2) {
                if (node.vertex == s_i) {
                    node.distance = Math.min(node.distance, d_i);
                }
            }
            for (Node node : nodes4) {
                if (node.vertex == t_i) {
                    node.distance = Math.min(node.distance, d_i);
                    flag = 1;
                }
            }
            if (flag == 0) {
                graph_memo.get(s_i).add(new Node(t_i, d_i));
                graph_memo.get(t_i).add(new Node(s_i, d_i));
                graph_memo_memo.get(s_i).add(new Node(t_i, d_i));
                graph_memo_memo.get(t_i).add(new Node(s_i, d_i));
            }
        }
        for (int i = 0; i < T; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            business.add(new Business(i, a, b));
        }
        //业务排序
        business.sort((o1, o2) -> {
            if (o1.start == o2.start) {
                return o1.end - o2.end;
            }
            return o1.start - o2.start;
        });
        // 对边的距离从小到大排序
        for (String key : graph.keySet()) {
            List<Edge> edges = graph.get(key);
            edges.sort((o1, o2) -> o1.weight - o2.weight);
        }

    }

    public static List<Integer> printShortestPath(int[] prev, int destination) { //迭代法
        ArrayList<Integer> path = new ArrayList<>();
        int curr = destination;
        while (curr != -1) {
            path.add(curr);
            curr = prev[curr];
        }
        //反转路径，使其从起始顶点到目标顶点
        //int size = path.size();
        //for (int i = size - 1; i >= 0; i--) {
        //    System.out.print(path.get(i));
        //    if (i > 0) {
        //        System.out.print(" -> ");
        //    }
        //}
        //System.out.println();
        return path;
    }

    public static int[] dijkstra(int source) {
        int[] path = new int[N];
        Arrays.fill(path, -1);
        //path[source] = -1;
        int[] dist = new int[N]; // 存储起点到各点的最短距离
        boolean[] visited = new boolean[N]; // 存储节点是否已被访问
        Arrays.fill(dist, Integer.MAX_VALUE); // 初始化距离数组为最大值
        Arrays.fill(visited, false); // 初始化visited数组为false
        dist[source] = 0; // 起点到自身的距离为0
        // 采用优先队列，优化迪杰斯特拉算法
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(a -> dist[a]));
        pq.offer(source); // 将起始顶点加入优先队列
        while (!pq.isEmpty()) {
            int curr = pq.poll();//从优先队列中取出当前顶点，距离最小的顶点编号
            if (visited[curr]) {
                continue; // Skip visited vertex
            }
            visited[curr] = true;
            for (Node neighbor : graph_memo.get(curr)) {
                int v = neighbor.vertex;
                int weight = neighbor.distance;
                if (weight == Integer.MAX_VALUE) {
                    continue;
                }
                int newDistance = dist[curr] + weight;
                if (!visited[v] && newDistance < dist[v]) {
                    dist[v] = newDistance;
                    pq.offer(v);
                    path[v] = curr;
                }
            }
        }
        return path;
    }

    public static int[] dijkstra1(int source) {
        int[] path = new int[N];
        path[source] = -1;
        int[] dist = new int[N]; // 存储起点到各点的最短距离
        boolean[] visited = new boolean[N]; // 存储节点是否已被访问
        Arrays.fill(dist, Integer.MAX_VALUE); // 初始化距离数组为最大值
        Arrays.fill(visited, false); // 初始化visited数组为false
        dist[source] = 0; // 起点到自身的距离为0
        // 采用优先队列，优化迪杰斯特拉算法
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(a -> dist[a]));
        pq.offer(source); // 将起始顶点加入优先队列
        while (!pq.isEmpty()) {
            int curr = pq.poll();//从优先队列中取出当前顶点，距离最小的顶点编号
            if (visited[curr]) {
                continue; // Skip visited vertex
            }
            visited[curr] = true;
            for (Node neighbor : graph_memo_memo.get(curr)) {
                int v = neighbor.vertex;
                int weight = neighbor.distance;
                int newDistance = dist[curr] + weight;
                if (!visited[v] && newDistance < dist[v]) {
                    dist[v] = newDistance;
                    pq.offer(v);
                    path[v] = curr;
                }
            }
        }
        return path;
    }

    public static void main(String[] args) {
        // 输入信息读取
        readInfo();
        int[] paths = null;
        List<Integer> path = null;
        for (int i = 0; i < T; i++) {
            //System.out.println("执行业务：" + i);
            Business oneBusiness = business.get(i);
            int start = oneBusiness.start, end = oneBusiness.end;
            int flag = 0;
            for (int p = 0; p < P; p++) {
                //int p = selectChannel(path);
                for (int j = 0; j < graph_memo.size(); j++) {
                    for (int k = 0; k < graph_memo.get(j).size(); k++) {
                        Node node = graph_memo.get(j).get(k);
                        node.distance = Integer.MAX_VALUE;
                        int a = Math.min(j, node.vertex);
                        int b = Math.max(j, node.vertex);
                        String s = a + "+" + b;
                        List<Edge> edges = graph.get(s);
                        for (int l = 0; l < edges.size(); l++) {
                            Edge edge = edges.get(l);
                            if (!edge.channel[p]) {
                                node.distance = Math.min(node.distance, edge.weight);
                                break;
                            }
                        }
                    }
                }
                paths = dijkstra(start);
                if (paths[end] != -1) {
                    path = printShortestPath(paths, end);
                    //int channel = selectChannel(path);
                    oneBusiness.channel = p;
                    List<Integer> result = new ArrayList<>();
                    List<Integer> add_amplifier = new ArrayList<>();
                    int distance = 0;
                    for (int j = path.size() - 1; j >= 1; j--) {
                        int a = Math.min(path.get(j - 1), path.get(j));
                        int b = Math.max(path.get(j - 1), path.get(j));
                        String s = a + "+" + b;
                        List<Edge> edges = graph.get(s);
                        for (int k = 0; k < edges.size(); k++) {
                            Edge edge = edges.get(k);
                            if (!edge.channel[p]) {
                                distance = distance + edge.weight;
                                if (distance > D) {
                                    add_amplifier.add(path.get(j));
                                    distance = edge.weight;
                                }
                                result.add(edge.sno);
                                edge.channel[p] = true;
                                break;
                            }
                        }
                    }
                    oneBusiness.business_path = result;
                    oneBusiness.amplifier_sno = add_amplifier;
                    flag = 1;
                    //break;
                }
            }
            if (flag == 0) {
                paths = dijkstra1(start);
                path = printShortestPath(paths, end);
                int channel = selectChannel(path);
                oneBusiness.channel = channel;
                List<Integer> result = new ArrayList<>();
                List<Integer> add_amplifier = new ArrayList<>();
                int distance = 0;
                for (int j = path.size() - 1; j >= 1; j--) {
                    int a = Math.min(path.get(j - 1), path.get(j));
                    int b = Math.max(path.get(j - 1), path.get(j));
                    String s = a + "+" + b;
                    List<Edge> edges = graph.get(s);
                    int ans = 0;
                    for (int k = 0; k < edges.size(); k++) {
                        Edge edge = edges.get(k);
                        if (!edge.channel[channel]) {
                            distance = distance + edge.weight;
                            if (distance > D) {
                                add_amplifier.add(path.get(j));
                                distance = edge.weight;
                            }
                            result.add(edge.sno);
                            edge.channel[channel] = true;
                            ans = 1;
                            break;
                        }
                    }
                    if (ans == 0) {
                        List<Edge> edges1 = addEdge(a, b);
                        edges1.get(0).channel[channel] = true;
                        distance = distance + edges1.get(0).weight;
                        if (distance > D) {
                            add_amplifier.add(path.get(j));
                            distance = edges1.get(0).weight;
                        }
                        result.add(edges1.get(0).sno);
                    }
                }
                oneBusiness.business_path = result;
                oneBusiness.amplifier_sno = add_amplifier;
            }
        }
        printResult();
    }

    // 选择添加边最少的通道，暴力遍历
    public static int selectChannel(List<Integer> path) {
        int select = random.nextInt(P);
        //int min = Integer.MAX_VALUE;
        //int select = 0;
        //for (int i = 0; i < P; i++) {
        //    int num = 0;
        //    for (int j = path.size()-1; j>=1; j--) {
        //        int a = Math.min(path.get(j-1),path.get(j));
        //        int b = Math.max(path.get(j-1),path.get(j));
        //        String s = a + "+" + b;
        //        List<Edge> edges = graph.get(s);
        //        int flag = 0;
        //        for (int k = 0; k < edges.size(); k++) {
        //            Edge edge = edges.get(k);
        //            if (!edge.channel[i]){
        //                flag = 1;
        //                break;
        //            }
        //        }
        //        if (flag==0) num++;
        //    }
        //    if (num < min){
        //        min = num;
        //        select = i;
        //        if (num == 0){
        //            break;
        //        }
        //    }
        //}
        return select;
    }

    public static List<Edge> addEdge(int a, int b) {
        if (a > b) {
            System.exit(0);
        }
        String s = a + "+" + b;
        List<Edge> edges = graph.get(s);
        add_edge.add(new int[]{a, b});
        edges.add(0, new Edge(M_copy++, edges.get(0).weight, new boolean[P]));
        return edges;
    }

    //打印结果
    public static void printResult() {
        business.sort((o1, o2) -> o1.b_sno - o2.b_sno);
        int add = M_copy - M;
        System.out.println(add);
        for (int[] edge : add_edge) {
            System.out.println(edge[0] + " " + edge[1]);
        }
        for (int i = 0; i < business.size(); i++) {
            Business one_business = business.get(i);
            System.out.print(one_business.channel + " " + one_business.business_path.size() + " " + one_business.amplifier_sno.size());
            for (int j = 0; j < one_business.business_path.size(); j++) {
                System.out.print(" " + one_business.business_path.get(j));
            }
            for (int j = 0; j < one_business.amplifier_sno.size(); j++) {
                System.out.print(" " + one_business.amplifier_sno.get(j));
            }
            System.out.println();
        }

    }
}
