package com.shen.big_file;


import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @Description: File包装类，用于按行读取
 * @author: chujiejie
 * @date: 2018-7-19 下午5:24:54
 *
 */
public class FileEntity {

    private int id = 0;
    private String line = "";
    private BufferedReader br;

    /**
     * 传入文件 BufferedReader，包装成FileEntity
     *
     * @param br
     * @throws IOException
     */
    public FileEntity(BufferedReader br) throws IOException {
        this.br = br;
        // 初始化读取第一行
        setLineId();
    }

    /**
     * 使用来排序的数据
     *
     * @throws IOException
     */
    private void setLineId() throws IOException {
        line = br.readLine();
        if (line != null) {
            try {
                id = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                id = 0;
            }
        }
    }

    /**
     * 关闭流
     */
    public void close() {
        if (this.br != null) {
            try {
                this.br.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取下一行
     *
     * @return FileEntity
     */
    public FileEntity nextLine() {
        try {
            setLineId();
        } catch (IOException e) {
        }
        return this;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return String
     */
    public String getLine() {
        return line;
    }
}
