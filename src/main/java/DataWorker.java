import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataWorker {
    public static void main(String[] args) {
        List<DataModel> dataList = new ArrayList<>();

        Random rnd = new Random(System.currentTimeMillis());
        int numHum = 1 + rnd.nextInt(30);

        DataBaseWorker dataBase = new DataBaseWorker();
        boolean isDataBaseConnect = dataBase.createConnection();

        for (int i = 0; i < numHum; i++) {
            APIWorker apiWork = new APIWorker();
            if (!(apiWork.createData())) {
                if (!(isDataBaseConnect) || dataBase.isEmpty()) {
                    dataList.add(new DataModel(true));
                    continue;
                }

                dataList.add(dataBase.getRandRow());
                continue;
            }

            dataList.add(apiWork.fromJson());

            if (isDataBaseConnect)
                dataBase.add(dataList.get(i));
        }

        if (isDataBaseConnect) {
            try {
                dataBase.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        ExcelWorker.createExcel(dataList);
    }
}
