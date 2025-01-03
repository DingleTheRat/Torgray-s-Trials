package object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Gate extends SuperObject{
    public OBJ_Gate() {
        name = "Gate";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/gate.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
