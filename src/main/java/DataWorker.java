import java.util.List;

public class DataWorker {
    public static void main(String[] args) {
        List<DataModel> dataList = DataModel.fillData();
        ExcelWorker.createExcel(dataList);
        PDFWorker.createPDF(dataList);
    }
}
