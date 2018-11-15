package com.shen.big_file;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * 基于大数据量的外排序算法，分为二路归并和多路归并
 * @author java2king
 * @link http://blog.csdn.net/Java2King
 *
 */
public class ExternalSort {

    public static int ITEM_COUNT = 10000000; //总数

    public static int BUFFER_SIZE = 1024*4*1000;// 一次缓冲读取

    public static int FILE_COUNT = 1024*1000*1*4;// 每个文件的记录数

    public static File MAIN_FILE = new File("/Users/mac/desktop/file/33.txt");//要排序的文件

    /**
     * 二路归并
     * @param file
     * @return
     * @throws IOException
     */
    public File sort(File file) throws IOException {
        ArrayList<File> files = split(file);
        return process(files);
    }
    /**
     * 多路归并
     * @param file
     * @throws IOException
     */
    public void mSort(File file) throws IOException{
        ArrayList<File> files = split(file);
        multipleMerge(files);

    }

    // recursive method to merge the lists until we are left with a
    // single merged list
    private File process(ArrayList<File> list) throws IOException {
        if (list.size() == 1) {
            return list.get(0);
        }
        ArrayList<File> inter = new ArrayList<File>();
        for (Iterator<File> itr = list.iterator(); itr.hasNext();) {
            File one = itr.next();
            if (itr.hasNext()) {
                File two = itr.next();
                inter.add(merge(one, two));
            } else {
                //  return one;
                inter.add(one);
            }
        }
        return process(inter);
    }
    /**
     * Splits the original file into a number of sub files.
     */
    private ArrayList<File> split(File file) throws IOException {
        ArrayList<File> files = new ArrayList<File>();
        int[] buffer = new int[FILE_COUNT];
        FileInputStream fr = new FileInputStream(file);
        BufferedInputStream bin = new BufferedInputStream(fr,BUFFER_SIZE);
        DataInputStream din=new DataInputStream(bin);
        boolean fileComplete = false;

        while (!fileComplete) {
            int index = buffer.length;
            for (int i = 0; i < buffer.length && !fileComplete; i++) {
                try {
                    buffer[i] = din.readInt();
                } catch (Exception e) {
                    fileComplete = true;
                    index = i;
                }
            }
            if (index != 0 && buffer[0] > -1) {
                Arrays.sort(buffer, 0, index);
                File f = new File("set" + new Random().nextInt());
                //       File temp = File.createTempFile("josp", ".tmp", f);
                FileOutputStream writer = new FileOutputStream(f);
                BufferedOutputStream bOutputStream = new BufferedOutputStream(writer);

                DataOutputStream dout=new DataOutputStream(bOutputStream);
                for (int j = 0; j < index; j++) {
                    dout.writeInt(buffer[j]);
                }
                dout.close();
                bOutputStream.close();
                writer.close();
                files.add(f);

            }

        }
        din.close();
        bin.close();
        fr.close();
        return files;
    }
    /**
     * 多路归并
     * @param list
     * @throws IOException
     */
    private void multipleMerge(ArrayList<File> list) throws IOException {

        int fileSize = list.size();

        if(fileSize == 1){
            return;
        }

        ArrayList<DataInputStream> dinlist = new ArrayList<DataInputStream>();

        int[] ext = new int[fileSize];//比较数组

        //    File output = new File("multipleMerged");
        FileOutputStream os = new FileOutputStream(MAIN_FILE);
        BufferedOutputStream bout = new BufferedOutputStream(os);
        DataOutputStream dout = new DataOutputStream(bout);

        for (int i = 0; i < fileSize; i++) {
            try {
                dinlist.add(i, new DataInputStream(new BufferedInputStream(
                        new FileInputStream(list.get(i)), BUFFER_SIZE)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int index = 0;

        for (int i = 0; i < fileSize; i++) {
            try {
                ext[i] = dinlist.get(i).readInt();
            } catch (Exception e) {
                System.err.println("file_" + i + "为空");
                ext[i] = -1;
            }
        }
        int count = fileSize;
        int[] sum = new int[fileSize];

        while (count > 1) {

            index = getMinIndex(ext);
            dout.writeInt(ext[index]);
            sum[index]++;
            try {
                ext[index] = dinlist.get(index).readInt();
            } catch (Exception e) {
                ext[index] = -1;
                count--;
                dinlist.get(index).close();
                //        System.err.println(index + "空,写进:" +sum[index]);

            }
        }
        int sIndex = getSIndex(ext);
        dout.writeInt(ext[sIndex]);
        while (true) {
            try {
                dout.writeInt(dinlist.get(sIndex).readInt());
            } catch (Exception e) {
                dinlist.get(sIndex).close();
                break;
            }
        }
        dout.close();
    }
    //找到剩下的最后一个文件输入流
    public int getSIndex(int[] ext){
        int result = 0;
        for (int i = 0; i < ext.length; i++) {
            if(ext[i]!= -1){
                result = i;
                break;
            }
        }
        return result;
    }
    //找到数据中最小的一个
    public int getMinIndex(int[] ext){
        int min = 2147483647;
        int index = -1;
        for (int i = 0; i < ext.length; i++) {
            if(ext[i] != -1 && ext[i] < min){
                min = ext[i];
                index = i;
            }
        }
        return index;
    }
    /**
     * 二路归并
     *
     * @param one
     * @param two
     * @return
     * @throws IOException
     */
    private File merge(File one, File two) throws IOException {
        FileInputStream fis1 = new FileInputStream(one);
        FileInputStream fis2 = new FileInputStream(two);
        BufferedInputStream bin1 = new BufferedInputStream(fis1,BUFFER_SIZE);
        BufferedInputStream bin2 = new BufferedInputStream(fis2,BUFFER_SIZE);

        DataInputStream din1=new DataInputStream(bin1);
        DataInputStream din2=new DataInputStream(bin2);

        File output = new File("merged" + new Random().nextInt());
        FileOutputStream os = new FileOutputStream(output);
        BufferedOutputStream bout = new BufferedOutputStream(os);
        DataOutputStream dout=new DataOutputStream(bout);

        int a = -1;//= din1.readInt();
        int b = -1;//= din2.readInt();

        boolean finished = false;
        boolean emptyA = false;//
        int flag = 0;
        while (!finished) {

            if (flag != 1) {
                try {
                    a = din1.readInt();
                } catch (Exception e) {
                    emptyA = true;
                    break;
                }
            }
            if (flag != 2) {
                try {
                    b = din2.readInt();
                } catch (Exception e) {
                    emptyA = false;
                    break;
                }
            }
            if(a > b){
                dout.writeInt(b);
                flag = 1;
            }else if( a < b){
                dout.writeInt(a);
                flag = 2;
            }else if(a == b){
                dout.write(a);
                dout.write(b);
                flag = 0;
            }
        }
        finished = false;
        if(emptyA){
            dout.writeInt(b);
            while(!finished){
                try {
                    b = din2.readInt();
                } catch (Exception e) {
                    break;
                }
                dout.writeInt(b);
            }
        }else{
            dout.writeInt(a);
            while(!finished){
                try {
                    a = din1.readInt();
                } catch (Exception e) {
                    break;
                }
                dout.writeInt(a);
            }
        }
        dout.close();
        os.close();
        bin1.close();
        bin2.close();
        bout.close();
        return output;
    }





    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Random random = new Random(10000);
        FileOutputStream fw = new FileOutputStream(MAIN_FILE);
        BufferedOutputStream bout = new BufferedOutputStream(fw);
        DataOutputStream dout=new DataOutputStream(bout);

        for (int i = 0; i < ITEM_COUNT; i++) {
            int ger = random.nextInt();
            ger = ger < 0 ? -ger : ger;
            dout.writeInt(ger);

        }
        dout.close();
        bout.close();
        fw.close();
        ExternalSort sort = new ExternalSort();
        System.out.println("Original:");

        long start = System.currentTimeMillis();
        sort.mSort(MAIN_FILE);


        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000 + "s");
        recordFile((end - start)/1000 ,true);
    }

    private static void recordFile(long time,boolean isBuffer)
            throws FileNotFoundException, IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("log",true));
        bw.write("FILE_COUNT = "+FILE_COUNT+";对"+ ITEM_COUNT + "条数据 "+ ITEM_COUNT*4/(1024*1204) +"MB排序耗时:" + time + "s ");
        if(isBuffer){
            bw.write("  使用缓冲:"+BUFFER_SIZE*4/(1024*1204) +"MB");
        }
        bw.newLine();
        bw.close();
    }

}
