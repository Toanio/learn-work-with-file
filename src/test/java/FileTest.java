import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTest {

    ClassLoader cl = FileTest.class.getClassLoader();
    @Test
    void openZipFile() throws Exception {
        var zipFile = new ZipFile("src/test/resources/files/learnqaguru.zip");
        ZipInputStream is = new ZipInputStream(Objects.requireNonNull(cl.getResourceAsStream("files/learnqaguru.zip")));
        ZipEntry entry;

        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().equals("files/examplepdf.pdf")) {
                try (InputStream stream = zipFile.getInputStream(entry)) {
                    assert stream != null;
                    PDF pdf = new PDF(stream);
                    Assertions.assertEquals(1, pdf.numberOfPages);
                    assertThat(pdf.text).contains("Oleg");
                }
            }
            if (entry.getName().equals("files/examplecsv.csv")) {
                try (InputStream stream = zipFile.getInputStream(entry)) {
                    assert stream != null;
                    try (CSVReader reader = new CSVReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

                        List<String[]> content = reader.readAll();
                        org.assertj.core.api.Assertions.assertThat(content).contains(
                                new String[]{"Maria", "350"},
                                new String[]{"Mikl", "120"},
                                new String[]{"Alena", "5050"}
                        );
                    }
                }
            }
            if (entry.getName().equals("files/examplexlsx.xlsx")) {
                try (InputStream stream = zipFile.getInputStream(entry)) {
                    assert stream != null;
                    XLS xls = new XLS(stream);
                    String stringCellValue = xls.excel.getSheetName(0);
                    System.out.println(stringCellValue);
                    org.assertj.core.api.Assertions.assertThat(stringCellValue).contains("Olexandr");
                }
            }
        }

        }
    }


