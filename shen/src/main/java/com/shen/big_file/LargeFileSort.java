package com.shen.big_file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;

/**
 * 题目：有10个文件，每个文件有1000万行，文件内容的每一行为一个整型数字；需要，写一个程序，将所有数字排序，分为10个文件输出，如0号文件包含前1000万个数字，1号文件文件包含1千万-2千万之间的数字，依次类推。
 * 限制：如果使用java，-Xmx需要设置为32MB；其它语言也需限制内存为32MB。 要求:正确输出 使用多线程加分编写时长：24。
 * 小时。提供可运行的程序，以及实现说明。
 *
 * 思路：因为内存限制为32MB，将大文件分割成可以进行内部排序的的小文件，然后利用多路归并排序进行最终外部排序。 步骤：
 * 1，产生随机数，生成10个测试文件，10个线程同时进行。
 * 2，将大文件分割1BM的小文件，每个线程对分割而成的内容进行内部排序后，写入文件，利用自定义阻塞线程池，每次同时写入3~4个文件。
 * 3，将所有小文件排序后，利用多路排序算法将小文件写入最终文件。
 *
 * @ClassName: LargeFileSort
 * @Description: 排序十个文件，每个文件一千万个数据
 * @author chujiejie
 * @date: 2018-7-19 下午5:24:54
 *
 */
public class LargeFileSort {

    // 存放测试和结果文件路径，改变环境修改此地址
    private final static String ROOT_FILE_PATH = "/Users/mac/desktop/file";
    // 测试文件
    private static String[] genFiles = new String[10];
    // 切分大文件的512k, 默认为 512k
    private final static int SIZE = 1024*10;
    // 切分文件大小/
    private final static int BYTE_SIZE = SIZE * 1024;
    // 线程任务完成计数器
    private static CountDownLatch doneSignal;
    // 切分文件
    private static final List<File> divFiles = new CopyOnWriteArrayList<>();

    /**
     * 入口函数
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Long root = System.currentTimeMillis();
        // 生成测试数据
        generateTestFiles();

//        Long gen = System.currentTimeMillis();
//        System.out.println("\n***************");
//        System.out.println(String.format("初始化数据完成:%s s。", (gen - root) / 1000));
//        System.out.println("***************");
//        Long divStart = System.currentTimeMillis();
//        // 切分大文件成排序好的小文件，参数为同时执行线程数量
//        divisionAndSortFiles(2);
//
//        Long divEnd = System.currentTimeMillis();
//        System.out.println("\n***************");
//        System.out.println(String.format("切分数据完成:%s s。", (divEnd - divStart) / 1000));
//        System.out.println("***************");
//        Long mergeStart = System.currentTimeMillis();
//        // 最终合并所有小文件
//        merge();
//
//        Long mergeEnd = System.currentTimeMillis();
//        System.out.println("\n***************");
//        System.out.println(String.format("合并完成:%s 秒。", (mergeEnd - mergeStart) / 1000));
//        System.out.println("\n***************");
//        System.out.println(
//                String.format("排序完成:%s s。 %s 分钟 。", (mergeEnd - divStart) / 1000, (mergeEnd - divStart) / 1000 / 60));
//        System.out.println("***************");
//        System.out.println("最终文件地址：" + divFiles.get(0).getAbsolutePath());
//        // 验证
//        validation();
//        System.exit(0);
    }

    /**
     * 产生测试文件
     */
    public static void generateTestFiles() {
        // 启用十个线程生成测试文件
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        doneSignal = new CountDownLatch(10);
        for (int i = 0; i < genFiles.length; i++) {
            genFiles[i] = ROOT_FILE_PATH + "/originalData" + i + ".txt";
            executorService.submit(new generateFileTask(genFiles[i]));
        }
        try {
            // 等待生成文件
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    /**
     *
     * @author chujiejie
     *
     */
    static class generateFileTask implements Runnable {
        final String filePath;

        public generateFileTask(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            try {
                generateTestFile(filePath);
                // 任务执行完毕递减锁存器
                doneSignal.countDown();
                System.out.print("生成文件：" + doneSignal.getCount() + "，  ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @throws Exception
     */
    public static void generateTestFile(String filePath) throws Exception {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        FileChannel channel = null;
        RandomAccessFile rf = null;
        try {
            File testFile = new File(filePath);
            if (testFile.exists())
                testFile.delete();
            // 如果文件不存在则创建
            testFile.createNewFile();

            rf = new RandomAccessFile(testFile, "rw");
            channel = rf.getChannel();
            int capacity = 1024 * 1024; // 字节
            ByteBuffer writerBuffer = ByteBuffer.allocate(capacity);
            // 一千万
            for (long i = 0; i <= 10000 * 1000; i++) {
                sb.append(random.nextInt(100000)).append("\n");
                // 刷新缓冲
                if ((i + 1) % 10000 == 0) {
                    writerBuffer.put(sb.toString().getBytes());
                    sb.setLength(0);
                    writerBuffer.flip();
                    channel.write(writerBuffer);
                    writerBuffer.clear();
                }
            }
            if (sb.length() > 0) {
                writerBuffer.put(sb.toString().getBytes());
                writerBuffer.flip();
                channel.write(writerBuffer);
                writerBuffer.clear();
            }
        } catch (IOException e) {
            System.out.println("生成测试文件失败！" + e.getMessage());
            throw e;
        } finally {
            try {
                if (rf != null) {
                    rf.close();
                }
            } catch (IOException e) {
                // no-op
            }
            try {
                if (channel.isOpen()) {
                    channel.close();
                }
            } catch (IOException e) {
                // no-op
            }

        }
    }

    /**
     * 分割文件task
     *
     * @author chujiejie
     *
     */
    static class divWorkTask implements Runnable {
        final String filePath;

        public divWorkTask(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            try {
                List<File> lists = divWork(filePath);
                // 将分割完成的文件存入
                divFiles.addAll(lists);
                // 任务执行完毕递减锁存器
                doneSignal.countDown();
                System.out.println("完成分割任务：" + doneSignal.getCount());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并分为两步，第一步将所有小文件合并和成较大的文件 , 第二部将所有文件合并最终结果。
     *
     * @throws IOException
     */
    private static void merge() throws IOException {
        List<File> divfiles2 = new ArrayList<>(divFiles);
        divFiles.clear();
        // 如果不满足条件直接合并
        if (divfiles2.size() < 20) {
            mergeLargeFile(divfiles2, ROOT_FILE_PATH + "/resultFile.txt");
            return;
        }
        List<List<File>> divTwo = new ArrayList();
        // 划分任务，15个为一组
        for (int i = 0; i < divfiles2.size(); i += 15) {
            List<File> files = new ArrayList();
            for (int j = i; j < divfiles2.size() && j < (i + 15); j++) {
                files.add(divfiles2.get(j));
            }
            divTwo.add(files);
        }
        // 定义一个三个线程的阻塞线程池
        BlockThreadPool pool = new BlockThreadPool(3);
        doneSignal = new CountDownLatch(divTwo.size());
        for (int i = 0; i < divTwo.size(); i++) {
            pool.execute(new mergeTask(divTwo.get(i), ROOT_FILE_PATH + "/temp_reslt" + i + ".txt"));
        }
        try {
            // 等待合并文件完成
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        System.out.println("最终合并");
        // 最终合并
        File resultFile = mergeLargeFile(divFiles, ROOT_FILE_PATH + "/temp_reslt_all.txt");
        divFiles.clear();
        divFiles.add(resultFile);
    }

    /**
     * 合并任务
     *
     * @author chujiejie
     *
     */
    static class mergeTask implements Runnable {
        private final List<File> ListFiles;
        private final String fileName;

        public mergeTask(List<File> ListFiles, String fileName) {
            this.fileName = fileName;
            this.ListFiles = ListFiles;
        }

        @Override
        public void run() {
            try {
                File file = mergeLargeFile(ListFiles, fileName);
                divFiles.add(file);
                // 任务执行完毕递减锁存器
                doneSignal.countDown();
                System.out.println("完成合并任务：" + doneSignal.getCount());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 将文件分割并排序
     */
    public static void divisionAndSortFiles(int concurrentNum) {
        // 自定义线程池
        BlockThreadPool blockPool = new BlockThreadPool(concurrentNum);
        // 切分任务开始
        doneSignal = new CountDownLatch(10);
        for (int i = 0; i < 10; i++)
            blockPool.execute(new divWorkTask(genFiles[i]));
        try {
            // 等待切分完成
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 关闭线程池
        blockPool.shutdown();
    }

    /**
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<File> divWork(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists())
            throw new Error("文件不存在");
        // 得到文件大小k
        int mbsize = (int) Math.ceil(file.length() / 1024);
        System.out.println("获取当前文件大小："+mbsize);
        // 计算得到切分的文件数
        int fileNum = (int) Math.ceil(mbsize / SIZE) + 1;
        System.out.println("切分文件数字："+fileNum);
        // 创建零时文件
        List<File> tempFileList = createTempFileList(file, fileNum);
        // 切分文件
        divAndSort(file, tempFileList);
        return tempFileList;

    }

    /**
     * 把临时文件合并到结果文件
     *
     * @param tempFileList
     * @throws IOException
     */
    public static File mergeLargeFile(List<File> tempFileList, String fileName) throws IOException {
        List<FileEntity> bwList = new ArrayList<FileEntity>();
        for (int i = 0; i < tempFileList.size(); i++) {
            FileEntity le = new FileEntity(
                    new BufferedReader(new InputStreamReader(new FileInputStream(tempFileList.get(i)))));
            bwList.add(le);
        }
        BufferedWriter resultBw = null;
        try {
            resultBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
            int count = 0;
            FileEntity fe = null;
            StringBuilder sb = new StringBuilder();
            while ((fe = getFirstFileEntity(bwList)) != null) {
                count++;
                // 写入符合条件的一行数据
                sb.append(fe.getLine()).append("\n");
                // // 准备下一行
                fe.nextLine();
                // 清缓冲流
                if ((count + 1) % (10000) == 0) {
                    resultBw.write(sb.toString());
                    sb.setLength(0);
                    resultBw.flush();
                }
            }
            if (sb.length() > 0) {
                resultBw.write(sb.toString());
                resultBw.flush();
                sb = null;
            }
        } catch (Exception e) {
            // no-op
        } finally {
            if (resultBw != null) {
                try {
                    resultBw.flush();
                    resultBw.close();
                } catch (IOException e) {
                    // no-op
                }
            }
        }
        // 关闭
        for (int i = 0; i < bwList.size(); i++) {
            bwList.get(i).close();
        }
        return new File(fileName);
    }

    /**
     * 从切分的文件中按序行读取（因为切分文件时已经做好了排序）
     *
     * @param bwList
     * @return
     */
    private static FileEntity getFirstFileEntity(List<FileEntity> bwList) {
        if (bwList.size() == 0) {
            return null;
        }
        Iterator<FileEntity> it = bwList.iterator();
        while (it.hasNext()) {
            FileEntity fe = it.next();
            // 如果文件读到完就关闭流和删除在集合的文件流
            if (fe.getLine() == null) {
                fe.close();
                it.remove();
            }
        }
        if (bwList.size() == 0) {
            return null;
        }
        // 排序获取一行数据
        bwList.sort(new FileEntityComparator());
        // 返回第一个符合条件的文件对象
        return bwList.get(0);
    }

    /**
     * 切分文件并做第一次排序
     *
     * @param file
     * @param tempFileList
     */
    private static void divAndSort(File file, List<File> tempFileList) {
        BufferedReader bRead = null;
        try {
            // 读取大文件
            bRead = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            // 行数据保存对象
            String line = null;
            // 临时文件索引
            int index = tempFileList.size() - 1;
            // 保存数据
            List<String> lineList = new ArrayList<>();
            // 统计文件大小
            int byteSum = 0;
            // 循环临时文件并循环大文件
            while ((line = bRead.readLine()) != null) {
                line += "\n";
                byteSum += line.getBytes().length;
                // 如果长度达到每个文件大小则重新计算
                if (byteSum >= BYTE_SIZE) {
                    Long time0 = System.currentTimeMillis();
                    // 写入到文件
                    putLineListToFile(tempFileList.get(index), lineList);
                    Long time1 = System.currentTimeMillis();
                    System.out.println(String.format("写入文件%s：%s ms", index, time1 - time0));
                    index--;
                    byteSum = line.getBytes().length;
                    lineList.clear();
                }
                lineList.add(line);
            }
            if (lineList.size() > 0) {
                // 写入到文件
                putLineListToFile(tempFileList.get(0), lineList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bRead != null) {
                    bRead.close();
                }
            } catch (IOException e) {
                // no-op
            }
        }
    }

    /**
     * 把数据写到临时文件
     *
     * @param lineList
     */
    private static void putLineListToFile(File file, List<String> lineList) throws IOException {
        FileOutputStream tempFileFos = null;
        try {
            // 第一次写入文件时，调用Collection.sort进行内部排序
            lineList.sort(new LineComparator());
            tempFileFos = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lineList.size(); i++) {
                sb.append(lineList.get(i));
                if ((i + 1) % 1000 == 0) {
                    tempFileFos.write(sb.toString().getBytes());
                    sb.setLength(0);
                }
            }
            if (sb.length() > 0) {
                tempFileFos.write(sb.toString().getBytes());
            }
            sb = null;
        } finally {
            if (tempFileFos != null) {
                tempFileFos.close();
            }
        }
    }

    /**
     * AIO异步写文件
     *
     * @param file
     * @param lineList
     * @throws IOException
     */
    private static void putLineListToFileNIO(File file, List<String> lineList) throws IOException {
        Path path = Paths.get(file.getAbsolutePath());
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 512);
        Future<Integer> operation = null;
        try {
            // 第一次写入文件时，调用Collection.sort进行内部排序
            lineList.sort(new LineComparator());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lineList.size(); i++) {
                sb.append(lineList.get(i));
                if ((i + 1) % 1000 == 0) {
                    if (operation != null) {
                        while (!operation.isDone())
                            ;
                    }
                    buffer.put(sb.toString().getBytes());
                    buffer.flip();
                    operation = fileChannel.write(buffer, 0);
                    buffer.clear();
                    sb.setLength(0);
                }
            }
            if (sb.length() > 0) {
                buffer.put(sb.toString().getBytes());
                buffer.flip();
                operation = fileChannel.write(buffer, 0);
                buffer = null;
            }
            sb = null;
        } finally {
            if (operation != null) {
                while (!operation.isDone())
                    ;
            }
            fileChannel.force(true);
            if (fileChannel.isOpen()) {
                fileChannel.close();
            }
        }
    }

    /**
     * 生成零时文件
     *
     * @param file
     * @param fileNum
     * @return List<File>
     */
    private static List<File> createTempFileList(File file, int fileNum) {
        List<File> tempFileList = new ArrayList<File>();
        String fileFolder = file.getParent();
        System.out.println("当前线程拆分的文件"+fileFolder);
        String name = file.getName();
        for (int i = 0; i < fileNum; i++) {
            // 创建临时文件
            File tempFile = new File(fileFolder + "/" + name + ".temp_" + i + ".txt");
            // 如果存在就删除
            if (tempFile.exists()) {
                tempFile.delete();
            }
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempFileList.add(tempFile);
        }
        return tempFileList;
    }

    /**
     * @param o1
     * @param o2
     * @return
     */
    public static int compare(String o1, String o2) {
        o1 = o1.trim();
        o2 = o2.trim();
        // 从小到大
        return Integer.parseInt(o1) - Integer.parseInt(o2);
        // 从大到小
        // return Integer.parseInt(o2) - Integer.parseInt(o1);
    }

    /**
     * 验证
     *
     * @throws IOException
     */
    private static void validation() throws IOException {
        System.out.println("执行验证");
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(divFiles.get(0).getAbsolutePath())));
        String line = "";
        String pre = br.readLine();
        while ((line = br.readLine()) != null) {
            if (Integer.parseInt(line.trim()) < Integer.parseInt(pre.trim())) {
                System.out.println("验证不通过");
                System.exit(0);
            }
        }
        System.out.println("验证通过");
    }
}

/**
 * 排序
 */
class LineComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return LargeFileSort.compare(o1, o2);
    }
}

/**
 * 排序类
 */
class FileEntityComparator implements Comparator<FileEntity> {
    @Override
    public int compare(FileEntity o1, FileEntity o2) {
        return LargeFileSort.compare(o1.getLine(), o2.getLine());
    }
}
