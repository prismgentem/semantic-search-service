package org.example.semanticsearchservice.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SimpleVectorizer {
    public static List<Float> vectorize(String text) {
        return Arrays.stream(text.split(" "))
                .map(word -> (float) word.hashCode())
                .collect(Collectors.toList());
    }
}

