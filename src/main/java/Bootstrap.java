import thread.SortTask;

import java.io.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * 启动类
 *
 * @version C10 2018年06月14日
 * @since SDP V300R003C10
 */
public class Bootstrap
{

    /**
     * 文件编码
     */
    private static final String CHARSETNAME = "UTF-8";

    /**
     * 排序前的数组
     */
    private static long[] ARRAY = null;

    public static void main(String[] args)
    {
        System.out.println("Hello World !");

        // 获取文件行数
        int lines = getFileLines();

        // 设置排序前和排序后的数组大小
        ARRAY = new long[lines];

        // 读取文件写入到BEFORE_ARRAY中
        readFile();

        // 利用forkjoin排序
        forkJoinSort();

        // 将排序后的结果写入到新文件
        writeFile();
    }

    /**
     * 文件行数
     *
     * @return lines
     */
    private static int getFileLines()
    {
        long beginTime = System.currentTimeMillis();

        String filename = Bootstrap.class.getResource("/msisdn.txt").getFile();
        File readFile = new File(filename);

        int index = 0;
        try (BufferedReader br =
                 new BufferedReader(new InputStreamReader(new FileInputStream(readFile), CHARSETNAME));)
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                index++;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("read file lines times = %s", (endTime - beginTime)));

        return index;
    }

    /**
     * 读取文件
     */
    private static void readFile()
    {
        long beginTime = System.currentTimeMillis();

        String filename = Bootstrap.class.getResource("/msisdn.txt").getFile();
        File readFile = new File(filename);

        int index = 0;
        try (BufferedReader br =
                 new BufferedReader(new InputStreamReader(new FileInputStream(readFile), CHARSETNAME));)
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                ARRAY[index++] = Long.valueOf(line);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("read file times = %s", (endTime - beginTime)));
    }

    /**
     * 写入新文件
     */
    private static void writeFile()
    {
        long beginTime = System.currentTimeMillis();

        String filename = Bootstrap.class.getResource("/sortFile.txt").getFile();
        File sortFile = new File(filename);

        try (
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sortFile), CHARSETNAME)))
        {
            for (int i = 0; i < ARRAY.length; i++)
            {
                if (ARRAY[i] == 0)
                {
                    continue;
                }

                bw.write(String.valueOf(ARRAY[i]));
                bw.newLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("write new file times = %s", (endTime - beginTime)));
    }

    /**
     * 多线程排序
     */
    private static void forkJoinSort()
    {
        long beginTime = System.currentTimeMillis();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(new SortTask(ARRAY, 0, ARRAY.length - 1));
        forkJoinPool.shutdown();
        try
        {
            forkJoinPool.awaitTermination(10000, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("sort file times = %s", (endTime - beginTime)));
    }
}
