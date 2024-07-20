package org.example.neptuneojserver.dto.judges;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EngineDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String status;
}
