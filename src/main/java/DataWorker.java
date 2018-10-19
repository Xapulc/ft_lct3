import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataWorker {
    public static void main(String[] args) {
        List<DataModel> dataList = new ArrayList<>();
        boolean isConExist;

        Random rnd = new Random(System.currentTimeMillis());
        int numHum = 1 + rnd.nextInt(30);

        for (int i = 0; i < numHum; i++) {
            APIWorker apiWork = new APIWorker();
            isConExist = apiWork.createData();
            if (!(isConExist)) {
                dataList.add(new DataModel(true));
                continue;
            }
            dataList.add(apiWork.fromJson());
        }

        ExcelWorker.createExcel(dataList);
    }
}
