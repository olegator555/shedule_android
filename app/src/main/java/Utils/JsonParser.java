package Utils;

import Model.ServerAnswerModel;
import android.content.Context;
import android.util.Log;
import com.olegator555.rasp.DB.DBManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser  {
    private final JSONArray topLevelJsonArray;
    private DBManager dbManager;

    public JsonParser(JSONArray topLevelJsonArray, Context context) {
        this.topLevelJsonArray = topLevelJsonArray;
        dbManager = new DBManager(context);
    }
    public void parse_json_into_db(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                write_to_db();
            }
        });
    }
    private void write_to_db() {
        dbManager.openDB();
        for(int i = 0; i< topLevelJsonArray.length(); i++){
            try {
                String country = topLevelJsonArray.getJSONObject(i).getString("title");
                JSONObject regionsLevelJsonObject = topLevelJsonArray.getJSONObject(i);
                JSONArray regionsLevelJsonArray = regionsLevelJsonObject.getJSONArray("regions");
                for(int j = 0; j<regionsLevelJsonArray.length();j++){
                    String region = regionsLevelJsonArray.getJSONObject(j).getString("title");
                    JSONObject settlementsLevelJsonObject = regionsLevelJsonArray.getJSONObject(j);
                    JSONArray settlementsLevelJsonArray = settlementsLevelJsonObject.getJSONArray("settlements");
                    for(int k=0; k<settlementsLevelJsonArray.length(); k++) {
                        String settlement = settlementsLevelJsonArray.getJSONObject(k).getString("title");
                        JSONObject stationsLevelJsonObject = settlementsLevelJsonArray.getJSONObject(k);
                        JSONArray stationsLevelJsonArray = stationsLevelJsonObject.getJSONArray("stations");
                        for(int z=0; z<stationsLevelJsonArray.length(); z++) {
                            String station_name = stationsLevelJsonArray.getJSONObject(z).getString("title");
                            String direction = stationsLevelJsonArray.getJSONObject(z).getString("direction");
                            // FIXME: 31/01/2022
                            JSONObject codeLevelJsonObject = stationsLevelJsonArray.getJSONObject(z);
                            JSONObject yandex_code_jbject = codeLevelJsonObject.getJSONObject("codes");
                            String yandex_code = yandex_code_jbject.getString("yandex_code");
                            Log.d("tag", yandex_code);
                            ServerAnswerModel serverAnswerModel = new ServerAnswerModel(country,region,settlement,
                                        direction,station_name,yandex_code);

                                dbManager.insert(serverAnswerModel);
                                
                        }


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        dbManager.closeDB();

    }

}
