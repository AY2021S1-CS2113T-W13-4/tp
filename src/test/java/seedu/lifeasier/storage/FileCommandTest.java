package seedu.lifeasier.storage;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileCommandTest {

    public static final String TEST_FILEPATH = "testCommandsSave.txt";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
    private FileCommand fileCommand;

    public FileCommandTest() {
        this.fileCommand = new FileCommand();
    }

    @Test
    void convertToLocalDateTime_inputValidDateTime_correctOutput() {
        String formattedDateTime =
                fileCommand.convertToLocalDateTime("09-04-21T18:00").format(DATE_TIME_FORMATTER);
        assertEquals("09-04-21 18:00", formattedDateTime);
    }

    @Test
    void convertToLocalDateTIme_invalidDateTime_defaultDateTime() {
        String formattedDateTime =
                fileCommand.convertToLocalDateTime("09-23-21T18:00").format(DATE_TIME_FORMATTER);
        assertEquals("31-12-99 00:00", formattedDateTime);
    }

    @Test
    void convertToBoolean_validString_convertedToTrue() {
        assertEquals(true, fileCommand.convertToBoolean("True"));
    }

    @Test
    void convertToBoolean_invalidString_convertedToTrue() {
        assertEquals(false, fileCommand.convertToBoolean("True  "));
    }

    @Test
    void clearSaveFile_testFilePath_emptyFile() {
        try {
            FileWriter testFileWriter = new FileWriter(TEST_FILEPATH, true);
            testFileWriter.write("This is a test" + System.lineSeparator());
            testFileWriter.write("All this should not exist after file clearing" + System.lineSeparator());
            testFileWriter.close();
            fileCommand.clearSaveFile(TEST_FILEPATH);

            File testFile = new File(TEST_FILEPATH);
            Scanner fileScanner = new Scanner(testFile);
            String documentInformation = null;

            if (fileScanner.hasNext()) {
                documentInformation = fileScanner.nextLine();
            }

            testFile.deleteOnExit();
            assertNull(documentInformation);

        } catch (IOException e) {
            System.out.println("Failed creating test save file");
        }
    }

}