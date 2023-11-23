import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DateienErstellung {
    private static final String FILE_SEP = File.separator;
    private final Logger logger = Logger.getLogger(DateienErstellung.class.getName());
    Path PROJEKTBERICHT = Paths.get(String.format("src%sexterneQuelle%sProjektbericht.pdf"
            ,FILE_SEP,FILE_SEP));
    Path FALSCHER_NAME_DATEI = Paths.get(String.format("src%sexterneQuelle%sTestDoküment.txt"
            ,FILE_SEP,FILE_SEP));
    Path ZIEL_ORDNER = Paths.get(String.format("src%smain%sjava%sinput",FILE_SEP,FILE_SEP,FILE_SEP));

    public void start(){
        List<Path> pathList = new ArrayList<>();
        pathList.add(PROJEKTBERICHT);
        pathList.add(FALSCHER_NAME_DATEI);
        kopiereDateien(pathList);
        erstelleNeueDatei(ZIEL_ORDNER.resolve("EineDatei.txt"));
    }
    private void kopiereDateien(List<Path> pathList) {
        for (Path path : pathList){
            try{
                Files.copy(path,Paths.get(ZIEL_ORDNER+FILE_SEP+path.getFileName()),StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e){
                logger.severe("Leider ist beim kopieren der Datei: "+path.getFileName()+" etwas " +
                        "schief gelaufen und es konnte nicht nach: "+ZIEL_ORDNER.getFileName()+" verschoben werden" +
                        " Exception ist: "+e.getMessage());
            }
        }
    }

    private void erstelleNeueDatei(Path zielOrdner){
        String dateiInhalt = "Dies ist der Inhalt der neuen Datei.";
        try {
            Files.write(zielOrdner, dateiInhalt.getBytes(), StandardOpenOption.CREATE);
        }catch (IOException e){
            logger.severe("Beim erstellen einer Datei in den ZielOrdner: "+zielOrdner.getFileName()+" ist " +
                    "leider ein Problem aufgetreten. Exception: "+e.getMessage());
        }

    }
}
