

public class Dokument {
    private String dateiName;
    private String ordnerName;
    public Dokument(String dateiName, String ordnerName){
        this.dateiName = dateiName;
        this.ordnerName = ordnerName;
    }
    public String getDateiName() {
        return dateiName;
    }

    public String getOrdnerName() {
        return ordnerName;
    }
}
