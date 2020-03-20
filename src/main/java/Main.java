import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    /*main方法调用内置的launch的一个框架，框架里面实现了一个start方法
    * 1.加载app.fxml文件
    * 2.通过反射，初始化出来文件中的对象，显示在运行界面中
    * 3.“choose”对象在页面中绑定了onMouseClicked的鼠标点击事件，
    * 相当于鼠标一点击调用到Controller中的对应的choose方法
    * fxml中的4个信息：
    * Button对应的是选择目录的按钮
    * Label 相对应显示的我们选择好路径后所对应的标签页
    * TextField 搜索框
    * TableColumn 表格 对应的name，path，sizeText，lastModifiedText
    *
    * */

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(
                getClass().getClassLoader()
                        .getResource("app.fxml"));
        primaryStage.setTitle("文件搜索");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}