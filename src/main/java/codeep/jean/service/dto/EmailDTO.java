package codeep.jean.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EmailDTO {
    private String address;
    private String title;
    private String message;
    //private MultipartFile file;
}
