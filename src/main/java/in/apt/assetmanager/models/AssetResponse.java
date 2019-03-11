package in.apt.assetmanager.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetResponse extends Response implements Serializable {

    private String id;

    private String status;

    private String downloadURL;

    private List<AssetResponse> assets;

    @Builder
    public AssetResponse(String message, String id, String status, String downloadURL, List<AssetResponse> assets) {
        super(message);
        this.id = id;
        this.status = status;
        this.downloadURL = downloadURL;
        this.assets = assets;
    }
}
