package in.apt.assetmanager.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class Asset {

    @Id
    private Long id;

    private String name;

    private Date timestamp;

    private String url;

    private String type;

    @Indexed
    private String status;

    private Long size;
}
