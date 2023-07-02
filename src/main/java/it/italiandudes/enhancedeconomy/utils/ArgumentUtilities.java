package it.italiandudes.enhancedeconomy.utils;

import java.util.ArrayList;

@SuppressWarnings("unused")
public final class ArgumentUtilities {

    public static String[] reparseArgs(String[] args) {

        if (!argsValidity(args)) return null;

        ArrayList<String> result = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        boolean insideQuotes = false;

        for (String s : args) {
            if (s.startsWith("\"")) {
                insideQuotes = true;
                builder.append(s.substring(1));
            } else if (s.endsWith("\"")) {
                insideQuotes = false;
                builder.append(" ").append(s, 0, s.length() - 1);
                result.add(builder.toString());
                builder.setLength(0);
            } else if (insideQuotes) {
                builder.append(" ").append(s);
            } else {
                result.add(s);
            }
        }

        return result.toArray(new String[0]);
    }

    private static boolean argsValidity(String[] args) {
        int count = 0;
        for (String arg : args) {
            for (int i=0;i<arg.length();i++) {
                if (arg.charAt(i) == '\"') count++;
            }
        }
        return count%2 == 0;
    }

}
