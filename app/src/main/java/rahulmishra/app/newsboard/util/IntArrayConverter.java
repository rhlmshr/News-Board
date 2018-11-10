package rahulmishra.app.newsboard.util;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;

public class IntArrayConverter {

    @TypeConverter
    public static String toString(ArrayList<Integer> array) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            stringBuilder.append(array.get(i));
            if (i < array.size() - 1) stringBuilder.append(",");
        }

        return stringBuilder.toString();
    }

    @TypeConverter
    public static ArrayList<Integer> toArray(String ids) {
        String[] idSet = ids.split(",");
        ArrayList<Integer> idArray = new ArrayList<>();
        for (String anIdSet : idSet) {
            try {
                idArray.add(Integer.parseInt(anIdSet));
            } catch (Exception ignored) {
            }
        }
        return idArray;
    }
}
