import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

class ExcelWorker {
    private static void createHeaders(Row row) {
        int rowCur = -1;

        row.createCell(++rowCur).setCellValue("Имя");
        row.createCell(++rowCur).setCellValue("Фамилия");
        row.createCell(++rowCur).setCellValue("Отчество");
        row.createCell(++rowCur).setCellValue("Возраст");
        row.createCell(++rowCur).setCellValue("Пол");
        row.createCell(++rowCur).setCellValue("Дата рождения");
        row.createCell(++rowCur).setCellValue("ИНН");
        row.createCell(++rowCur).setCellValue("Почтовый индекс");
        row.createCell(++rowCur).setCellValue("Страна");
        row.createCell(++rowCur).setCellValue("Область");
        row.createCell(++rowCur).setCellValue("Город");
        row.createCell(++rowCur).setCellValue("Улица");
        row.createCell(++rowCur).setCellValue("Дом");
        row.createCell(++rowCur).setCellValue("Квартира");
    }

    private static void createRow(HSSFSheet sheet, int rowNum, DataModel dataModel) {
        Row row = sheet.createRow(rowNum);
        int columnCur = -1;

        row.createCell(++columnCur).setCellValue(dataModel.getName());
        row.createCell(++columnCur).setCellValue(dataModel.getSurname());
        row.createCell(++columnCur).setCellValue(dataModel.getPatronymic());
        row.createCell(++columnCur).setCellValue(dataModel.getAge());
        row.createCell(++columnCur).setCellValue(
                (dataModel.getSex() == 'M') ? "М" : "Ж");
        row.createCell(++columnCur).setCellValue("" + dataModel.getBorn().get(Calendar.DATE) +
                "." + (1 + dataModel.getBorn().get(Calendar.MONTH)) +
                "." + dataModel.getBorn().get(Calendar.YEAR));
        row.createCell(++columnCur).setCellValue(dataModel.getITN().toString());
        row.createCell(++columnCur).setCellValue(dataModel.getPostcode().toString());
        row.createCell(++columnCur).setCellValue(dataModel.getCountry());
        row.createCell(++columnCur).setCellValue(dataModel.getRegion());
        row.createCell(++columnCur).setCellValue(dataModel.getCity());
        row.createCell(++columnCur).setCellValue(dataModel.getStreet());
        row.createCell(++columnCur).setCellValue(dataModel.getHome());
        row.createCell(++columnCur).setCellValue(dataModel.getFlat());

    }

    private static void aligning (HSSFSheet sheet, List<DataModel> dataList) {
        int[] maxSizeColumn = DataModel.aligning(dataList);
        int columnNum = sheet.getRow(0).getLastCellNum();
        for (int i = 0; i < columnNum; i++) {
            sheet.setColumnWidth(i, 150 + 300 * maxSizeColumn[i]);
        }
    }

    static void createExcel(List<DataModel> dataList) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("База данных");
        int rowNum = 0;

        Row row = sheet.createRow(0);
        createHeaders(row);

        for (DataModel dataModel : dataList)
            createRow(sheet, ++rowNum, dataModel);

        aligning(sheet, dataList);
        File file = new File("DataExcel.xls");
        try (FileOutputStream out = new FileOutputStream(file)) {
            workbook.write(out);
            System.out.println("Файл создан. Путь: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
