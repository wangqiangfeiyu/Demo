import thread.SortTask;

import java.io.*;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;

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
    private static String[] ARRAY = null;

    private static final int THREADS = 12;

    public static void main(String[] args)
    {
        System.out.println("welcome...start!");

        // 获取文件行数
        int lines = getFileLines();

        // 设置排序前和排序后的数组大小
        ARRAY = new String[lines];

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
        File file = new File("D:\\IDE-WorkSpace\\DemoWq\\in.txt");

        int index = 0;
        try (BufferedReader br =
                 new BufferedReader(new InputStreamReader(new FileInputStream(file), CHARSETNAME));)
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

        File readFile = new File("D:\\IDE-WorkSpace\\DemoWq\\in.txt");

        int index = 0;
        try (BufferedReader br =
                 new BufferedReader(new InputStreamReader(new FileInputStream(readFile), CHARSETNAME));)
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                ARRAY[index++] = String.valueOf(line);
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
        File sortFile = new File("D:\\IDE-WorkSpace\\DemoWq\\out.txt");

        try (
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sortFile), CHARSETNAME)))
        {
            for (int i = 0; i < ARRAY.length; i++)
            {

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
        Date start = new Date();
        SortTask task = new SortTask(ARRAY);
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREADS);
        forkJoinPool.invoke(task);
        Date end = new Date();
        long timeDiff = end.getTime() - start.getTime();
        System.out.println("sort:" + timeDiff + "ms");
    }
}
