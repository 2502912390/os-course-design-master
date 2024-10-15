package top.R3.view;


import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Scene;
import org.springframework.beans.factory.InitializingBean;

@FXMLView(value= "/view/fileSys.fxml",css = "")
public class MemoryView extends AbstractFxmlView implements InitializingBean{
    private Scene scene;

    @Override
    public void afterPropertiesSet() throws Exception {
        scene = new Scene(this.getView());
    }

    public Scene getScene() {
        return scene;
    }
}
