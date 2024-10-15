package top.R3.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;


/**
 * 通过使用 @FXMLView 注解，你告诉 Spring 在运行时如何处理这个类
 * 包括加载与之关联的 FXML 文件以及执行与 JavaFX 视图相关的其他操作。
 * resources文件夹下的同名文件
 * 这种注解方式通常用于在 Spring Boot 中配置 JavaFX 应用程序。**/
@FXMLView(value = "/view/index.fxml",css = "")  //这个参数指定了与这个视图关联的 FXML 文件的路径。
public class IndexView extends AbstractFxmlView {
}
