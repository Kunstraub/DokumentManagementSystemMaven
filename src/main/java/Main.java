

import DateiService.DateienLeseService;
import DateiService.DateienManipulationsService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Main {
    private static final String FILE_SEP = File.separator;
    private static final String INPUT_PATH = String.format("src%smain%sjava%sinput", FILE_SEP, FILE_SEP, FILE_SEP);
    public static final String IMPORT_PATH = String.format("src%smain%sjava%simport", FILE_SEP, FILE_SEP, FILE_SEP, FILE_SEP);
    public static void main(String[] args) throws IOException {
        final Logger logger = Logger.getLogger(OrdnerSystem.class.getName());
        OrdnerSystem ordnerSystem = new OrdnerSystem(logger);
        DateienErstellung dateienErstellung = new DateienErstellung();
        dateienErstellung.start();
        File inputOrdner = new File(INPUT_PATH);
        File importOrdner = new File(IMPORT_PATH);
        ordnerSystem.verschiebeDateienVonInputZuImport(inputOrdner,importOrdner);
        ordnerSystem.manipuliereDateienVonImport(importOrdner);



    }



}

