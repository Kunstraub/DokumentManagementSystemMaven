

import java.io.File;

public class AblageReferenz {
    private String ablageName;
    private String ordner;

    public AblageReferenz(String ablageName, String ordner) {
        this.ablageName = ablageName;
        this.ordner = ordner;
    }

    public String getAblageName() {
        return ablageName;
    }
    public String getOrdner() {
        return ordner;
    }
    public String getPfadUndDatei(){
        return getOrdner()+ File.separator+getAblageName();
    }
}
