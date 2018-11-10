package rahulmishra.app.newsboard.util;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


public class StringArrayConverter {

    @TypeConverter
    public static String toString(ArrayList<String> array) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            stringBuilder.append(array.get(i));
            if (i < array.size() - 1) stringBuilder.append(",");
        }

        return stringBuilder.toString();
    }

    @TypeConverter
    public static ArrayList<String> toArray(String ids) {
        String[] idSet = ids.split(",");
        Log.e("CookBook", "IDS " + ids);
        ArrayList<String> idArray = new ArrayList<>();
        idArray.addAll(Arrays.asList(idSet));
        return idArray;
    }
}
