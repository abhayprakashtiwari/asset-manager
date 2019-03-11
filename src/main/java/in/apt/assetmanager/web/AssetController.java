package in.apt.assetmanager.web;

import in.apt.assetmanager.service.AssetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assetlist")
@Api(value = "AssetList", basePath = "/assetlist", description = "Route to fetch assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("")
    @ApiOperation(value = "Fetch list of Active assets"
            , response = ResponseEntity.class
            , consumes = "application/json"
            , produces = "application/json")
    public ResponseEntity<?> getActiveAssets(){
        return ResponseEntity.ok(assetService.getActiveAssets());
    }


}
