package xyj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Test {

    //public static void main(String[] args) throws IOException {
    //    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./2.txt"));
    //    int N = 5000;
    //    int M = 5000;
    //    int T = 10000;
    //    int P = 80;
    //    int D = 1000;
    //    bufferedWriter.write("5000 5000 10000 80 1000\n");
    //    Random random = new Random();
    //    int a =0,b=1,c=0;
    //    for (int i = 0; i < M; i++) {
    //        c = random.nextInt(D+1);
    //        String str = a + " " + b + " " + c;
    //        a++;b++;
    //        if (i==M-1){
    //            a = 0;
    //            b = M-1;
    //            str = a + " " + b + " " + c;
    //        }
    //        bufferedWriter.write(str);
    //        bufferedWriter.write("\n");
    //    }
    //    for (int i = 0; i < T; i++) {
    //        a = random.nextInt(N);
    //        b = random.nextInt(N);
    //        while (a==b){
    //            a = random.nextInt(N);
    //            b = random.nextInt(N);
    //        }
    //        String str = a + " " + b;
    //        bufferedWriter.write(str);
    //        bufferedWriter.write("\n");
    //    }
    //    bufferedWriter.flush();
    //    bufferedWriter.close();
    //}

    public static void main(String[] args) {
        long result;
        long startTime = System.currentTimeMillis(); // 记录开始时间

        // 进行5000*10000次乘法运算
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 5000; j++) {
                for (int k = 0; k < 5000; k++) {
                    for (int l = 0; l < 50; l++) {
                        result = i * j * k * l;
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis(); // 记录结束时间
        long elapsedTime = endTime - startTime; // 计算耗时
        System.out.println("总耗时（毫秒）：" + elapsedTime);
    }
}
