package codeep.jean.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseDTO {
    private String from;
    private String to;
    private String title;
    private String message;
}
