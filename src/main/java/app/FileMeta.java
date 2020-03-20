package app;

/*
设计表   那表中的每一列  就是我们现在所设定的属性
*/


import util.Util;

import java.io.File;
import java.util.Objects;

public class FileMeta {
    //属性我们不止要对比我们显视fxml中的，还需要查看我们sql中的需要的属性
   private  String  name;
   private  String  path;
   private Long size;
   private Long last_modified;//timestamp
   private boolean is_Directory;
   private  String sizeText;
   private String lastModifiedText;

   //为什么在构造方法中  使用的是普通类  不是封装   因为我们使用普通的话  会默认带一个值，这样就可以避免到再次判断
    //否则比如boolean   默认为false 不需要判断是否为null
    public FileMeta(String name, String path, long size, long last_modified, boolean is_Directory) {
        this.name = name;
        this.path = path;
        this.size = size;//大小的一个转化
        this.last_modified = last_modified;//时间的一个转化
        this.is_Directory = is_Directory;
        this.sizeText=Util.parseSize(size);//转化size
        this.lastModifiedText=Util.parseDate(last_modified);//转化last_modified

    }

    public FileMeta(File child) {
        this(child.getName(), child.getParent(), child.length(),
                child.lastModified(), child.isDirectory());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMeta meta = (FileMeta) o;
        return is_Directory == meta.is_Directory &&
                Objects.equals(name, meta.name) &&
                Objects.equals(path, meta.path);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, path, is_Directory);
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", last_modified=" + last_modified +
                ", is_Directory=" + is_Directory +
                ", sizeText='" + sizeText + '\'' +
                ", lastModifiedText='" + lastModifiedText + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(Long last_modified) {
        this.last_modified = last_modified;
    }

    public boolean isIs_Directory() {
        return is_Directory;
    }

    public void setIs_Directory(boolean is_Directory) {
        this.is_Directory = is_Directory;
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public String getLastModifiedText() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText = lastModifiedText;
    }
}
