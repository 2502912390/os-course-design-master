package top.R3.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Scene;
import org.springframework.beans.factory.InitializingBean;

// 使用FXMLView注解指定FXML文件的位置
@FXMLView(value="/view/createProcess.fxml",css = "")
public class CreateProcessView extends AbstractFxmlView implements InitializingBean {
    // 存储JavaFX场景
    private Scene scene;

    // 实现InitializingBean接口的方法,在bean属性设置完成后调用
    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建新的Scene对象,使用从AbstractFxmlView继承的getView()方法获取根节点
        scene = new Scene(this.getView());
    }

    // 获取Scene对象的方法
    public Scene getScene() {
        return scene;
    }
}