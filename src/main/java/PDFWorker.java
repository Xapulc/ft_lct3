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

        table.addCell(new PdfPCell(new Phrase(dataModel.getName(), new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase(dataModel.getSurname(), new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase(dataModel.getPatronymic(), new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("" + dataModel.getAge())));
        table.addCell(new PdfPCell(new Phrase(
                (dataModel.getSex() == 'M') ? "М" : "Ж", new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("" + dataModel.getBorn().get(Calendar.DATE) +
                "." + 1+dataModel.getBorn().get(Calendar.MONTH) +
                "." + dataModel.getBorn().get(Calendar.YEAR))));
        table.addCell(new PdfPCell(new Phrase(dataModel.getITN().toString())));
        table.addCell(new PdfPCell(new Phrase(dataModel.getPostcode().toString())));
        table.addCell(new PdfPCell(new Phrase(dataModel.getCountry(), new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase(dataModel.getRegion(), new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase(dataModel.getCity(), new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase(dataModel.getStreet(), new Font(times,12))));
        table.addCell(new PdfPCell(new Phrase("" + dataModel.getHome())));
        table.addCell(new PdfPCell(new Phrase("" + dataModel.getFlat())));
    }

    static void createPDF(List<DataModel> dataList) {
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(document, new FileOutputStream("DataPDF.pdf"));
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

        section.add(table);

        try {
            document.add(chapter);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();

        System.out.println("PDF file is created!");
    }
}