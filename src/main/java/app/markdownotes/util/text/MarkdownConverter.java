package app.markdownotes.util.text;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

public class MarkdownConverter {

    public static String convertToMD (Stream<String> lines) throws IOException {
        Function<String, String> formatMarkdownLine = line -> {
            String trimmedLine = line.trim();
            if (trimmedLine.isBlank()) return "";
            if (trimmedLine.endsWith(":")) return "## " + line.substring(0, line.length() - 1) + "  ";
            if (trimmedLine.startsWith("-")) return line.replaceFirst("-", "* ") + "  ";
            return trimmedLine + "  ";
        };
        
        return lines
                .map(formatMarkdownLine)
                .reduce((left, right) -> left + System.lineSeparator() + right)
                .orElse("");
    }
}
