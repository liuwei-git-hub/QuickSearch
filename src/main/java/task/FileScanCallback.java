package task;
/*
* 文件扫描任务的回调方法，主要目的是在数据库中插入数据
*
* */
import java.io.File;

public interface FileScanCallback {
    void execute(File dir);
}
