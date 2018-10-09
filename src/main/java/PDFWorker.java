import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

class PDFWorker {
    private static void createHeaders(PdfPTable table) {
        BaseFont times = null;
        try {
            times = BaseFont.createFont("c:/windows/fonts/times.ttf","cp1251",BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        table.addCell(new PdfPCell(new Phrase("Имя", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Фамилия", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Отчество", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Возраст", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Пол", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Дата рождения", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("ИНН", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Почтовый индекс", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Страна", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Область", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Город", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Улица", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Дом", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("Квартира", new Font(times,12))));
    }

    private static void createRow(PdfPTable table, DataModel dataModel) {
        BaseFont times = null;
        try {
            times = BaseFont.createFont("c:/windows/fonts/times.ttf","cp1251",BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        int sizeFont = 10;
        Font font = new Font(times,sizeFont);
        table.addCell(new PdfPCell(new Phrase(dataModel.getName(), font)));
        table.addCell(new PdfPCell(new Phrase(dataModel.getSurname(), font)));
        table.addCell(new PdfPCell(new Phrase(dataModel.getPatronymic(), font)));
        table.addCell(new PdfPCell(new Phrase("" + dataModel.getAge(), font)));
        table.addCell(new PdfPCell(new Phrase(
                (dataModel.getSex() == 'M') ? "М" : "Ж", font)));
        table.addCell(new PdfPCell(new Phrase("" + dataModel.getBorn().get(Calendar.DATE) +
                "." + (1 + dataModel.getBorn().get(Calendar.MONTH)) +
                "." + dataModel.getBorn().get(Calendar.YEAR), font)));
        table.addCell(new PdfPCell(new Phrase(dataModel.getITN().toString(), font)));
        table.addCell(new PdfPCell(new Phrase(dataModel.getPostcode().toString(), font)));
        table.addCell(new PdfPCell(new Phrase(dataModel.getCountry(), font)));
        table.addCell(new PdfPCell(new Phrase(dataModel.getRegion(), font)));
        table.addCell(new PdfPCell(new Phrase(dataModel.getCity(), font)));
        table.addCell(new PdfPCell(new Phrase(dataModel.getStreet(), font)));
        table.addCell(new PdfPCell(new Phrase("" + dataModel.getHome(), font)));
        table.addCell(new PdfPCell(new Phrase("" + dataModel.getFlat(), font)));
    }

    static void createPDF(List<DataModel> dataList) {
        Document document = new Document(PageSize.A4.rotate());
        File file = new File("DataPDF.pdf");
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();

        Paragraph title = new Paragraph();
        Chapter chapter = new Chapter(title, 1);
        chapter.setNumberDepth(0);
        Section section = chapter.addSection(title);

        PdfPTable table = new PdfPTable(14);
        createHeaders(table);
        for (DataModel dataModel : dataList)
            createRow(table, dataModel);

        try {
            table.setWidths(DataModel.aligning(dataList));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        section.add(table);

        try {
            document.add(chapter);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();

        System.out.println("Файл создан. Путь: " + file.getAbsolutePath());
    }
}