package br.net.olimpiodev.naturavon.naturavon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getDataNow() {
        Calendar c = Calendar.getInstance();
        Date data = c.getTime();
        Locale brasilLocale = new Locale("pt", "BR");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", brasilLocale);
        return sdf.format(data);
    }
}
