package top.R3.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Scene;
import org.springframework.beans.factory.InitializingBean;

/**
 * MemoryView 类代表内存视图
 * 它使用 FXML 文件定义界面布局，并创建一个 JavaFX Scene
 */
@FXMLView(value= "/view/fileSys.fxml",css = "")
public class MemoryView extends AbstractFxmlView implements InitializingBean {
    
    // JavaFX Scene 对象，代表视图的整个内容
    private Scene scene;

    /**
     * 在所有属性设置完成后初始化
     * 这个方法来自 InitializingBean 接口
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建新的 Scene，使用从 FXML 加载的视图
        scene = new Scene(this.getView());
    }

    /**
     * 获取创建的 Scene 对象
     * @return JavaFX Scene 对象
     */
    public Scene getScene() {
        return scene;
    }
}
