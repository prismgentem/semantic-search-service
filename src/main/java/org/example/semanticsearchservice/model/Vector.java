package org.example.semanticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vector {
    private Long id;
    private List<Float> vector;
    private Object payload;
}
