package in.apt.assetmanager.web;

import in.apt.assetmanager.exception.AssetManagerException;
import in.apt.assetmanager.models.AssetModel;
import in.apt.assetmanager.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/asset")
@Api(value = "Asset", basePath = "/Asset", description = "Route to manage assets")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    @ApiOperation(value = "Uploads asset to storage and registers with the timestamp"
            , response = ResponseEntity.class
            , consumes = "file"
            , produces = "application/json")
    public ResponseEntity<?> registerAndUploadAsset(@RequestPart("file")MultipartFile file) throws AssetManagerException {
        return ResponseEntity.ok(fileService.uploadAsset(file));
    }

    @GetMapping("/{assetId}")
    @ApiOperation(value = "Return s3 signed url for download with the timeout in seconds. If timeout not specified default is assumed"
            , response = ResponseEntity.class
            , consumes = "application/json"
            , produces = "application/json")
    public ResponseEntity<?> getAssetUrlWithTTl(@PathVariable("assetId") String assetId,
                                                @RequestParam(value = "timeout", required = false) String timeout) throws AssetManagerException {
        return ResponseEntity.ok(fileService.fetchAssetUrlWithTTL(assetId, timeout));
    }

    @PatchMapping("/{assetId}")
    @ApiOperation(value = "Marks assets's status as passed in PATCH requests for the key 'status'. Accepts only valid status types"
            , response = ResponseEntity.class
            , consumes = "application/json"
            , produces = "application/json")
    public ResponseEntity<?> updateAssetStatus(@PathVariable("assetId") String assetId,
                                               @RequestBody AssetModel asset) throws AssetManagerException {
        return  ResponseEntity.ok(fileService.updateAssetStatus(assetId ,asset));
    }


}
