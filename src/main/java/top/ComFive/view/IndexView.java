package top.ComFive.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;

/**
 * 这是应用程序的主视图类
 * 它使用了@FXMLView注解，指定了对应的FXML文件路径
 */
@FXMLView(value = "/view/index.fxml", css = "")
public class IndexView extends AbstractFxmlView {
    // 这个类没有具体的方法实现
    // 它主要用于将FXML文件与Java类关联
    // AbstractFxmlView 类会处理FXML的加载和控制器的实例化
}
