package task;
import app.FileMeta;
import dao.fileOperator;
import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class FileOperateTask implements FileScanCallback  {
    @Override
    public void execute(File dir) {
        //文件比对
        if (dir.isDirectory()){
            //本地文件
        File[]children=dir.listFiles();
        //包装为本地的自定义文件类集合
        List<FileMeta> localMetas=compose(children);
        //数据库文件，jdbc查询实现
        //（根据路径查询路径下的所有文件/文件夹）
        List<FileMeta>metas=fileOperator.query(dir.getPath());
        //本地有，数据库没有的文件
            //数据库有，本地没有的文件
            for (FileMeta meta:metas) {
                if (!localMetas.contains(meta)){
                    //如果meta是文件夹，我们还需要将子文件夹全部删除
                    fileOperator.delete(meta);
                }
            }
            for (FileMeta localMeta:localMetas) {
                if (!metas.contains(localMeta)){//包含contains我们需要实现Comparable的接口，重写hashCode（）和equals方法。
                    fileOperator.insert(localMeta);
                }
            }
        }
    }

    private List<FileMeta> compose(File[] children) {
        List<FileMeta> metas=new ArrayList<>();
        if(children!=null){
            for (File child:children) {
                /*metas.add(new FileMeta(child.getName(), child.getParent(), child.length(),
                        child.lastModified(), child.isDirectory()));*/
              metas.add(new FileMeta(child));
            }
        }
        return metas;
    }
}
