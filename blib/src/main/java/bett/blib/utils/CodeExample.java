package bett.blib.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by bett-pc on 4/8/2016.
 */
public class CodeExample {
    public static enum TYPE {
        PUBLIC("public"),
        PRIVATE("private"),
        MY("my");

        private final String text;

        private TYPE(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
