package app;
import dao.fileOperator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import task.DBInit;
import task.FileOperateTask;
import task.FileScanCallback;
import task.FileScanTask;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
//实现了Initializable  这个是javaFX的初始化接口默认提供了初始化方法，通过初始化方法可以完成一些操作，
// 相当于主键初始化后就需要执行Initializable的这段代码块
public class Controller implements Initializable {
//注解：代表放在一个java类型上，或者属性，方法上这样的话可以通过反射来获取标签的一些东西
    //FXML   页面元素  方法的绑定
    @FXML
    private GridPane rootPane;
    //对应到fxml中的rootPane，名称和id进行绑定，绑定后就于对应到fxml的主键绑定起来

    @FXML
    private TextField searchField;

    @FXML
    private TableView<FileMeta> fileTable;

    @FXML
    private Label srcDirectory;//设置标签

    //定义一个线程目的是为了不让堵塞
    private Thread t;
    public void initialize(URL location, ResourceBundle resources) {
        // 添加搜索框监听器，内容改变时执行监听事件
        //在初始化后第一个加载
        DBInit.init();
        searchField.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                freshTable();
            }
        });
    }

    /*
    * 文件搜索操作，数据更新到数据库
    *
    * */
    public void choose(Event event) {
        // 选择文件目录
        DirectoryChooser directoryChooser=new DirectoryChooser();
        Window window = rootPane.getScene().getWindow();
        File file = directoryChooser.showDialog(window);
        if(file == null)
            return;
        // 获取选择的目录路径，并显示
        String path = file.getPath();
        srcDirectory.setText(path);

        // TODO
        //线程启动
//      /*
// 我们需要的是如果现在有线程正在加载，我们就让其停止，去执行其他操作，如果没有线程我们实例化一个
//
// */
        if (t!=null) {
            t.interrupt();
        }
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //启动一个线程   我们的作用就是扫描文件，获取到文件信息
                    //所以这个里面主要用到FileScanTask中的东西


                    /*接口回调这样也可以实现，但是为了更清楚我们封装到一个类中
                    FileScanCallback fileScanCallback=new FileScanCallback() {
                        @Override
                        public void execute(File dir) {

                        }
                    };
                    FileScanTask task = new FileScanTask(fileScanCallback);
                    */
                    try {
                        FileScanCallback fileScanCallback=new FileOperateTask();
                        FileScanTask task = new FileScanTask(fileScanCallback);

                        task.startScan(file);
                        task.waitFinish();
                        System.out.println("执行完，src=" + srcDirectory.getText());
                        freshTable();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }


    // 刷新表格数据
    private void freshTable(){
       ObservableList<FileMeta> metas = fileTable.getItems();
        metas.clear();
        // TODO
        //我们将属性插入到表中，先将他放入在数组中，然后添加到metas中
        List<FileMeta>dates=fileOperator.search(srcDirectory.getText(),searchField.getText());
        metas.addAll(dates);
    }
}