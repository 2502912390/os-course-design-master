package top.R3;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import top.R3.splash.MainSplash;
import top.R3.view.IndexView;

/**
 * 这是应用程序的主类，它继承自AbstractJavaFxApplicationSupport，
 * 这个类是一个用于集成Spring Boot和JavaFX的支持类。
 */
@SpringBootApplication(scanBasePackages = "top.R3")
public class Application extends AbstractJavaFxApplicationSupport {

    /**
     * 应用程序的入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动JavaFX应用程序
        // Application.class: 当前类，作为主应用程序类
        // IndexView.class: 主视图类
        // new MainSplash(): 创建一个新的启动画面
        // args: 命令行参数
        Application.launch(Application.class, IndexView.class, new MainSplash(), args);
    }

    /**
     * 在显示初始视图之前配置主舞台（主窗口）
     * @param stage JavaFX的主舞台
     * @param ctx Spring的应用程序上下文
     */
    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        stage.setTitle("菜单"); // 设置窗口标题
        stage.setWidth(1500);   // 设置窗口宽度
        stage.setHeight(900);   // 设置窗口高度
    }
}
